package com.bookofbrilliantthings.mustache4j;

import java.io.Writer;
import java.util.HashMap;

public class HashMapRenderer
    implements FragmentRenderer
{

    public HashMapRenderer()
    {
    }

    @Override
    public void render(Writer writer, Object o)
        throws Exception
    {
        if (!HashMap.class.isAssignableFrom(o.getClass()))
            throw new IllegalArgumentException("the object must be a HashMap");

        final HashMap<String, ?> hashMap = (HashMap<String, ?>)o;

        for(FragmentRenderer renderer : fragmentList)
        {
            renderer.render(writer, x);
        }
        // TODO Auto-generated method stub

    }

}
