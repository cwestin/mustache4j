package com.bookofbrilliantthings.mustache4j;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.LinkedList;

import com.bookofbrilliantthings.mustache4j.util.LocatorReader;


public class Mustache
{
    private static class BaseHandler
        extends ParserHandler
    {
        final LinkedList<FragmentRenderer> fragmentList;
        protected Locator locator;

        BaseHandler()
        {
            fragmentList = new LinkedList<FragmentRenderer>();
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
            fragmentList.add(new StringRenderer(literal));
        }
    }

    private static class ObjectHandler
        extends BaseHandler
    {
        private final HashMap<String, Field> fieldNameMap;
        private LocatorReader locatorReader;

        ObjectHandler(Class<?> forClass)
        {
            super();
            fieldNameMap = new HashMap<String, Field>();

            // analyze the class; we'll build up knowledge of all the fields and methods and their returns

            // index the fields
            final Field fields[] = forClass.getFields(); // only returns "accessible public fields"
            for(Field field : fields)
            {
                // check for the annotation, and if present, use the tagname
                final MustacheValue mustacheValue = field.getAnnotation(MustacheValue.class);
                if (mustacheValue == null)
                    continue;

                final String tagname = mustacheValue.tagname().isEmpty() ?
                        field.getName() : mustacheValue.tagname();

                fieldNameMap.put(tagname, field);
            }
        }

        @Override
        public void variable(String varName)
            throws MustacheParserException
        {
            // check for a field by this name
            if (fieldNameMap.containsKey(varName))
            {
                fragmentList.add(new FieldRenderer(fieldNameMap.get(varName)));
                return;
            }

            throw new MustacheParserException(locator,
                    "no MustacheValue named '" + varName + "' in object");
        }

        @Override
        public void sectionBegin(String secName, boolean inverted)
            throws MustacheParserException
        {

            throw new RuntimeException("unimplemented");
        }

        @Override
        public void sectionEnd(String secName)
            throws MustacheParserException
        {
            throw new RuntimeException("unimplemented");
        }
    }

    public static MustacheRenderer compile(final Reader templateReader, final Class<?> forClass)
        throws IOException, MustacheParserException
    {
        final ObjectHandler objectHandler = new ObjectHandler(forClass);
        Template.parse(objectHandler, templateReader);

        return new MustacheRenderer(objectHandler.fragmentList, forClass);
    }

    private static class HashMapHandler
        extends BaseHandler
    {
        HashMapHandler()
        {
            super();
        }

        @Override
        public void variable(String varName)
            throws MustacheParserException
        {
            fragmentList.add(new HashMapValueRenderer(varName));
        }

        @Override
        public void sectionBegin(String secName, boolean inverted)
            throws MustacheParserException
        {
            throw new RuntimeException("unimplemented");
        }

        @Override
        public void sectionEnd(String secName)
            throws MustacheParserException
        {
            throw new RuntimeException("unimplemented");
        }
    }

    public static MustacheRenderer compileForHashMap(final Reader templateReader)
        throws IOException, MustacheParserException
    {
        final HashMapHandler hashMapHandler = new HashMapHandler();
        Template.parse(hashMapHandler, templateReader);

        return new MustacheRenderer(hashMapHandler.fragmentList, null);
    }
}
