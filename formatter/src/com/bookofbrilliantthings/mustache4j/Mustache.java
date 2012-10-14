package com.bookofbrilliantthings.mustache4j;

import java.io.Reader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.LinkedList;

public class Mustache
{
    private static class BaseHandler
        extends StackableParserHandler
    {
        final protected LinkedList<FragmentRenderer> fragmentList;
        final protected boolean inverted;
        final protected ValueSource valueSource;
        final protected Class<? extends FragmentRenderer> rendererClass;
        final protected StackingParserHandler stackingParserHandler;
        final protected MustacheServices mustacheServices;
        protected Locator locator;

        protected BaseHandler(LinkedList<FragmentRenderer> fragmentList, boolean inverted,
                ValueSource valueSource, Class<? extends FragmentRenderer> rendererClass,
                StackingParserHandler stackingParserHandler, MustacheServices mustacheServices)
        {
            this.fragmentList = fragmentList;
            this.inverted = inverted;
            this.valueSource = valueSource;
            this.rendererClass = rendererClass;
            this.stackingParserHandler = stackingParserHandler;
            this.mustacheServices = mustacheServices;
        }

        @Override
        public void resume(FragmentRenderer fragmentRenderer)
        {
            fragmentList.add(fragmentRenderer);
        }

        @Override
        public void setLocator(Locator locator)
        {
            this.locator = locator;
        }

        @Override
        public void literal(String literal)
            throws MustacheParserException
        {
            fragmentList.add(new LiteralRenderer(literal));
        }
    }

    private static class ObjectHandler
        extends BaseHandler
    {
        protected final HashMap<String, ValueSource> valueNameMap;
        protected final Class<?> forClass;

        private static String getBeanName(final String methodName, final Class<?> forClass)
            throws MustacheParserException
        {
            if (!methodName.startsWith("get"))
                throw new MustacheParserException(null, "class '" + forClass.getName() +
                        "', MustacheValue method '" + methodName + "', name does not begin with 'get'");

            if (methodName.length() < 4)
                throw new MustacheParserException(null, "class '" + forClass.getName() +
                        "', MustacheValue method '" + methodName + "', name does not have anything after 'get'");

            final String beanName = Character.toLowerCase(methodName.charAt(3)) + methodName.substring(4);
            return beanName;
        }

        ObjectHandler(final LinkedList<FragmentRenderer> fragmentList, boolean inverted,
                ValueSource valueSource, Class<? extends FragmentRenderer> rendererClass,
                StackingParserHandler stackingParserHandler, MustacheServices mustacheServices,
                Class<?> forClass)
            throws MustacheParserException
        {
            super(fragmentList, inverted, valueSource, rendererClass, stackingParserHandler, mustacheServices);
            this.forClass = forClass;
            valueNameMap = new HashMap<String, ValueSource>();

            // analyze the class; we'll build up knowledge of all the fields and methods and their returns

            // index the fields
            final Field fields[] = forClass.getFields(); // only returns "accessible public fields"
            for(Field field : fields)
            {
                // check for the annotation, and if present, use the tagname
                final MustacheValue mustacheValue = field.getAnnotation(MustacheValue.class);
                if (mustacheValue == null)
                    continue;

                final String tagname = mustacheValue.tagname().isEmpty() ?
                        field.getName() : mustacheValue.tagname();

                valueNameMap.put(tagname, new FieldSource(tagname, field));
            }

            // index the methods
            final Method methods[] = forClass.getMethods(); // only returns public member methods
            for(Method method : methods)
            {
                final MustacheValue mustacheValue = method.getAnnotation(MustacheValue.class);
                if (mustacheValue == null)
                    continue;

                // check that the method doesn't require parameters
                final String methodName = method.getName();
                final Class<?> paramTypes[] = method.getParameterTypes();
                if (paramTypes.length != 0)
                    throw new MustacheParserException(null, "class '" + forClass.getName() +
                            "', MustacheValue method '" + methodName +
                            "', method must not have any parameters");

                // check that the method returns a value
                final Class<?> returnType = method.getReturnType();
                if (returnType == void.class)
                    throw new MustacheParserException(null, "class '" + forClass.getName() +
                            "', MustacheValue method '" + methodName +
                            "', must return a value");

                final String tagname = mustacheValue.tagname().isEmpty() ?
                        getBeanName(methodName, forClass) : mustacheValue.tagname();

                valueNameMap.put(tagname,  new MethodSource(tagname, method));
            }
        }

        @Override
        public void variable(String varName)
            throws MustacheParserException
        {
            if (!valueNameMap.containsKey(varName))
                throw new MustacheParserException(locator,
                        "no MustacheValue named '" + varName + "' in object");

            final ValueSource valueSource = valueNameMap.get(varName);
            fragmentList.add(valueSource.createVariableRenderer(true));
        }

        @Override
        public void sectionBegin(String secName, boolean inverted)
            throws MustacheParserException
        {
            if (!valueNameMap.containsKey(secName))
                throw new MustacheParserException(locator,
                        "no MustacheValue named '" + secName + "' in object");

            // gather information we need to decide what to instantiate
            final ValueSource valueSource = valueNameMap.get(secName);
            final Class<?> valueType = valueSource.getType();
            final PrimitiveType pt = PrimitiveType.getSwitchType(valueType);

            // begin constructing what we need for the section
            final LinkedList<FragmentRenderer> fragmentList = new LinkedList<FragmentRenderer>();

            if (pt == PrimitiveType.BOOLEAN)
            {
                final ObjectHandler objectHandler =
                        new ObjectHandler(fragmentList, inverted, valueSource,
                                valueSource.getConditionalRendererClass(), stackingParserHandler,
                                mustacheServices, forClass);

                stackingParserHandler.push(objectHandler);
                return;
            }

            if (pt == PrimitiveType.STRING)
            {
                final ObjectHandler objectHandler =
                        new ObjectHandler(fragmentList, inverted, valueSource,
                                valueSource.getStringSectionRendererClass(), stackingParserHandler,
                                mustacheServices, forClass);

                stackingParserHandler.push(objectHandler);
                return;
            }

            if (pt == PrimitiveType.OBJECT)
            {
                // check for Iterable<T>
                if (Iterable.class.isAssignableFrom(valueType))
                {
                    if (inverted)
                        throw new MustacheParserException(locator, "section \"" + secName +
                                "\" can't be inverted, because it's an Iterable<?>");

                    final ParameterizedType iterableType = (ParameterizedType)valueSource.getGenericType();
                    final Type argTypes[] = iterableType.getActualTypeArguments();
                    if (!(argTypes[0] instanceof Class<?>))
                        throw new MustacheParserException(locator, "Iterable type parameter must be concrete");
                    final Class<?> iteratedClass = (Class<?>)argTypes[0];
                    // TODO later, we can chase it up the declarations if it's a TypeVariable, and get
                    // the instantiated type

                    final ObjectHandler objectHandler =
                            new ObjectHandler(fragmentList, inverted, valueSource,
                                    valueSource.getIterableRendererClass(), stackingParserHandler,
                                    mustacheServices, iteratedClass);
                    stackingParserHandler.push(objectHandler);
                    return;
                }

                // check for HashMap<String, T>
                // TODO

                // check for arrays
                // TODO

                // reject other generics for the time being
                // TODO detect these

                final ObjectHandler objectHandler =
                        new ObjectHandler(fragmentList, inverted, valueSource,
                                valueSource.getObjectRendererClass(), stackingParserHandler, mustacheServices,
                                (inverted ? forClass : valueType));
                stackingParserHandler.push(objectHandler);
                return;
            }

            throw new MustacheParserException(locator, "section '" + secName +
                    "': don't know what to do with MustacheValue '" + valueSource.getName() +
                    "' of type '" + valueType.getName() + "'");
        }

        @Override
        public void sectionEnd(String secName)
            throws MustacheParserException
        {
            stackingParserHandler.pop(valueSource.createRenderer(rendererClass, fragmentList, inverted));
        }

        @Override
        public void unescaped(final String varName)
            throws MustacheParserException
        {
            if (!valueNameMap.containsKey(varName))
                throw new MustacheParserException(locator,
                        "no MustacheValue named '" + varName + "' in object");

            final ValueSource valueSource = valueNameMap.get(varName);
            fragmentList.add(valueSource.createVariableRenderer(false));
        }

        @Override
        public void partial(String partialName)
            throws MustacheParserException
        {
            final MustacheRenderer renderer = mustacheServices.getRenderer(partialName, forClass);
            fragmentList.add(renderer);
        }
    }

