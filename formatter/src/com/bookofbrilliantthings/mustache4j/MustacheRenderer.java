package com.bookofbrilliantthings.mustache4j;

import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

public class MustacheRenderer
    implements FragmentRenderer
{
    private final List<FragmentRenderer> fragmentList;

    public MustacheRenderer(List<FragmentRenderer> fragmentList)
    {
        this.fragmentList = new ArrayList<FragmentRenderer>(fragmentList); // clone into an array
    }

    @Override
    public void render(Writer writer, Object o)
        throws Exception
    {
        for(FragmentRenderer renderer : fragmentList)
        {
            renderer.render(writer, o);
        }
    }
}
