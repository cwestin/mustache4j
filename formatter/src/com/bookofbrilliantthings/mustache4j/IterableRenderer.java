package com.bookofbrilliantthings.mustache4j;

import java.lang.reflect.Type;
import java.util.LinkedList;

public abstract class IterableRenderer
    extends IteratorRenderer
{
    public IterableRenderer(LinkedList<FragmentRenderer> fragmentList, int objectDepth, boolean inverted, Type type)
    {
        super(fragmentList, objectDepth, inverted, type);
    }

    public abstract Object getIterable(Object o)
            throws Exception;

    @Override
    public Object getIterator(Object o)
            throws Exception
    {
        final Object i = getIterable(o);
        if (i == null)
            return null;

        final Iterable<?> iterable = (Iterable<?>)i;
        return iterable.iterator();
    }
}
