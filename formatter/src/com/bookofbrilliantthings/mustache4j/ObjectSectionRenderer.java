package com.bookofbrilliantthings.mustache4j;

import java.util.LinkedList;

import com.bookofbrilliantthings.mustache4j.util.SwitchableWriter;

public abstract class ObjectSectionRenderer
    implements FragmentRenderer
{
    private final ObjectRenderer objectRenderer;
    private final boolean inverted;

    public ObjectSectionRenderer(final LinkedList<FragmentRenderer> fragmentList,
            final boolean inverted, final Class<?> valueClass, final Class<?> containerClass)
    {
        final PrimitiveType primitiveType = PrimitiveType.getSwitchType(valueClass);
        if (primitiveType != PrimitiveType.OBJECT)
            throw new IllegalArgumentException("section renderer can only operate on non-primitive types");

        objectRenderer = new ObjectRenderer(fragmentList, (inverted ? containerClass : valueClass));
        this.inverted = inverted;
    }

    public abstract Object getValue(Object o)
        throws Exception;

    @Override
    public void render(final SwitchableWriter writer, final Object o)
        throws Exception
    {
        final Object value = getValue(o);

        if (!inverted)
        {
            if (value == null)
                return;

            objectRenderer.render(writer, value);
        }
        else
        {
            if (value == null)
                objectRenderer.render(writer, o);
        }
    }
}
