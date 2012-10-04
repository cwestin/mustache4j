package com.bookofbrilliantthings.mustache4j;

import java.lang.reflect.Method;
import java.util.List;

public class StringMethodSectionRenderer
    extends StringSectionRenderer
{
    private final Method method;

    public StringMethodSectionRenderer(List<FragmentRenderer> fragmentList, boolean inverted, Method method)
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

    private static class MyFactory
        extends Factory
    {
        private final Method method;

        MyFactory(final List<FragmentRenderer> fragmentList, final boolean inverted, final Method method)
        {
            super(fragmentList, inverted);
            this.method = method;
        }

        @Override
        public FragmentRenderer createRenderer()
        {
            return new StringMethodSectionRenderer(fragmentList, inverted, method);
        }
    }

    public static RendererFactory createFactory(final List<FragmentRenderer> fragmentList,
            final boolean inverted, final Method method)
    {
        return new MyFactory(fragmentList, inverted, method);
    }
}
