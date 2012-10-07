package com.bookofbrilliantthings.mustache4j;

import java.util.LinkedList;

public class StackingParserHandler
    extends ParserHandler
{
    private StackableParserHandler topHandler;
    private final LinkedList<StackableParserHandler> handlerStack;
    private Locator locator;

    public StackingParserHandler()
    {
        handlerStack = new LinkedList<StackableParserHandler>();
    }

    public void push(StackableParserHandler parserHandler)
    {
        handlerStack.addFirst(topHandler);
        topHandler = parserHandler;

        if (locator != null)
            topHandler.setLocator(locator);
    }

    public void pop(final FragmentRenderer fragmentRenderer)
    {
        if (handlerStack.size() == 1)
            throw new IllegalStateException();

        topHandler = handlerStack.removeFirst();
        topHandler.resume(fragmentRenderer);
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
    public void partial(String partialName)
        throws MustacheParserException
    {
        topHandler.partial(partialName);
    }

    @Override
    public void unescaped(String varName)
        throws MustacheParserException
    {
        topHandler.unescaped(varName);
    }

    @Override
    public void comment(String comment)
        throws MustacheParserException
    {
        topHandler.comment(comment);
    }

    @Override
    public void done()
        throws MustacheParserException
    {
        topHandler.done();
    }
}
