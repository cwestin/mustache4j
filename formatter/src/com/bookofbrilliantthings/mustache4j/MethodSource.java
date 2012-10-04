package com.bookofbrilliantthings.mustache4j;

import java.lang.reflect.Method;
import java.util.LinkedList;

public class MethodSource
    implements ValueSource
{
    private final Method method;

    public MethodSource(Method method)
    {
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
}
