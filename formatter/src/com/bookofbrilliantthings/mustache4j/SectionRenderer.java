package com.bookofbrilliantthings.mustache4j;

import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SectionRenderer
    implements FragmentRenderer
{
    private final List<FragmentRenderer> fragmentList;

    public SectionRenderer(final List<FragmentRenderer> list)
    {
        // we clone the list so we can be sure this is immutable
        fragmentList = new ArrayList<FragmentRenderer>(list);
    }

    @Override
    public void render(final Writer writer, final Object o)
        throws Exception
    {
        if (o == null)
            return;

        if (Iterable.class.isAssignableFrom(o.getClass()))
            render(writer, (Iterable<?>)o);
        else
        {
            for(FragmentRenderer renderer : fragmentList)
            {
                renderer.render(writer, o);
            }
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
