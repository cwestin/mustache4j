package com.bookofbrilliantthings.mustache4j;

import java.io.Writer;
import java.lang.reflect.Method;
import java.util.List;

public class ConditionalMethodRenderer
    implements FragmentRenderer
{
    private final ObjectRenderer objectRenderer;
    private final boolean inverted;
    private final Method method;

    public ConditionalMethodRenderer(final List<FragmentRenderer> fragmentList, final boolean inverted,
            final Method booleanMethod)
    {
        assert booleanMethod.getReturnType() == boolean.class;
        objectRenderer = new ObjectRenderer(fragmentList, booleanMethod.getDeclaringClass());
        this.inverted = inverted;
        this.method = booleanMethod;
    }

    @Override
    public void render(final Writer writer, final Object o)
        throws Exception
    {
        final Boolean b0 = (Boolean)method.invoke(o, (Object [])null);
        final boolean b = b0.booleanValue() ^ inverted;
        if (!b)
            return;

        objectRenderer.render(writer, o);
    }

    private static class MyFactory
        implements RendererFactory
    {
        private final List<FragmentRenderer> fragmentList;
        private final boolean inverted;
        private final Method method;

        MyFactory(final List<FragmentRenderer> fragmentList, final boolean inverted, final Method method)
        {
            this.fragmentList = fragmentList;
            this.inverted = inverted;
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
