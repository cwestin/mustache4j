package com.bookofbrilliantthings.mustache4j;

import com.bookofbrilliantthings.mustache4j.util.SwitchableWriter;

public abstract class StackingRenderer
    implements FragmentRenderer
{
    protected final int objectDepth;

    protected StackingRenderer(int objectDepth)
    {
        assert objectDepth >= 0;
        this.objectDepth = objectDepth;
    }

    public abstract void render(SwitchableWriter writer, ObjectStack objectStack)
            throws Exception;
}
