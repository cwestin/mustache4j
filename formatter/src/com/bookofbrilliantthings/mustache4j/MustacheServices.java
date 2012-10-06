package com.bookofbrilliantthings.mustache4j;

public class MustacheServices
{
    private MustacheLoader loader;

    private static class NoLoader
        implements MustacheLoader
    {
        @Override
        public MustacheRenderer load(String name)
        {
            throw new RuntimeException("no MustacheLoader implementation provided in MustacheServices");
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

    public void setLoader(MustacheLoader loader)
    {
        this.loader = loader;
    }
}
