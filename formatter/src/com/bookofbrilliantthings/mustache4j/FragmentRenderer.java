package com.bookofbrilliantthings.mustache4j;

import com.bookofbrilliantthings.mustache4j.util.SwitchableWriter;

public interface FragmentRenderer
{
    public void render(SwitchableWriter writer, ObjectStack objectStack)
        throws Exception;
}
