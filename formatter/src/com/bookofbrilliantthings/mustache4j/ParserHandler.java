package com.bookofbrilliantthings.mustache4j;

public class ParserHandler
{
    /**
     * A literal appears next. Called whenever a string literal has been completed.
     *
     * The default implementation is to do nothing.
     *
     * @param literal the value of the literal
     */
    public void literal(String literal)
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
    {
    }

    /**
     * Parsing is complete. Called once at the end.
     *
     * The default implementation is to do nothing.
     */
    public void done()
    {
    }
}
