package com.bookofbrilliantthings.mustache4j;

import java.lang.reflect.Method;

public class MethodReturnRenderer
    extends VariableRenderer
{
    private final Method method;

    public MethodReturnRenderer(int objectDepth, boolean escaped, Method method)
    {
        super(objectDepth, escaped, method.getReturnType());
        this.method = method;
    }

    @Override
    public Object getValue(final Object o)
        throws Exception
    {
        return method.invoke(o, (Object [])null);
    }
}
