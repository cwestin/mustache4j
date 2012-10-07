package com.bookofbrilliantthings.mustache4j;

import java.util.LinkedList;

import com.bookofbrilliantthings.mustache4j.util.SwitchableWriter;

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
    public void render(final SwitchableWriter writer, final Object o)
        throws Exception
    {
        final String s = getString(o);

        if ((s != null) ^ inverted)
            objectRenderer.render(writer, o);
    }
}
