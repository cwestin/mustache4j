package com.bookofbrilliantthings.mustache4j;

import java.io.Writer;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;


public abstract class SectionRenderer
    extends SectionRendererData
    implements FragmentRenderer
{
    protected SectionRenderer(final List<FragmentRenderer> list, final boolean inverted,
            final Field field, final Method method)
    {
        super(list, inverted, field, method);
    }

    public abstract boolean shouldRender(Object o)
        throws Exception;
    public abstract Object getObjectToRender(Object o)
        throws Exception;

    @Override
    public void render(final Writer writer, final Object o)
        throws Exception
    {
        if (!shouldRender(o))
            return;

        final Object objectToRender = getObjectToRender(o);
        for(FragmentRenderer renderer : fragmentList)
        {
            renderer.render(writer, objectToRender);
        }
    }

    public void render(final Writer writer, final Iterable<?> iterable)
        throws Exception
    {
        final Iterator<?> iterator = iterable.iterator();
        while(iterator.hasNext())
        {
            final Object o = iterator.next();
            render(writer, o);
        }
    }

}
