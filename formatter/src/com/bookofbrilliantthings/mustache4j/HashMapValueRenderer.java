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

        // if it's a boxed primitive, write o.toString()
        // if it's an object
        // -- check for List<T>
        // -- check for Iterable<T>
        // -- check for HashMap<String, ?>
        // -- create a renderer for the object -- but need a new template too

        // if the object is a boxed primitive
        writer.write(o.toString());
    }
}
