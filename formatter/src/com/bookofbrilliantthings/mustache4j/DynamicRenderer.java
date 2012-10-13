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
    public void render(SwitchableWriter switchableWriter, Object o)
            throws Exception
    {
        MustacheEdition edition = editionRef.get();

        // look up the template, if there's a newer version
        while(true)
        {
            if ((edition == null) || edition.newerAvailable())
            {
                final MustacheLoader loader = services.getLoader();
                final MustacheEdition newEdition = loader.load(services, templateName, forClass);
                if (editionRef.compareAndSet(edition, newEdition))
                {
                    edition = newEdition;
                    break;
                }
            }
        }

        final MustacheRenderer renderer = edition.getRenderer();
        renderer.render(switchableWriter, o);
    }
}
