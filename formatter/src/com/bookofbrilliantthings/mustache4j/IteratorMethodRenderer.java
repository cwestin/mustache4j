package com.bookofbrilliantthings.mustache4j;

import java.lang.reflect.Method;
import java.util.LinkedList;

public class IteratorMethodRenderer
    extends IteratorRenderer
{
    private final Method method;

    public IteratorMethodRenderer(LinkedList<FragmentRenderer> fragmentList, int objectDepth,
            boolean inverted, Method method)
    {
        super(fragmentList, objectDepth, inverted, method.getGenericReturnType());
        this.method = method;
    }

    @Override
    public Object getIterator(Object o)
            throws Exception
    {
        return method.invoke(o, (Object[])null);
    }
}
