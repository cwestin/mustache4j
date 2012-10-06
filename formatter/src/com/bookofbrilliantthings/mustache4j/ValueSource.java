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

    public abstract FragmentRenderer createVariableRenderer();

    public abstract Class<? extends FragmentRenderer> getObjectRendererClass();
    public abstract Class<? extends FragmentRenderer> getConditionalRendererClass();
    public abstract Class<? extends FragmentRenderer> getStringSectionRendererClass();

    public abstract <T extends FragmentRenderer> T createRenderer(Class<T> rendererClass,
            LinkedList<FragmentRenderer> fragmentList, boolean inverted)
        throws MustacheParserException;
}
