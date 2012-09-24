package com.bookofbrilliantthings.mustache4j;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class NestedRenderer
    extends SectionRenderer
{
    private static class MyFactoryClosure
        extends FactoryClosure
    {
        MyFactoryClosure(final List<FragmentRenderer> fragmentList, final Field field)
        {
            super(fragmentList, false, field, null);
        }

        @Override
        public FragmentRenderer create()
        {
            return new NestedRenderer(fragmentList, field);
        }
    }

    public static RendererFactory createClosure(final List<FragmentRenderer> fragmentList, final Field field)
    {
        return new MyFactoryClosure(fragmentList, field);
    }

    public NestedRenderer(final List<FragmentRenderer> fragmentList, final Field field)
    {
        super(new ArrayList<FragmentRenderer>(fragmentList), false, field, null);
        assert PrimitiveType.getSwitchType(field.getType()) == PrimitiveType.OBJECT;
    }

    @Override
    public boolean shouldRender(Object o)
        throws Exception
    {
        final Object childObject = field.get(o);
        return (childObject != null);
    }

    @Override
    public Object getObjectToRender(Object o)
        throws Exception
    {
        final Object childObject = field.get(o);
        return childObject;
    }
}
