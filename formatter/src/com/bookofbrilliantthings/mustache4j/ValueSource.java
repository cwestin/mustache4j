package com.bookofbrilliantthings.mustache4j;

import java.lang.reflect.Type;
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
    public abstract Type getGenericType();

    public abstract FragmentRenderer createVariableRenderer(int objectDepth, boolean escaped);

    public abstract Class<? extends FragmentRenderer> getObjectRendererClass();
    public abstract Class<? extends FragmentRenderer> getConditionalRendererClass();
    public abstract Class<? extends FragmentRenderer> getStringSectionRendererClass();
    public abstract Class<? extends FragmentRenderer> getIterableRendererClass();
    public abstract Class<? extends FragmentRenderer> getIteratorRendererClass();

    public abstract <T extends FragmentRenderer> T createRenderer(Class<T> rendererClass,
            LinkedList<FragmentRenderer> fragmentList, int objectDepth, boolean inverted)
            throws MustacheParserException;
}