    public static MustacheRenderer compile(final MustacheServices mustacheServices,
            final Reader templateReader, final Class<?> forClass)
        throws MustacheParserException
    {
        final LinkedList<FragmentRenderer> fragmentList = new LinkedList<FragmentRenderer>();
        final StackingParserHandler stackingParserHandler = new StackingParserHandler();
        final ObjectHandler objectHandler =
                new ObjectHandler(fragmentList, false, null, null, stackingParserHandler,
                        mustacheServices, forClass);
        stackingParserHandler.push(objectHandler);

        Template.parse(stackingParserHandler, templateReader);

        return new StaticRenderer(fragmentList, forClass);
    }

/* DISABLED
    The dynamism that this introduces makes it very hard to deal with efficiently, because we essentially
    have to check everything as we render the template. In the Object case above, we don't have to do that
    because we know the objects' types at compile time. If HashMapRenderer and HashMapValueRenderer encounter
    an Object, they'll have to compile a renderer for it on the fly, since we can't rely on that key always
    having that type of value.

    For my purposes, I'm only interested in Object rendering, because I plan to use object implementation to
    lazily fetch database fields, so I'm not going to go down this path for now.

    private static class HashMapHandler
        extends BaseHandler
    {
        HashMapHandler(LinkedList<FragmentRenderer> fragmentList,
                RendererFactory rendererFactory,
                StackingParserHandler stackingParserHandler)
        {
            super(fragmentList, rendererFactory, stackingParserHandler);
        }

        @Override
        public void variable(String varName)
            throws MustacheParserException
        {
            fragmentList.add(new HashMapValueRenderer(varName));
        }

        @Override
        public void sectionBegin(String secName, boolean inverted)
            throws MustacheParserException
        {
            throw new RuntimeException("unimplemented");
        }

        @Override
        public void sectionEnd(String secName)
            throws MustacheParserException
        {
            throw new RuntimeException("unimplemented");
        }
    }

    public static MustacheRenderer compileForHashMap(final Reader templateReader)
        throws IOException, MustacheParserException
    {
        final LinkedList<FragmentRenderer> fragmentList = new LinkedList<FragmentRenderer>();
        final StackingParserHandler stackingParserHandler = new StackingParserHandler();
        final HashMapHandler hashMapHandler = new HashMapHandler(fragmentList, null, stackingParserHandler);

        stackingParserHandler.push(hashMapHandler);
        Template.parse(stackingParserHandler, templateReader);

        return new HashMapRenderer(fragmentList);
    }
*/
}
