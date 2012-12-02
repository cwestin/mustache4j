package com.bookofbrilliantthings.mustache4j;

import java.util.LinkedList;

public abstract class BaseHandler
    extends StackableParserHandler
{
    final private LinkedList<FragmentRenderer> fragmentList;
    final private boolean inverted;
    final private ValueSource valueSource;
    final private Class<? extends FragmentRenderer> rendererClass;
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

    protected ValueReference findValue(String valueName)
            throws MustacheParserException
    {
        final ValueReference valueReference = stackingParserHandler.findValue(valueName);
        if (valueReference == null)
            throw new MustacheParserException(locator,
                    "no MustacheValue named '" + valueName + "' found in current object stack");

        return valueReference;
    }

    protected void addFragment(FragmentRenderer fragmentRenderer)
    {
        fragmentList.add(fragmentRenderer);
    }

    protected void pop()
            throws MustacheParserException
    {
        stackingParserHandler.pop(valueSource.createRenderer(rendererClass, fragmentList, inverted));
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
