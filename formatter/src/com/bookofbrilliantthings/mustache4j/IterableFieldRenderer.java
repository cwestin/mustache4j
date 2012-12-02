package com.bookofbrilliantthings.mustache4j;

import java.lang.reflect.Field;
import java.util.LinkedList;

public class IterableFieldRenderer
    extends IterableRenderer
{
    private final Field field;

    public IterableFieldRenderer(LinkedList<FragmentRenderer> fragmentList, int objectDepth,
            boolean inverted, Field field)
    {
        super(fragmentList, objectDepth, inverted, field.getGenericType());
        this.field = field;
    }

    @Override
    public Object getIterable(Object o)
            throws Exception
    {
        return field.get(o);
    }
}
