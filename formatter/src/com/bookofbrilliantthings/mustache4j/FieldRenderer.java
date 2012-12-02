package com.bookofbrilliantthings.mustache4j;

import java.lang.reflect.Field;

public class FieldRenderer
    extends VariableRenderer
{
    private final Field field;

    public FieldRenderer(int objectDepth, boolean escaped, final Field field)
    {
        super(objectDepth, escaped, field.getType());
        this.field = field;
    }

    @Override
    public Object getValue(final Object o)
        throws Exception
    {
        return field.get(o);
    }
}
