package com.bookofbrilliantthings.mustache4j;

import java.util.ArrayList;
import java.util.List;

import com.bookofbrilliantthings.mustache4j.util.SwitchableWriter;

public class ListRenderer
    implements FragmentRenderer
{
    private final List<FragmentRenderer> fragmentList;

    public ListRenderer(List<FragmentRenderer> fragmentList, Class<?> forClass)
    {
        this.fragmentList = new ArrayList<FragmentRenderer>(fragmentList); // clone into an array
    }

    @Override
    public void render(SwitchableWriter writer, ObjectStack objectStack)
        throws Exception
    {
        for(FragmentRenderer renderer : fragmentList)
        {
            renderer.render(writer, objectStack);
        }
    }
}
