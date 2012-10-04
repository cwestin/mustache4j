package com.bookofbrilliantthings.mustache4j;

import java.util.LinkedList;

public interface ValueSource
{
    public Class<?> getType();
    public VariableRenderer createVariableRenderer();
    public RendererFactory createConditionalRendererFactory(
            LinkedList<FragmentRenderer> fragmentList, boolean inverted);
}
