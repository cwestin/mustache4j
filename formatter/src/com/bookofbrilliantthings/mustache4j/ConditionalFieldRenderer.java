package com.bookofbrilliantthings.mustache4j;

import java.lang.reflect.Field;
import java.util.LinkedList;

public class ConditionalFieldRenderer
    extends ConditionalSectionRenderer
{
    private final Field field;

    public ConditionalFieldRenderer(final LinkedList<FragmentRenderer> fragmentList,
            final int objectDepth, final boolean inverted, final Field booleanField)
    {
        super(fragmentList, objectDepth, inverted, booleanField.getType(), booleanField.getDeclaringClass());
        this.field = booleanField;
    }

    @Override
    public boolean getCondition(final Object o)
        throws Exception
    {
        return ((Boolean)field.get(o)).booleanValue();
    }
}
