package com.bookofbrilliantthings.mustache4j;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedList;

import com.sun.xml.internal.bind.v2.schemagen.xmlschema.List;


public class Mustache
{
    private static class BaseHandler
        extends StackableParserHandler
    {
        final protected LinkedList<FragmentRenderer> fragmentList;
        protected Locator locator;
        final private RendererFactory rendererFactory;
        final protected StackingParserHandler stackingParserHandler;

        protected BaseHandler(LinkedList<FragmentRenderer> fragmentList, RendererFactory rendererFactory,
                StackingParserHandler stackingParserHandler)
        {
            this.fragmentList = fragmentList;
            this.rendererFactory = rendererFactory;
            this.stackingParserHandler = stackingParserHandler;
        }

        FragmentRenderer createRenderer()
        {
            return rendererFactory.createRenderer();
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
        protected final HashMap<String, Field> fieldNameMap;
        protected final HashMap<String, Method> methodNameMap;
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

        ObjectHandler(final LinkedList<FragmentRenderer> fragmentList, Class<?> forClass,
                RendererFactory rendererFactory, StackingParserHandler stackingParserHandler)
            throws MustacheParserException
        {
            super(fragmentList, rendererFactory, stackingParserHandler);
            fieldNameMap = new HashMap<String, Field>();
            methodNameMap = new HashMap<String, Method>();
            valueNameMap = new HashMap<String, ValueSource>();
            this.forClass = forClass;

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

                fieldNameMap.put(tagname, field);
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

                methodNameMap.put(tagname, method);
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
            final VariableRenderer variableRenderer = valueSource.createVariableRenderer();
            fragmentList.add(variableRenderer);
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
                final RendererFactory rendererFactory =
                        valueSource.createConditionalRendererFactory(fragmentList, inverted);
                final ObjectHandler objectHandler =
                        new ObjectHandler(fragmentList, forClass, rendererFactory, stackingParserHandler);

                stackingParserHandler.push(objectHandler);
                return;
            }

            if (pt == PrimitiveType.STRING)
            {
                final RendererFactory rendererFactory =
                        valueSource.createStringSectionRendererFactory(fragmentList, inverted);
                final ObjectHandler objectHandler =
                        new ObjectHandler(fragmentList, forClass, rendererFactory, stackingParserHandler);

                stackingParserHandler.push(objectHandler);
                return;
            }

            if (pt == PrimitiveType.OBJECT)
            {
                // check for List<T> (follow up with getGenericType())
                if (List.class.isAssignableFrom(valueType))
                {
/* TODO
                        final ObjectHandler objectHandler = new ObjectHandler(fragmentList, x,
                                ListRenderer.createFactory(fragmentList, field), stackingParserHandler);

                        stackingParserHandler.push(objectHandler);
                        return;
 */
                    throw new RuntimeException("List<T> field unimplemented");
                }

                // check for Iterable<T>
                if (Iterable.class.isAssignableFrom(valueType))
                {
                    // TODO
                    throw new RuntimeException("Iterable<T> field unimplemented");
                }

                // check for HashMap<String, T>
                // TODO

                // check for arrays
                // TODO

                // reject other generics for the time being
                // TODO detect these

                final RendererFactory rendererFactory =
                        valueSource.createObjectRendererFactory(fragmentList, inverted);
                final ObjectHandler objectHandler =
                        new ObjectHandler(fragmentList, (inverted ? forClass : valueType),
                                rendererFactory, stackingParserHandler);
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
            stackingParserHandler.pop(createRenderer());
        }
    }

    public static MustacheRenderer compile(final Reader templateReader, final Class<?> forClass)
        throws IOException, MustacheParserException
    {
        final LinkedList<FragmentRenderer> fragmentList = new LinkedList<FragmentRenderer>();
        final StackingParserHandler stackingParserHandler = new StackingParserHandler();
        final ObjectHandler objectHandler =
                new ObjectHandler(fragmentList, forClass, null, stackingParserHandler);
        stackingParserHandler.push(objectHandler);

        Template.parse(stackingParserHandler, templateReader);

        return new TopLevelRenderer(fragmentList, forClass);
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
