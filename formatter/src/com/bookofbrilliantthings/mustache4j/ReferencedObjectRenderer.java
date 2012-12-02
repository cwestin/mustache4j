package com.bookofbrilliantthings.mustache4j;

import java.lang.reflect.Field;
import java.util.LinkedList;

public class ReferencedObjectRenderer
    extends ObjectSectionRenderer
{
    private final Field field;

    public ReferencedObjectRenderer(final LinkedList<FragmentRenderer> fragmentList, final int objectDepth,
            final boolean inverted, final Field field)
    {
        super(fragmentList, objectDepth, inverted, field.getType(), field.getDeclaringClass());
        this.field = field;
    }

    @Override
    public Object getValue(final Object o)
        throws Exception
    {
        return field.get(o);
    }
}
