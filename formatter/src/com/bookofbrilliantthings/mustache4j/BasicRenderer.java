package com.bookofbrilliantthings.mustache4j;

import java.util.ArrayList;
import java.util.List;

import com.bookofbrilliantthings.mustache4j.util.SwitchableWriter;

public class BasicRenderer
    implements FragmentRenderer
{
    private final List<FragmentRenderer> fragmentList;

    public BasicRenderer(List<FragmentRenderer> fragmentList)
    {
        this.fragmentList = new ArrayList<FragmentRenderer>(fragmentList); // clone into an array
    }

    @Override
    public void render(SwitchableWriter writer, Object o)
        throws Exception
    {
        for(FragmentRenderer renderer : fragmentList)
        {
            renderer.render(writer, o);
        }
    }
}
