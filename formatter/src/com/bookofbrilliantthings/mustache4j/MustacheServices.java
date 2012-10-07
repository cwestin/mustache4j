package com.bookofbrilliantthings.mustache4j;

public class MustacheServices
{
    private MustacheLoader loader;

    private static class NoLoader
        implements MustacheLoader
    {
        @Override
        public MustacheRenderer load(String name, Class<?> forClass)
        {
            throw new RuntimeException("can't find MustacheLoader named \"" + name +
                    "\" for class " + forClass.getName());
        }
    }

    public MustacheServices()
    {
        loader = new NoLoader();
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
