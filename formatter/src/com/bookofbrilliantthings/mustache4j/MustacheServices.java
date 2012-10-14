package com.bookofbrilliantthings.mustache4j;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicReference;

import com.bookofbrilliantthings.mustache4j.util.Pair;

public class MustacheServices
{
    private final AtomicReference<HashMap<Pair<String, Class<?>>, MustacheEdition>> cacheRef;
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
        cacheRef = new AtomicReference<HashMap<Pair<String, Class<?>>, MustacheEdition>>(
                new HashMap<Pair<String, Class<?>>, MustacheEdition>());
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

    public MustacheEdition getEdition(String name, Class<?> forClass)
        throws MustacheParserException
    {
        final Pair<String, Class<?>> cacheKey = new Pair<String, Class<?>>(name, forClass);

        // keep trying this until we find a shared copy in the cache map
        while(true)
        {
            final HashMap<Pair<String, Class<?>>, MustacheEdition> cacheMap = cacheRef.get();
            MustacheEdition edition = cacheMap.get(cacheKey);
            if (edition != null)
            {
                if (!edition.newerAvailable())
                    return edition;
            }

            final MustacheLoader loader = getLoader();
            edition = loader.load(this, name, forClass);
            if (edition == null)
                throw new IllegalStateException("last edition reported newerAvailable(), but can't load");

            // clone the map
            @SuppressWarnings("unchecked")
            final HashMap<Pair<String, Class<?>>, MustacheEdition> cacheMapClone =
                    (HashMap<Pair<String, Class<?>>, MustacheEdition>)cacheMap.clone();

            // add the renderer to the cloned map
            cacheMapClone.put(cacheKey, edition);

            // try to replace the modified hashmap; return if we succeed
            if (cacheRef.compareAndSet(cacheMap, cacheMapClone))
                return edition;
        }
    }
}
