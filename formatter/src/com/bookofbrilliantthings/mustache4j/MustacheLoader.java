package com.bookofbrilliantthings.mustache4j;

public interface MustacheLoader
{
    public MustacheEdition load(MustacheServices services, String name, Class<?> forClass)
        throws MustacheParserException;
}
