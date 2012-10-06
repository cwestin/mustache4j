package com.bookofbrilliantthings.mustache4j;

import java.lang.reflect.Method;
import java.util.LinkedList;

public class StringMethodSectionRenderer
    extends StringSectionRenderer
{
    private final Method method;

    public StringMethodSectionRenderer(LinkedList<FragmentRenderer> fragmentList, boolean inverted, Method method)
    {
        super(fragmentList, inverted, method.getDeclaringClass());
        this.method = method;
    }

    @Override
    public String getString(Object o)
        throws Exception
    {
        final Object s = method.invoke(o, (Object []) null);
        if (s != null)
            return (String)s;

        return null;
    }
}
