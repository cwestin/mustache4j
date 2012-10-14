package com.bookofbrilliantthings.mustache4j;

public class MustacheParserException
    extends Exception
{
    private static final long serialVersionUID = 1L;

    public MustacheParserException(String message)
    {
        super(message);
    }

    public MustacheParserException(String message, Exception e)
    {
        super(message, e);
    }

    private static String locatorStr(final Locator locator)
    {
        if (locator == null)
            return "";

        return locator.getLineCount() + ":" + locator.getLinePos() + " ";
    }

    public MustacheParserException(final Locator locator, final String message)
    {
        /*
         * Somewhat stupidly, Java wouldn't allow me to build this up in a more readable fashion over
         * several lines using a StringBuilder. It required that super() be the first statement in
         * the constructor. Guh. The only real requirement should be that I call super() before I call any
         * methods on this.
         */
        super(locatorStr(locator) + message);
    }

    public MustacheParserException(final Locator locator, final String message, Exception e)
    {
        /*
         * Somewhat stupidly, Java wouldn't allow me to build this up in a more readable fashion over
         * several lines using a StringBuilder. It required that super() be the first statement in
         * the constructor. Guh. The only real requirement should be that I call super() before I call any
         * methods on this.
         */
        super(locatorStr(locator) + message, e);
    }
}
