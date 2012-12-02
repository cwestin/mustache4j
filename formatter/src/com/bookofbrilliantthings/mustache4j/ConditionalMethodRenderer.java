package com.bookofbrilliantthings.mustache4j;

import java.lang.reflect.Method;
import java.util.LinkedList;

public class ConditionalMethodRenderer
    extends ConditionalSectionRenderer
{
    private final Method method;

    public ConditionalMethodRenderer(final LinkedList<FragmentRenderer> fragmentList,
            final int objectDepth, final boolean inverted, final Method booleanMethod)
    {
        super(fragmentList, objectDepth, inverted, booleanMethod.getReturnType(), booleanMethod.getDeclaringClass());
        this.method = booleanMethod;
    }

    @Override
    public boolean getCondition(final Object o)
            throws Exception
    {
        final Boolean b = (Boolean)method.invoke(o, (Object [])null);
        return b.booleanValue();
    }
}
