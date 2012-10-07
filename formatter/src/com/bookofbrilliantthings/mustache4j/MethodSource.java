package com.bookofbrilliantthings.mustache4j;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.LinkedList;

public class MethodSource
    extends ValueSource
{
    private final Method method;

    public MethodSource(String name, Method method)
    {
        super(name);
        this.method = method;
    }

    @Override
    public Class<?> getType()
    {
        return method.getReturnType();
    }

    @Override
    public VariableRenderer createVariableRenderer(boolean escaped)
    {
        return new MethodReturnRenderer(escaped, method);
    }

    @Override
    public Class<? extends FragmentRenderer> getObjectRendererClass()
    {
        return ReturnedObjectRenderer.class;
    }

    @Override
    public Class<? extends FragmentRenderer> getConditionalRendererClass()
    {
        return ConditionalMethodRenderer.class;
    }

    @Override
    public Class<? extends FragmentRenderer> getStringSectionRendererClass()
    {
        return StringMethodSectionRenderer.class;
    }

    @Override
    public <T extends FragmentRenderer> T createRenderer(
            Class<T> rendererClass, LinkedList<FragmentRenderer> fragmentList, boolean inverted)
        throws MustacheParserException
    {
        try
        {
            Constructor<T> ctor = rendererClass.getConstructor(LinkedList.class, boolean.class, Method.class);
            T fragmentRenderer = ctor.newInstance(fragmentList, inverted, method);
            return fragmentRenderer;
        }
        catch(NoSuchMethodException nsme)
        {
            throw new MustacheParserException(null, "class " + rendererClass.getName() +
                    " has no constructor with signature (LinkedList<FragmentRenderer>, boolean, Method)", nsme);
        }
        catch(Exception e)
        {
            throw new MustacheParserException(null, "could not instantiate renderer of class " +
                    rendererClass.getName(), e);
        }
    }
}
