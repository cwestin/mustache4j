package com.bookofbrilliantthings.mustache4j;

import java.io.Writer;

public class StringRenderer
    implements FragmentRenderer
{
    private final String string;

    public StringRenderer(final String string)
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
