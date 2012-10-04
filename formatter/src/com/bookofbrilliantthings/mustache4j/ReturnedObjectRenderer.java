package com.bookofbrilliantthings.mustache4j;

import java.lang.reflect.Method;
import java.util.List;

public class ReturnedObjectRenderer
    extends ObjectSectionRenderer
{
    private final Method method;

    public ReturnedObjectRenderer(final List<FragmentRenderer> fragmentList,
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

    private static class MyFactory
        extends Factory
    {
        private final Method method;

        MyFactory(List<FragmentRenderer> fragmentList, boolean inverted, Method method)
        {
            super(fragmentList, inverted);
            this.method = method;
        }

        @Override
        public FragmentRenderer createRenderer()
        {
            return new ReturnedObjectRenderer(fragmentList, inverted, method);
        }
    }

    public static RendererFactory createFactory(final List<FragmentRenderer> fragmentList,
            final boolean inverted, final Method method)
    {
        return new MyFactory(fragmentList, inverted, method);
    }
}
