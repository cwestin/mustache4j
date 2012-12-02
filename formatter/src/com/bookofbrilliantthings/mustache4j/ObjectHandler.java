package com.bookofbrilliantthings.mustache4j;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.LinkedList;


public class ObjectHandler
    extends BaseHandler
{
    private final HashMap<String, ValueSource> valueNameMap;
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
            ValueReference valueReference, Class<? extends FragmentRenderer> rendererClass,
            StackingParserHandler stackingParserHandler, MustacheServices mustacheServices,
            Class<?> forClass)
        throws MustacheParserException
    {
        super(fragmentList, inverted, valueReference, rendererClass, stackingParserHandler, mustacheServices);
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
    public ValueSource getValueSource(String valueName)
    {
        return valueNameMap.get(valueName);
    }

    @Override
    public void variable(String varName)
        throws MustacheParserException
    {
        final ValueReference valueReference = findValue(varName);
        addFragment(valueReference.valueSource.createVariableRenderer(valueReference.stackDepth, true));
    }

    @Override
    public void sectionBegin(String secName, boolean inverted)
        throws MustacheParserException
    {
        final ValueReference valueReference = findValue(secName);
        final ValueSource valueSource = valueReference.valueSource;

        // gather information we need to decide what to instantiate
        final Class<?> valueType = valueSource.getType();
        final PrimitiveType pt = PrimitiveType.getSwitchType(valueType);

        // begin constructing what we need for the section
        final LinkedList<FragmentRenderer> fragmentList = new LinkedList<FragmentRenderer>();

        if (pt == PrimitiveType.BOOLEAN)
        {
            final ObjectHandler objectHandler =
                    new ObjectHandler(fragmentList, inverted, valueReference,
                            valueSource.getConditionalRendererClass(), stackingParserHandler,
                            mustacheServices, forClass);

            stackingParserHandler.push(objectHandler);
            return;
        }

        if (pt == PrimitiveType.STRING)
        {
            final ObjectHandler objectHandler =
                    new ObjectHandler(fragmentList, inverted, valueReference,
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
                        new ObjectHandler(fragmentList, inverted, valueReference,
                                valueSource.getIterableRendererClass(), stackingParserHandler,
                                mustacheServices, iteratedClass);
                stackingParserHandler.push(objectHandler);
                return;
            }

            // check for arrays
            // TODO

            // reject other generics for the time being
            // TODO detect these

            final ObjectHandler objectHandler =
                    new ObjectHandler(fragmentList, inverted, valueReference,
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
        pop();
    }

    @Override
    public void unescaped(final String varName)
        throws MustacheParserException
    {
        final ValueReference valueReference = findValue(varName);
        addFragment(valueReference.valueSource.createVariableRenderer(valueReference.stackDepth, false));
    }

    @Override
    public void partial(String partialName)
        throws MustacheParserException
    {
        final MustacheRenderer renderer = mustacheServices.getRenderer(partialName, forClass);
        addFragment(renderer);
    }
}
