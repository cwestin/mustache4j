package com.bookofbrilliantthings.mustache4j;

import com.bookofbrilliantthings.mustache4j.util.SwitchableWriter;

public class DynamicRenderer
    extends MustacheRenderer
{
    private final String templateName;
    private final Class<?> forClass;
    private final MustacheServices mustacheServices;

    public DynamicRenderer(String templateName, Class<?> forClass, MustacheServices mustacheServices)
    {
        this.templateName = templateName;
        this.forClass = forClass;
        this.mustacheServices = mustacheServices;
    }

    @Override
    public void render(SwitchableWriter switchableWriter, Object o)
            throws Exception
    {
        // look up the template, if there's a newer version
        final MustacheLoader mustacheLoader = mustacheServices.getLoader();

        //
        // TODO Auto-generated method stub
    }
}
