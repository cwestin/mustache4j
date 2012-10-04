package com.bookofbrilliantthings.mustache4j;

import java.lang.reflect.Field;
import java.util.LinkedList;

public class FieldSource
    extends ValueSource
{
    private final Field field;

    public FieldSource(String name, Field field)
    {
        super(name);
        this.field = field;
    }

    @Override
    public Class<?> getType()
    {
        return field.getType();
    }

    @Override
    public VariableRenderer createVariableRenderer()
    {
        return new FieldRenderer(field);
    }

    @Override
    public RendererFactory createConditionalRendererFactory(
            LinkedList<FragmentRenderer> fragmentList, boolean inverted)
    {
        return ConditionalFieldRenderer.createFactory(fragmentList, inverted, field);
    }

    @Override
    public RendererFactory createObjectRendererFactory(
            LinkedList<FragmentRenderer> fragmentList, boolean inverted)
    {
        return ReferencedObjectRenderer.createFactory(fragmentList, inverted, field);
    }

    @Override
    public RendererFactory createStringSectionRendererFactory(
            LinkedList<FragmentRenderer> fragmentList, boolean inverted)
    {
        return StringFieldSectionRenderer.createFactory(fragmentList, inverted, field);
    }
}
