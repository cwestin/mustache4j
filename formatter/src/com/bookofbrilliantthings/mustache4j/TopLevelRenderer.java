package com.bookofbrilliantthings.mustache4j;

import java.io.Writer;
import java.util.List;

import com.bookofbrilliantthings.mustache4j.util.HtmlEscapeWriter;

public class TopLevelRenderer
    extends ObjectRenderer
{
    public TopLevelRenderer(List<FragmentRenderer> fragmentList, Class<?> forClass)
    {
        super(fragmentList, forClass);
    }

    @Override
    public void render(Writer writer, Object o)
        throws Exception
    {
        HtmlEscapeWriter htmlEscapeWriter = new HtmlEscapeWriter(writer);
        super.render(htmlEscapeWriter, o);
    }
}
