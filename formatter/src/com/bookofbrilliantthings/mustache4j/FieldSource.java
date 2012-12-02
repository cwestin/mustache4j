package com.bookofbrilliantthings.mustache4j;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
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
    public Type getGenericType()
    {
        return field.getGenericType();
    }

    @Override
    public VariableRenderer createVariableRenderer(int objectDepth, boolean escaped)
    {
        return new FieldRenderer(objectDepth, escaped, field);
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
            Class<T> rendererClass, LinkedList<FragmentRenderer> fragmentList, int objectDepth, boolean inverted)
        throws MustacheParserException
    {
        try
        {
            Constructor<T> ctor = rendererClass.getConstructor(LinkedList.class, int.class, boolean.class, Field.class);
            T fragmentRenderer = ctor.newInstance(fragmentList, objectDepth, inverted, field);
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

    @Override
    public Class<? extends FragmentRenderer> getIterableRendererClass()
    {
        return IterableFieldRenderer.class;
    }
}
