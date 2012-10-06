package com.bookofbrilliantthings.mustache4j;

import java.util.LinkedList;

public abstract class StringSectionRenderer
    implements FragmentRenderer
{
    private final ObjectRenderer objectRenderer;
    private final boolean inverted;

    protected StringSectionRenderer(final LinkedList<FragmentRenderer> fragmentList, final boolean inverted,
            Class<?> forClass)
    {
        objectRenderer = new ObjectRenderer(fragmentList, forClass);
        this.inverted = inverted;
    }

    public abstract String getString(Object o)
        throws Exception;

    @Override
    public void render(final HtmlEscapeWriter writer, final Object o)
        throws Exception
    {
        final String s = getString(o);

        if ((s != null) ^ inverted)
            objectRenderer.render(writer, o);
    }

    protected static abstract class Factory
        implements RendererFactory
    {
        protected final LinkedList<FragmentRenderer> fragmentList;
        protected final boolean inverted;

        Factory(final LinkedList<FragmentRenderer> fragmentList, final boolean inverted)
        {
            this.fragmentList = fragmentList;
            this.inverted = inverted;
        }

        @Override
        public abstract FragmentRenderer createRenderer();
    }
}
