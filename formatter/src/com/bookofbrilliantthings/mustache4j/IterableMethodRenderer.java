package com.bookofbrilliantthings.mustache4j;

import java.lang.reflect.Method;
import java.util.LinkedList;

public class IterableMethodRenderer
    extends IterableRenderer
{
    private final Method method;

    public IterableMethodRenderer(LinkedList<FragmentRenderer> fragmentList, int objectDepth,
            boolean inverted, Method method)
    {
        super(fragmentList, objectDepth, inverted, method.getGenericReturnType());
        this.method = method;
    }

    @Override
    public Object getIterable(Object o)
            throws Exception
    {
        return method.invoke(o, (Object[])null);
    }
}
