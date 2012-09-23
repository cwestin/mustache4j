package com.bookofbrilliantthings.mustache4j;

import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MustacheRenderer
    implements FragmentRenderer
{
    private final List<FragmentRenderer> fragmentList;
    private final Class<?> forClass;

    public MustacheRenderer(List<FragmentRenderer> fragmentList, Class<?> forClass)
    {
        this.fragmentList = new ArrayList<FragmentRenderer>(fragmentList); // clone into an array
        this.forClass = forClass;
    }

    @Override
    public void render(Writer writer, Object o)
        throws Exception
    {
        // check the class argument once at the beginning
        if (forClass == null)
        {
            if (!HashMap.class.isAssignableFrom(o.getClass()))
                throw new IllegalArgumentException("object is not a HashMap");
        }
        else
        {
            if (!forClass.isAssignableFrom(o.getClass()))
                throw new IllegalArgumentException("object does not have the required class ('" +
                        forClass.getName() + "')");
        }

        for(FragmentRenderer renderer : fragmentList)
        {
            renderer.render(writer, o);
        }
    }
}
