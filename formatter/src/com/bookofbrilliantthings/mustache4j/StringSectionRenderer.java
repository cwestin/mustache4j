package com.bookofbrilliantthings.mustache4j;

import java.util.LinkedList;

import com.bookofbrilliantthings.mustache4j.util.SwitchableWriter;

public abstract class StringSectionRenderer
    extends StackingRenderer
{
    private final ListRenderer objectRenderer;
    private final boolean inverted;

    protected StringSectionRenderer(final LinkedList<FragmentRenderer> fragmentList,
            final int objectDepth, final boolean inverted, Class<?> forClass)
    {
        super(objectDepth);
        objectRenderer = new ListRenderer(fragmentList, forClass);
        this.inverted = inverted;
    }

    public abstract String getString(Object o)
        throws Exception;

    @Override
    public void render(final SwitchableWriter writer, final ObjectStack objectStack)
            throws Exception
    {
        final String s = getString(objectStack.peekAt(objectDepth));

        if ((s != null) ^ inverted)
        {
            objectStack.repush();
            objectRenderer.render(writer, objectStack);
            objectStack.pop();
        }
    }
}
