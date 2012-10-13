package com.bookofbrilliantthings.mustache4j;

public class MustacheServices
{
    private MustacheLoader loader;
    private final boolean dynamic;

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
}
