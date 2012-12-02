package com.bookofbrilliantthings.mustache4j;

import java.io.Reader;
import java.util.LinkedList;

public class Mustache
{
    public static MustacheRenderer compile(final MustacheServices mustacheServices,
            final Reader templateReader, final Class<?> forClass)
        throws MustacheParserException
    {
        final LinkedList<FragmentRenderer> fragmentList = new LinkedList<FragmentRenderer>();
        final StackingParserHandler stackingParserHandler = new StackingParserHandler();
        final ObjectHandler objectHandler =
                new ObjectHandler(fragmentList, false, null, null, stackingParserHandler,
                        mustacheServices, forClass);
        stackingParserHandler.push(objectHandler);

        Template.parse(stackingParserHandler, templateReader);

        return new StaticRenderer(fragmentList, forClass);
    }
}
