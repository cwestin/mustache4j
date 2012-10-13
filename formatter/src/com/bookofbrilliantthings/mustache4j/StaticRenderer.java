package com.bookofbrilliantthings.mustache4j;

import java.util.List;

import com.bookofbrilliantthings.mustache4j.util.SwitchableWriter;

public class StaticRenderer
    extends MustacheRenderer
{
    private final ObjectRenderer objectRenderer;

    public StaticRenderer(List<FragmentRenderer> fragmentList, Class<?> forClass)
    {
        objectRenderer = new ObjectRenderer(fragmentList, forClass);
    }

    @Override
    public void render(SwitchableWriter switchableWriter, Object o)
        throws Exception
    {
        objectRenderer.render(switchableWriter, o);
    }
}
