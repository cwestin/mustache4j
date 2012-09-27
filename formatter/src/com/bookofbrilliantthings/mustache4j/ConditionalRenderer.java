package com.bookofbrilliantthings.mustache4j;

import java.io.Writer;
import java.lang.reflect.Field;
import java.util.List;

public class ConditionalRenderer
    implements FragmentRenderer
{
    private final ObjectRenderer objectRenderer;
    private final boolean inverted;
    private final Field field;

    public ConditionalRenderer(final List<FragmentRenderer> fragmentList, final boolean inverted,
            final Field booleanField)
    {
        assert booleanField.getType() == boolean.class;
        objectRenderer = new ObjectRenderer(fragmentList, booleanField.getDeclaringClass());
        this.inverted = inverted;
        this.field = booleanField;
    }

    @Override
    public void render(final Writer writer, final Object o)
        throws Exception
    {
        final boolean b = field.getBoolean(o) ^ inverted;
        if (!b)
            return;

        objectRenderer.render(writer, o);
    }

    private static class MyFactory
        implements RendererFactory
    {
        private final List<FragmentRenderer> fragmentList;
        private final boolean inverted;
        private final Field field;

        MyFactory(final List<FragmentRenderer> fragmentList, final boolean inverted, final Field field)
        {
            this.fragmentList = fragmentList;
            this.inverted = inverted;
            this.field = field;
        }

        @Override
        public FragmentRenderer createRenderer()
        {
            return new ConditionalRenderer(fragmentList, inverted, field);
        }
    }

    public static RendererFactory createFactory(final List<FragmentRenderer> fragmentList,
            final boolean inverted, final Field booleanField)
    {
        return new MyFactory(fragmentList, inverted, booleanField);
    }
}
