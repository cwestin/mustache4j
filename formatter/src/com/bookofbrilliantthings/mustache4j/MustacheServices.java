package com.bookofbrilliantthings.mustache4j;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicReference;

import com.bookofbrilliantthings.mustache4j.util.Pair;

public class MustacheServices
{
    private final AtomicReference<HashMap<Pair<String, Class<?>>, MustacheRenderer>> cacheRef;
    private final boolean dynamic;
    private MustacheLoader loader;

    private static class DefaultLoader
        implements MustacheLoader
    {
        @Override
        public MustacheEdition load(MustacheServices services, String name, Class<?> forClass)
        {
            throw new RuntimeException("can't find MustacheRenderer named \"" + name +
                    "\" for class " + forClass.getName());
        }
    }

    public MustacheServices(boolean dynamic)
    {
        cacheRef = new AtomicReference<HashMap<Pair<String, Class<?>>, MustacheRenderer>>(
                new HashMap<Pair<String, Class<?>>, MustacheRenderer>());
        this.dynamic = dynamic;
        loader = new DefaultLoader();
    }

    public boolean getDynamic()
    {
        return dynamic;
    }

    public MustacheLoader getLoader()
    {
        return loader;
    }

    public MustacheLoader setLoader(MustacheLoader newLoader)
    {
        final MustacheLoader oldLoader = loader;
        loader = newLoader;
        return oldLoader;
    }

    public MustacheRenderer getRenderer(String name, Class<?> forClass)
        throws MustacheParserException
    {
        final Pair<String, Class<?>> cacheKey = new Pair<String, Class<?>>(name, forClass);

        // keep trying this until we find a shared copy in the cache map
        MustacheRenderer loadedRenderer = null;
        while(true)
        {
            final HashMap<Pair<String, Class<?>>, MustacheRenderer> cacheMap = cacheRef.get();
            MustacheRenderer renderer = cacheMap.get(cacheKey);
            if (renderer != null)
                return renderer;

            // only load the template once
            if (loadedRenderer == null)
            {
                if (getDynamic())
                {
                    loadedRenderer = new DynamicRenderer(this, name, forClass);
                }
                else
                {
                    final MustacheLoader loader = getLoader();
                    final MustacheEdition edition = loader.load(this, name, forClass);
                    loadedRenderer = edition.getRenderer();
                }
            }

            // clone the map
            @SuppressWarnings("unchecked")
            final HashMap<Pair<String, Class<?>>, MustacheRenderer> cacheMapClone =
                    (HashMap<Pair<String, Class<?>>, MustacheRenderer>)cacheMap.clone();

            // add the renderer to the cloned map
            cacheMapClone.put(cacheKey, loadedRenderer);

            // try to replace the modified hashmap; return if we succeed
            if (cacheRef.compareAndSet(cacheMap, cacheMapClone))
                return loadedRenderer;
        }
    }
}
