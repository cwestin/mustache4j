package com.bookofbrilliantthings.mustache4j;

import java.util.LinkedList;

import com.bookofbrilliantthings.mustache4j.util.SwitchableWriter;

public abstract class ConditionalSectionRenderer
        extends StackingRenderer
{
    private final ListRenderer objectRenderer;
    private final boolean inverted;

    protected ConditionalSectionRenderer(final LinkedList<FragmentRenderer> fragmentList,
            final int objectDepth, final boolean inverted, Class<?> conditionClass, Class<?> forClass)
    {
        super(objectDepth);

        if (!conditionClass.equals(boolean.class) && !conditionClass.equals(Boolean.class))
            throw new IllegalArgumentException("conditional section can only operate on boolean or Boolean");

        objectRenderer = new ListRenderer(fragmentList, forClass);
        this.inverted = inverted;
    }

    public abstract boolean getCondition(Object o)
        throws Exception;

    @Override
    public void render(final SwitchableWriter writer, final ObjectStack objectStack)
            throws Exception
    {
        if (getCondition(objectStack.peekAt(objectDepth)) ^ inverted)
            objectRenderer.render(writer, objectStack);
    }
}
