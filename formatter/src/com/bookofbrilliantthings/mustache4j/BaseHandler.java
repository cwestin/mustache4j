package com.bookofbrilliantthings.mustache4j;

import java.util.LinkedList;

public class BaseHandler
    extends StackableParserHandler
{
    final protected LinkedList<FragmentRenderer> fragmentList;
    final protected boolean inverted;
    final protected ValueSource valueSource;
    final protected Class<? extends FragmentRenderer> rendererClass;
    final protected StackingParserHandler stackingParserHandler;
    final protected MustacheServices mustacheServices;
    protected Locator locator;

    protected BaseHandler(LinkedList<FragmentRenderer> fragmentList, boolean inverted,
            ValueSource valueSource, Class<? extends FragmentRenderer> rendererClass,
            StackingParserHandler stackingParserHandler, MustacheServices mustacheServices)
    {
        this.fragmentList = fragmentList;
        this.inverted = inverted;
        this.valueSource = valueSource;
        this.rendererClass = rendererClass;
        this.stackingParserHandler = stackingParserHandler;
        this.mustacheServices = mustacheServices;
    }

    @Override
    public void resume(FragmentRenderer fragmentRenderer)
    {
        fragmentList.add(fragmentRenderer);
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
        fragmentList.add(new LiteralRenderer(literal));
    }
}
