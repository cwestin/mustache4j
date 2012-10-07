package com.bookofbrilliantthings.mustache4j;

import com.bookofbrilliantthings.mustache4j.util.SwitchableWriter;

public class WrappedMustacheRenderer
    implements FragmentRenderer
{
    private final MustacheRenderer mustacheRenderer;

    public WrappedMustacheRenderer(MustacheRenderer mustacheRenderer)
    {
        this.mustacheRenderer = mustacheRenderer;
    }

    @Override
    public void render(SwitchableWriter switchableWriter, Object o)
            throws Exception
    {
        mustacheRenderer.renderNested(switchableWriter, o);
    }
}
