package com.bookofbrilliantthings.mustache4j;

import java.lang.reflect.Field;
import java.util.List;

public class ConditionalFieldRenderer
    extends ConditionalSectionRenderer
{
    private final Field field;

    public ConditionalFieldRenderer(final List<FragmentRenderer> fragmentList, final boolean inverted,
            final Field booleanField)
    {
        super(fragmentList, inverted, booleanField.getType(), booleanField.getDeclaringClass());
        this.field = booleanField;
    }

    @Override
    public boolean getCondition(final Object o)
        throws Exception
    {
        return ((Boolean)field.get(o)).booleanValue();
    }

    private static class MyFactory
        extends Factory
    {
        private final Field field;

        MyFactory(final List<FragmentRenderer> fragmentList, final boolean inverted, final Field field)
        {
            super(fragmentList, inverted);
            this.field = field;
        }

        @Override
        public FragmentRenderer createRenderer()
        {
            return new ConditionalFieldRenderer(fragmentList, inverted, field);
        }
    }

    public static RendererFactory createFactory(final List<FragmentRenderer> fragmentList,
            final boolean inverted, final Field booleanField)
    {
        return new MyFactory(fragmentList, inverted, booleanField);
    }
}
