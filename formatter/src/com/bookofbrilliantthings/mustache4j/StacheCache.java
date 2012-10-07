package com.bookofbrilliantthings.mustache4j;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicReference;

public class StacheCache
{
    private final AtomicReference<HashMap<String, MustacheRenderer>> hashMapRef;

    public StacheCache()
    {
        HashMap<String, MustacheRenderer> hashMap = new HashMap<String, MustacheRenderer>();
        hashMapRef = new AtomicReference<HashMap<String, MustacheRenderer>>(hashMap);
    }

    public MustacheRenderer getRenderer(final MustacheServices mustacheServices, final String name)
        throws MustacheParserException
    {
        MustacheRenderer renderer = null;

        // keep trying this until we find a shared copy
        while(true)
        {
            final HashMap<String, MustacheRenderer> hashMap = hashMapRef.get();
            if (hashMap.containsKey(name))
                return hashMap.get(name);

            // load the renderer, once
            if (renderer == null)
            {
                final MustacheLoader loader = mustacheServices.getLoader();
                // TODO renderer = loader.load(name, forClass);
                throw new RuntimeException("unimplemented");
            }

            // clone the map
            @SuppressWarnings("unchecked")
            final HashMap<String, MustacheRenderer> hashMapClone =
                    (HashMap<String, MustacheRenderer>)hashMap.clone();

            // add the renderer to the cloned map
            hashMapClone.put(name, renderer);

            // try to replace the modified hashmap; return if we succeed
            if (hashMapRef.compareAndSet(hashMap, hashMapClone))
                return renderer;
        }
    }
}
