package com.bookofbrilliantthings.mustache4j;

import java.lang.reflect.Field;
import java.util.LinkedList;

public class FieldSource
    implements ValueSource
{
    private final Field field;

    public FieldSource(Field field)
    {
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
}
