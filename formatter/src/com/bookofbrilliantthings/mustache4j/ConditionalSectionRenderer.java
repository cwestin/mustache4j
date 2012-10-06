package com.bookofbrilliantthings.mustache4j;

import java.util.LinkedList;

public abstract class ConditionalSectionRenderer
    implements FragmentRenderer
{
    private final ObjectRenderer objectRenderer;
    private final boolean inverted;

    protected ConditionalSectionRenderer(final LinkedList<FragmentRenderer> fragmentList,
            final boolean inverted, Class<?> conditionClass, Class<?> forClass)
    {
        if (!conditionClass.equals(boolean.class) && !conditionClass.equals(Boolean.class))
            throw new IllegalArgumentException("conditional section can only operate on boolean or Boolean");

        objectRenderer = new ObjectRenderer(fragmentList, forClass);
        this.inverted = inverted;
    }

    public abstract boolean getCondition(Object o)
        throws Exception;

    @Override
    public void render(final HtmlEscapeWriter writer, final Object o)
        throws Exception
    {
        if (getCondition(o) ^ inverted)
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
