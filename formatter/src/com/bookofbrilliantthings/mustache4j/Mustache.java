package com.bookofbrilliantthings.mustache4j;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedList;

import com.sun.xml.internal.bind.v2.schemagen.xmlschema.List;


public class Mustache
{
    private static class BaseHandler
        extends ParserHandler
    {
        protected LinkedList<FragmentRenderer> fragmentList;
        protected Locator locator;

        private static class Section
        {
            final RendererFactory rendererFactory;
            final LinkedList<FragmentRenderer> previousList;

            Section(RendererFactory rendererFactory, LinkedList<FragmentRenderer> previousList)
            {
                this.rendererFactory = rendererFactory;
                this.previousList = previousList;
            }
        }

        private final LinkedList<Section> sectionStack;

        BaseHandler()
        {
            fragmentList = new LinkedList<FragmentRenderer>();
            sectionStack = new LinkedList<Section>();
        }

        void push(final LinkedList<FragmentRenderer> newList, final RendererFactory rendererFactory)
        {
            final Section newSection = new Section(rendererFactory, fragmentList);
            sectionStack.addFirst(newSection);
            fragmentList = newList;
        }

        void pop()
        {
            final Section oldSection = sectionStack.peekFirst();
            if (oldSection == null)
                throw new IllegalStateException();

            // restore the previous fragment list and pop the stack
            fragmentList = oldSection.previousList;
            sectionStack.removeFirst();

            // add the created item to the fragment list
            fragmentList.add(oldSection.rendererFactory.create());
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
        private final HashMap<String, Method> methodNameMap;

        private static String getBeanName(final String methodName, final Class<?> forClass)
            throws MustacheParserException
        {
            if (!methodName.startsWith("get"))
                throw new MustacheParserException(null, "class '" + forClass.getName() +
                        "', MustacheValue method '" + methodName + "', name does not begin with 'get'");

            if (methodName.length() < 4)
                throw new MustacheParserException(null, "class '" + forClass.getName() +
                        "', MustacheValue method '" + methodName + "', name does not have anything after 'get'");

            final String beanName = Character.toLowerCase(methodName.charAt(3)) + methodName.substring(4);
            return beanName;
        }

        ObjectHandler(Class<?> forClass)
            throws MustacheParserException
        {
            super();
            fieldNameMap = new HashMap<String, Field>();
            methodNameMap = new HashMap<String, Method>();

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

            // index the methods
            final Method methods[] = forClass.getMethods(); // only returns public member methods
            for(Method method : methods)
            {
                final MustacheValue mustacheValue = method.getAnnotation(MustacheValue.class);
                if (mustacheValue == null)
                    continue;

                final String methodName = method.getName();
                final Class<?> paramTypes[] = method.getParameterTypes();
                if (paramTypes.length != 0)
                    throw new MustacheParserException(null, "class '" + forClass.getName() +
                            "', MustacheValue method '" + methodName +
                            "', method must not have any parameters");

                final String tagname = mustacheValue.tagname().isEmpty() ?
                        getBeanName(methodName, forClass) : mustacheValue.tagname();

                methodNameMap.put(tagname, method);
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
            // check for fields that this section name might refer to
            if (fieldNameMap.containsKey(secName))
            {
                final Field field = fieldNameMap.get(secName);
                final Class<?> fieldType = field.getType();
                final PrimitiveType pt = PrimitiveType.getSwitchType(fieldType);

                if (pt == PrimitiveType.BOOLEAN)
                {
                    final LinkedList<FragmentRenderer> newList = new LinkedList<FragmentRenderer>();
                    push(newList, ConditionalRenderer.createClosure(newList, inverted, field));
                    return;
                }

                if (pt == PrimitiveType.OBJECT)
                {
                    if (inverted)
                        throw new MustacheParserException(locator,
                            "can't create an inverted section for an Object, List<>, or Iterable<>");

                    // check for List<> (follow up with getGenericType())
                    if (List.class.isAssignableFrom(fieldType))
                    {
                        // TODO
                        throw new RuntimeException("unimplemented");
                    }

                    // check for Iterable<>
                    if (Iterable.class.isAssignableFrom(fieldType))
                    {
                        // TODO
                        throw new RuntimeException("unimplemented");
                    }

                    final LinkedList<FragmentRenderer> newList = new LinkedList<FragmentRenderer>();
                    push(newList, NestedRenderer.createClosure(newList, field)); // TODO need to push class maps
                    return;
                }

                throw new MustacheParserException(locator, "section '" + secName +
                        "': don't know what to do with field '" + field.getName() +
                        "' of type '" + fieldType.getName() + "'");
            }

            // check for methods this section name might refer to
            // TODO
            throw new RuntimeException("unimplemented");
        }

        @Override
        public void sectionEnd(String secName)
            throws MustacheParserException
        {
            // TODO need to execute the RendererFactory call here, as per Section.pop() above
            pop();
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
