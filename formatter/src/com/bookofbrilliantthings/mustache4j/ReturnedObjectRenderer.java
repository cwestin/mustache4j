package com.bookofbrilliantthings.mustache4j;

import java.lang.reflect.Method;
import java.util.LinkedList;

public class ReturnedObjectRenderer
    extends ObjectSectionRenderer
{
    private final Method method;

    public ReturnedObjectRenderer(final LinkedList<FragmentRenderer> fragmentList,
            final boolean inverted, final Method method)
    {
        super(fragmentList, inverted, method.getReturnType(), method.getDeclaringClass());
        this.method = method;
    }

    @Override
    public Object getValue(final Object o)
        throws Exception
    {
        return method.invoke(o, (Object [])null);
    }
}
