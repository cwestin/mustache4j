package com.bookofbrilliantthings.mustache4j;

import java.io.Writer;
import java.util.HashMap;

public class HashMapValueRenderer
    implements FragmentRenderer
{
    private final String name;

    public HashMapValueRenderer(String name)
    {
        this.name = name;
    }

    @Override
    public void render(Writer writer, Object hm)
        throws Exception
    {
        // TODO find a way not to do dynamic casts on this for every element
        @SuppressWarnings("unchecked")
        final HashMap<String, ?> hashMap = (HashMap<String, ?>)hm;
        final Object o = hashMap.get(name);

        // if empty, nothing to do
        if (o == null)
            return;

        writer.write(o.toString());
    }
}
