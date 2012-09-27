package com.bookofbrilliantthings.mustache4j;

import java.io.Writer;
import java.util.List;

public class ObjectRenderer
    extends MustacheRenderer
{
    private final Class<?> forClass;

    public ObjectRenderer(List<FragmentRenderer> fragmentList, Class<?> forClass)
    {
        super(fragmentList);
        assert forClass != null;
        this.forClass = forClass;
    }

    @Override
    public void render(Writer writer, Object o)
        throws Exception
    {
        // check the class argument once at the beginning
        if (!forClass.isAssignableFrom(o.getClass()))
            throw new IllegalArgumentException("object does not have the required class ('" +
                    forClass.getName() + "')");

        super.render(writer, o);
    }

    private static class MyFactory
        implements RendererFactory
    {
        private final List<FragmentRenderer> fragmentList;
        private final Class<?> forClass;

        MyFactory(List<FragmentRenderer> fragmentList, Class<?> forClass)
        {
            this.fragmentList = fragmentList;
            this.forClass = forClass;
        }

        @Override
        public FragmentRenderer createRenderer()
        {
            return new ObjectRenderer(fragmentList, forClass);
        }
    }

    public static RendererFactory createFactory(List<FragmentRenderer> fragmentList, Class<?> forClass)
    {
        return new MyFactory(fragmentList, forClass);
    }
}
