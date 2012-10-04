package com.bookofbrilliantthings.mustache4j;

import java.util.LinkedList;

public abstract class ValueSource
{
    private final String name;

    protected ValueSource(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

    public abstract Class<?> getType();

    public abstract VariableRenderer createVariableRenderer();

    public abstract RendererFactory createConditionalRendererFactory(
            LinkedList<FragmentRenderer> fragmentList, boolean inverted);
    public abstract RendererFactory createObjectRendererFactory(
            LinkedList<FragmentRenderer> fragmentList, boolean inverted);
}
