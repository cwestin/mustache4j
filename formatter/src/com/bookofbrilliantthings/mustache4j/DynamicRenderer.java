package com.bookofbrilliantthings.mustache4j;

import java.util.concurrent.atomic.AtomicReference;

import com.bookofbrilliantthings.mustache4j.util.SwitchableWriter;

public class DynamicRenderer
        extends MustacheRenderer
{
    private final String templateName;
    private final Class<?> forClass;
    private final MustacheServices services;
    private final AtomicReference<MustacheEdition> editionRef;

    public DynamicRenderer(MustacheServices services, String templateName, Class<?> forClass)
    {
        this.services = services;
        this.templateName = templateName;
        this.forClass = forClass;
        editionRef = new AtomicReference<MustacheEdition>();
    }

    @Override
    public void render(SwitchableWriter switchableWriter, ObjectStack objectStack)
            throws Exception
    {
        MustacheEdition edition = null;

        while(true)
        {
            edition = editionRef.get();

            if ((edition != null) && !edition.newerAvailable())
                break;

            // get the latest edition, and try to swap it in
            final MustacheLoader loader = services.getLoader();
            final MustacheEdition newEdition = loader.load(services, templateName, forClass);
            if (editionRef.compareAndSet(edition, newEdition))
            {
                edition = newEdition;
                break;
            }
        }

        final MustacheRenderer renderer = edition.getRenderer();
        renderer.render(switchableWriter, objectStack);
    }
}
