package com.bookofbrilliantthings.mustache4j;

import java.lang.reflect.Field;
import java.util.LinkedList;

public class StringFieldSectionRenderer
    extends StringSectionRenderer
{
    private final Field field;

    public StringFieldSectionRenderer(LinkedList<FragmentRenderer> fragmentList, boolean inverted, Field field)
    {
        super(fragmentList, inverted, field.getDeclaringClass());
        this.field = field;
    }

    @Override
    public String getString(Object o)
        throws Exception
    {
        final Object s = field.get(o);
        if (s != null)
            return (String)s;

        return null;
    }

    private static class MyFactory
        extends Factory
    {
        private final Field field;

        MyFactory(final LinkedList<FragmentRenderer> fragmentList, final boolean inverted, final Field field)
        {
            super(fragmentList, inverted);
            this.field = field;
        }

        @Override
        public FragmentRenderer createRenderer()
        {
            return new StringFieldSectionRenderer(fragmentList, inverted, field);
        }
    }

    public static RendererFactory createFactory(final LinkedList<FragmentRenderer> fragmentList,
            final boolean inverted, final Field field)
    {
        return new MyFactory(fragmentList, inverted, field);
    }
}
