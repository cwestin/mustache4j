package com.bookofbrilliantthings.mustache4j;

import com.bookofbrilliantthings.mustache4j.util.SwitchableWriter;

public class LiteralRenderer
    implements FragmentRenderer
{
    private final String string;

    public LiteralRenderer(final String string)
    {
        this.string = string;
    }

    @Override
    public void render(final SwitchableWriter writer, final ObjectStack objectStack)
        throws Exception
    {
        // in order to write this without escaping, use the original writer
        final boolean on = writer.setFiltered(false);
        writer.write(string);
        writer.setFiltered(on);
    }
}
