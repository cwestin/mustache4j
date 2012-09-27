package com.bookofbrilliantthings.mustache4j;

public abstract class StackableParserHandler
    extends ParserHandler
{
    public abstract void resume(FragmentRenderer fragmentRenderer);
}
