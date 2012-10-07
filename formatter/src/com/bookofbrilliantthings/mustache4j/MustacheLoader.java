package com.bookofbrilliantthings.mustache4j;

public interface MustacheLoader
{
    public MustacheRenderer load(String name, Class<?> forClass)
        throws MustacheParserException;
}
