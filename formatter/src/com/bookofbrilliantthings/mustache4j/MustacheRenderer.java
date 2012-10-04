package com.bookofbrilliantthings.mustache4j;

import java.io.Writer;
import java.util.List;

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
        HtmlEscapeWriter htmlEscapeWriter = new HtmlEscapeWriter(writer);
        objectRenderer.render(htmlEscapeWriter, o);
    }
}
