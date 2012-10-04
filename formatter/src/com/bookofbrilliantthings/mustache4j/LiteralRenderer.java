package com.bookofbrilliantthings.mustache4j;

import java.io.Writer;

public class LiteralRenderer
    implements FragmentRenderer
{
    private final String string;

    public LiteralRenderer(final String string)
    {
        this.string = string;
    }

    @Override
    public void render(final Writer writer, final Object o)
        throws Exception
    {
        writer.write(string);
    }

}
