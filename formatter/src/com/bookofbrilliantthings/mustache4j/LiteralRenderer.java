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
    public void render(final HtmlEscapeWriter writer, final Object o)
        throws Exception
    {
        // in order to write this without escaping, use the original writer
        final Writer unescapedWriter = writer.getUnescapedWriter();
        unescapedWriter.write(string);
    }

}
