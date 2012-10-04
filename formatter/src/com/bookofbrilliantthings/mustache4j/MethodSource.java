package com.bookofbrilliantthings.mustache4j;

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
}
