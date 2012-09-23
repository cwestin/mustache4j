package com.bookofbrilliantthings.mustache4j;

import java.io.IOException;
import java.io.Reader;
import java.util.LinkedList;


public class Mustache
{
    public static MustacheRenderer compile(final Reader templateReader, final Class<?> forClass)
    {
        // TODO
        throw new RuntimeException("unimplemented");
    }

    private static class HashMapHandler
        extends ParserHandler
    {
        final LinkedList<FragmentRenderer> fragmentList;

        HashMapHandler()
        {
            fragmentList = new LinkedList<FragmentRenderer>();
        }

        @Override
        public void literal(String literal)
        {
            fragmentList.add(new StringRenderer(literal));
        }

        @Override
        public void variable(String varName)
        {
            fragmentList.add(new HashMapValueRenderer(varName));
        }

        @Override
        public void sectionBegin(String secName, boolean inverted)
        {
            throw new RuntimeException("unimplemented");
        }

        @Override
        public void sectionEnd(String secName)
        {
            throw new RuntimeException("unimplemented");
        }
    }

    public static MustacheRenderer compileForHashMap(final Reader templateReader)
        throws IOException, MustacheParserException
    {
        final HashMapHandler hashMapHandler = new HashMapHandler();
        TemplateParser.parse(hashMapHandler, templateReader);

        return new MustacheRenderer(hashMapHandler.fragmentList, null);
    }
}
