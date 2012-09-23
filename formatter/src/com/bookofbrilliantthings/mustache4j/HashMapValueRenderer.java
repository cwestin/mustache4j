package com.bookofbrilliantthings.mustache4j;

import java.io.Writer;
import java.util.HashMap;

/**
 * Note: requires looking up the key twice; once via containsKey(), and another via get().
 *
 * @author cwestin
 *
 */
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
        final Object o = hashMap.containsKey(name) ? hashMap.get(name) : null;

        // if empty, nothing to do
        if (o == null)
            return;

        writer.write(o.toString());
    }
}
