package com.bookofbrilliantthings.mustache4j;

import java.io.Writer;
import java.util.HashMap;
import java.util.List;

public class HashMapRenderer
    extends MustacheRenderer
{
    public HashMapRenderer(List<FragmentRenderer> fragmentList)
    {
        super(fragmentList);
    }

    @Override
    public void render(Writer writer, Object o)
        throws Exception
    {
        // check the class argument once at the beginning
        // todo check generic type parameters
        if (!HashMap.class.isAssignableFrom(o.getClass()))
            throw new IllegalArgumentException("object is not a HashMap<String, ?>");

        super.render(writer, o);
    }

    private static class MyFactory
        implements RendererFactory
    {
        private final List<FragmentRenderer> fragmentList;

        MyFactory(List<FragmentRenderer> fragmentList, Class<?> forClass)
        {
            this.fragmentList = fragmentList;
        }

        @Override
        public FragmentRenderer createRenderer()
        {
            return new HashMapRenderer(fragmentList);
        }
    }

    public static RendererFactory createFactory(List<FragmentRenderer> fragmentList, Class<?> forClass)
    {
        return new MyFactory(fragmentList, forClass);
    }
}
