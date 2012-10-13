package com.bookofbrilliantthings.mustache4j;

import java.io.Writer;

import com.bookofbrilliantthings.mustache4j.util.HtmlEscapeWriter;
import com.bookofbrilliantthings.mustache4j.util.SwitchableWriter;

public abstract class MustacheRenderer
    implements FragmentRenderer
{
    public void render(Writer writer, Object o)
        throws Exception
    {
        final HtmlEscapeWriter htmlEscapeWriter = new HtmlEscapeWriter(writer);
        final SwitchableWriter switchableWriter = new SwitchableWriter(writer, htmlEscapeWriter);
        render(switchableWriter, o);

        // flush the filter, if it hasn't been flushed since it's last write
        switchableWriter.setFiltered(false);
    }

    public abstract void render(SwitchableWriter switchableWriter, Object o)
        throws Exception;
}
