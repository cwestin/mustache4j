package com.bookofbrilliantthings.mustache4j;

import java.util.HashMap;
import java.util.List;

public class HashMapRenderer
    extends BasicRenderer
{
    public HashMapRenderer(List<FragmentRenderer> fragmentList)
    {
        super(fragmentList);
    }

    @Override
    public void render(HtmlEscapeWriter writer, Object o)
        throws Exception
    {
        // check the class argument once at the beginning
        // todo check generic type parameters
        if (!HashMap.class.isAssignableFrom(o.getClass()))
            throw new IllegalArgumentException("object is not a HashMap<String, ?>");

        super.render(writer, o);
    }
}
