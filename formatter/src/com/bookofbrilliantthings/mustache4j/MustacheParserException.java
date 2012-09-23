package com.bookofbrilliantthings.mustache4j;

import com.bookofbrilliantthings.mustache4j.util.LocatorReader;

public class MustacheParserException
    extends Exception
{
    private static final long serialVersionUID = 1L;

    MustacheParserException(final LocatorReader locatorReader, final String message)
    {
        super("" + locatorReader.getLineCount() + ":" + locatorReader.getLinePos() + " " + message);
    }
}
