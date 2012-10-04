package com.bookofbrilliantthings.mustache4j;

import java.lang.reflect.Method;
import java.util.List;

public class ConditionalMethodRenderer
    extends ConditionalSectionRenderer
{
    private final Method method;

    public ConditionalMethodRenderer(final List<FragmentRenderer> fragmentList, final boolean inverted,
            final Method booleanMethod)
    {
        super(fragmentList, inverted, booleanMethod.getReturnType(), booleanMethod.getDeclaringClass());
        this.method = booleanMethod;
    }

    @Override
    public boolean getCondition(final Object o)
        throws Exception
    {
        final Boolean b = (Boolean)method.invoke(o, (Object [])null);
        return b.booleanValue();
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
            return new ConditionalMethodRenderer(fragmentList, inverted, method);
        }
    }

    public static RendererFactory createFactory(final List<FragmentRenderer> fragmentList,
            final boolean inverted, final Method booleanMethod)
    {
        return new MyFactory(fragmentList, inverted, booleanMethod);
    }
}
