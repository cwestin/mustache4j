package com.bookofbrilliantthings.mustache4j;

import java.io.Writer;
import java.lang.reflect.Field;
import java.util.List;

public class ChildObjectRenderer
    implements FragmentRenderer
{
    private final ObjectRenderer objectRenderer;
    private final Field field;

    public ChildObjectRenderer(final List<FragmentRenderer> fragmentList, final Field field)
    {
        objectRenderer = new ObjectRenderer(fragmentList, field.getType());
        this.field = field;
    }

    @Override
    public void render(final Writer writer, final Object o)
        throws Exception
    {
        final Object childObject = field.get(o);
        if (childObject == null)
            return;

        objectRenderer.render(writer, childObject);
    }

    private static class MyRendererFactory
        implements RendererFactory
    {
        private final List<FragmentRenderer> fragmentList;
        private final Field field;

        MyRendererFactory(List<FragmentRenderer> fragmentList, Field field)
        {
            this.fragmentList = fragmentList;
            this.field = field;
        }

        @Override
        public FragmentRenderer createRenderer()
        {
            return new ChildObjectRenderer(fragmentList, field);
        }
    }

    public static RendererFactory createFactory(final List<FragmentRenderer> fragmentList, final Field field)
    {
        return new MyRendererFactory(fragmentList, field);
    }
}
