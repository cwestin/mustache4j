package com.bookofbrilliantthings.mustache4j;

import java.util.List;

import com.bookofbrilliantthings.mustache4j.util.SwitchableWriter;

public class StaticRenderer
    extends MustacheRenderer
{
    private final ListRenderer objectRenderer;

    public StaticRenderer(List<FragmentRenderer> fragmentList, Class<?> forClass)
    {
        objectRenderer = new ListRenderer(fragmentList, forClass);
    }

    @Override
    public void render(SwitchableWriter switchableWriter, ObjectStack objectStack)
        throws Exception
    {
        objectRenderer.render(switchableWriter, objectStack);
    }
}
