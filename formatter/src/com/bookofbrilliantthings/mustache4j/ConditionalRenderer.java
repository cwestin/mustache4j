package com.bookofbrilliantthings.mustache4j;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;


public class ConditionalRenderer
    extends SectionRenderer
{
    private static class MyFactoryClosure
        extends FactoryClosure
    {
        MyFactoryClosure(final List<FragmentRenderer> fragmentList, final boolean inverted, final Field field)
        {
            super(fragmentList, inverted, field, null);
        }

        @Override
        public FragmentRenderer create()
        {
            return new ConditionalRenderer(fragmentList, inverted, field);
        }
    }

    public static RendererFactory createClosure(final List<FragmentRenderer> fragmentList,
            final boolean inverted, final Field booleanField)
    {
        return new MyFactoryClosure(fragmentList, inverted, booleanField);
    }

    public ConditionalRenderer(final List<FragmentRenderer> fragmentList, final boolean inverted,
            final Field booleanField)
    {
        super(new ArrayList<FragmentRenderer>(fragmentList), inverted, booleanField, null);
        assert booleanField.getType() == boolean.class;
    }

    @Override
    public boolean shouldRender(final Object o)
        throws Exception
    {
        final boolean b = field.getBoolean(o);
        return b ^ inverted;
    }

    @Override
    public Object getObjectToRender(final Object o)
        throws Exception
    {
        return o;
    }
}
