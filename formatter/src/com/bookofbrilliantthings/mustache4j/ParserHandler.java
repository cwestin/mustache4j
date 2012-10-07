package com.bookofbrilliantthings.mustache4j;

public class ParserHandler
{
    /**
     * Set an optional locator. This can be useful for generating nicer error messages
     *
     * @param locator the locator
     */
    public void setLocator(Locator locator)
    {
    }

    /**
     * A literal appears next. Called whenever a string literal has been completed.
     *
     * The default implementation is to do nothing.
     *
     * @param literal the value of the literal
     */
    public void literal(String literal)
        throws MustacheParserException
    {
    }

    /**
     * A variable appears next. Called whenever a variable tag has been completed.
     *
     * The default implementation is to do nothing.
     *
     * @param varName the name of the variable
     */
    public void variable(String varName)
        throws MustacheParserException
    {
    }

    /**
     * A section begins.
     *
     * The default implementation is to do nothing.
     *
     * @param secName the name of the section
     */
    public void sectionBegin(String secName, boolean inverted)
        throws MustacheParserException
    {
    }

    /**
     * The given section has been closed.
     *
     * The default implementation is to do nothing.
     *
     * @param secName the name of the section
     */
    public void sectionEnd(String secName)
        throws MustacheParserException
    {
    }

    public void partial(String partialName)
        throws MustacheParserException
    {
    }

    public void unescaped(String varName)
        throws MustacheParserException
    {
    }

    /**
     * A comment appeared in the template.
     *
     * The default implementation is to do nothing.
     *
     * @param comment the comment text
     */
    public void comment(String comment)
        throws MustacheParserException
    {
    }

    /**
     * Parsing is complete. Called once at the end.
     *
     * The default implementation is to do nothing.
     */
    public void done()
        throws MustacheParserException
    {
    }
}
