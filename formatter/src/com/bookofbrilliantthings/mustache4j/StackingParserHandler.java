package com.bookofbrilliantthings.mustache4j;

import java.util.LinkedList;

public class StackingParserHandler
    extends ParserHandler
{
    private ParserHandler topHandler;
    private final LinkedList<ParserHandler> handlerStack;
    private Locator locator;

    public StackingParserHandler(ParserHandler firstHandler)
    {
        topHandler = firstHandler;
        handlerStack = new LinkedList<ParserHandler>();
    }

    public void push(ParserHandler parserHandler)
    {
        handlerStack.addFirst(topHandler);
        topHandler = parserHandler;

        if (locator != null)
            topHandler.setLocator(locator);
    }

    public void pop()
    {
        if (handlerStack.size() == 0)
            throw new IllegalStateException();

        topHandler = handlerStack.removeFirst();
    }

    @Override
    public void setLocator(Locator locator)
    {
        this.locator = locator;
    }

    @Override
    public void literal(String literal)
        throws MustacheParserException
    {
        topHandler.literal(literal);
    }

    @Override
    public void variable(String varName)
        throws MustacheParserException
    {
        topHandler.variable(varName);
    }

    @Override
    public void sectionBegin(String secName, boolean inverted)
        throws MustacheParserException
    {
        topHandler.sectionBegin(secName, inverted);
    }

    @Override
    public void sectionEnd(String secName)
        throws MustacheParserException
    {
        topHandler.sectionEnd(secName);
    }

    @Override
    public void comment(String comment)
        throws MustacheParserException
    {
        topHandler.comment(comment);
    }
}
