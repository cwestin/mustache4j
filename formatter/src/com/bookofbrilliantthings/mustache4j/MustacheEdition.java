package com.bookofbrilliantthings.mustache4j;

public class MustacheEdition
{
    private final MustacheRenderer renderer;

    public MustacheEdition(MustacheRenderer renderer)
    {
        this.renderer = renderer;
    }

    public MustacheRenderer getRenderer()
    {
        return renderer;
    }

    public boolean newerAvailable()
    {
        return false;
    }
}
