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
}
