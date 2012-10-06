package com.bookofbrilliantthings.mustache4j;

import java.lang.reflect.Constructor;
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
    public Class<? extends FragmentRenderer> getObjectRendererClass()
    {
        return ReferencedObjectRenderer.class;
    }

    @Override
    public Class<? extends FragmentRenderer> getConditionalRendererClass()
    {
        return ConditionalFieldRenderer.class;
    }

    @Override
    public Class<? extends FragmentRenderer> getStringSectionRendererClass()
    {
        return StringFieldSectionRenderer.class;
    }

    @Override
    public <T extends FragmentRenderer> T createRenderer(
            Class<T> rendererClass, LinkedList<FragmentRenderer> fragmentList, boolean inverted)
        throws MustacheParserException
    {
        try
        {
            Constructor<T> ctor = rendererClass.getConstructor(LinkedList.class, boolean.class, Field.class);
            T fragmentRenderer = ctor.newInstance(fragmentList, inverted, field);
            return fragmentRenderer;
        }
        catch(NoSuchMethodException nsme)
        {
            throw new MustacheParserException(null, "class " + rendererClass.getName() +
                    " has no constructor with signature (LinkedList<FragmentRenderer>, boolean, Field)", nsme);
        }
        catch(Exception e)
        {
            throw new MustacheParserException(null, "could not instantiate renderer of class " +
                    rendererClass.getName(), e);
        }
    }
}
