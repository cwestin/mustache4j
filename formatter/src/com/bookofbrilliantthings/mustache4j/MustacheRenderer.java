package com.bookofbrilliantthings.mustache4j;

import java.io.Writer;
import java.util.List;

import com.bookofbrilliantthings.mustache4j.util.CorrectEscapeWriter;
import com.bookofbrilliantthings.mustache4j.util.SwitchableWriter;

public class MustacheRenderer
{
    private final ObjectRenderer objectRenderer;

    public MustacheRenderer(List<FragmentRenderer> fragmentList, Class<?> forClass)
    {
        objectRenderer = new ObjectRenderer(fragmentList, forClass);
    }

    public void render(Writer writer, Object o)
        throws Exception
    {
        final CorrectEscapeWriter htmlEscapeWriter = new CorrectEscapeWriter(writer);
        final SwitchableWriter switchableWriter = new SwitchableWriter(writer, htmlEscapeWriter);
        objectRenderer.render(switchableWriter, o);

        // flush the filter, if it hasn't been flushed since it's last write
        switchableWriter.setFiltered(false);
    }
}
