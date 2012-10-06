package com.bookofbrilliantthings.mustache4j;

import java.lang.reflect.Field;
import java.util.LinkedList;

public class ReferencedObjectRenderer
    extends ObjectSectionRenderer
{
    private final Field field;

    public ReferencedObjectRenderer(final LinkedList<FragmentRenderer> fragmentList,
            final boolean inverted, final Field field)
    {
        super(fragmentList, inverted, field.getType(), field.getDeclaringClass());
        this.field = field;
    }

    @Override
    public Object getValue(final Object o)
        throws Exception
    {
        return field.get(o);
    }

    private static class MyFactory
        extends Factory
    {
        private final Field field;

        MyFactory(LinkedList<FragmentRenderer> fragmentList, boolean inverted, Field field)
        {
            super(fragmentList, inverted);
            this.field = field;
        }

        @Override
        public FragmentRenderer createRenderer()
        {
            return new ReferencedObjectRenderer(fragmentList, inverted, field);
        }
    }

    public static RendererFactory createFactory(final LinkedList<FragmentRenderer> fragmentList,
            final boolean inverted, final Field field)
    {
        return new MyFactory(fragmentList, inverted, field);
    }
}
