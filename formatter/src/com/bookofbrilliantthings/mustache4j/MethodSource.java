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
    public VariableRenderer createVariableRenderer()
    {
        return new MethodReturnRenderer(method);
    }

    @Override
    public RendererFactory createConditionalRendererFactory(
            LinkedList<FragmentRenderer> fragmentList, boolean inverted)
    {
        return ConditionalMethodRenderer.createFactory(fragmentList, inverted, method);
    }

    @Override
    public RendererFactory createObjectRendererFactory(
            LinkedList<FragmentRenderer> fragmentList, boolean inverted)
    {
        return ReturnedObjectRenderer.createFactory(fragmentList, inverted, method);
    }

    @Override
    public RendererFactory createStringSectionRendererFactory(
            LinkedList<FragmentRenderer> fragmentList, boolean inverted)
    {
        return StringMethodSectionRenderer.createFactory(fragmentList, inverted, method);
    }

    @Override
    public FragmentRenderer createObjectRenderer(
            LinkedList<FragmentRenderer> fragmentList, boolean inverted)
    {
        return new ReturnedObjectRenderer(fragmentList, inverted, method);
    }

    @Override
    public FragmentRenderer createConditionalRenderer(
            LinkedList<FragmentRenderer> fragmentList, boolean inverted)
    {
        return new ConditionalMethodRenderer(fragmentList, inverted, method);
    }

    @Override
    public FragmentRenderer createStringSectionRenderer(
            LinkedList<FragmentRenderer> fragmentList, boolean inverted)
    {
        return new StringMethodSectionRenderer(fragmentList, inverted, method);
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
