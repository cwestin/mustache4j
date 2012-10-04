package com.bookofbrilliantthings.mustache4j;

import java.io.Writer;
import java.lang.reflect.Field;
import java.util.List;

public class ReferencedObjectRenderer
    implements FragmentRenderer
{
    private final ObjectRenderer objectRenderer;
    private final boolean inverted;
    private final Field field;

    public ReferencedObjectRenderer(final List<FragmentRenderer> fragmentList,
            final boolean inverted, final Field field)
    {
        objectRenderer = new ObjectRenderer(fragmentList,
                (inverted ? field.getDeclaringClass() : field.getType()));
        this.inverted = inverted;
        this.field = field;
    }

    @Override
    public void render(final Writer writer, final Object o)
        throws Exception
    {
        final Object childObject = field.get(o);

        if (!inverted)
        {
            if (childObject == null)
                return;

            objectRenderer.render(writer, childObject);
        }
        else
        {
            if (childObject == null)
                objectRenderer.render(writer, o);
        }
    }

    private static class MyRendererFactory
        implements RendererFactory
    {
        private final List<FragmentRenderer> fragmentList;
        private final boolean inverted;
        private final Field field;

        MyRendererFactory(List<FragmentRenderer> fragmentList, boolean inverted, Field field)
        {
            this.fragmentList = fragmentList;
            this.inverted = inverted;
            this.field = field;
        }

        @Override
        public FragmentRenderer createRenderer()
        {
            return new ReferencedObjectRenderer(fragmentList, inverted, field);
        }
    }

    public static RendererFactory createFactory(final List<FragmentRenderer> fragmentList,
            final boolean inverted, final Field field)
    {
        return new MyRendererFactory(fragmentList, inverted, field);
    }
}
