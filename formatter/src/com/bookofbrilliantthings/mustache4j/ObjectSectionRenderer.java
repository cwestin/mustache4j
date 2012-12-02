package com.bookofbrilliantthings.mustache4j;

import java.util.LinkedList;

import com.bookofbrilliantthings.mustache4j.util.SwitchableWriter;

public abstract class ObjectSectionRenderer
    extends StackingRenderer
{
    private final ListRenderer objectRenderer;
    private final boolean inverted;

    public ObjectSectionRenderer(final LinkedList<FragmentRenderer> fragmentList, final int objectDepth,
            final boolean inverted, final Class<?> valueClass, final Class<?> containerClass)
    {
        super(objectDepth);

        final PrimitiveType primitiveType = PrimitiveType.getSwitchType(valueClass);
        if (primitiveType != PrimitiveType.OBJECT)
            throw new IllegalArgumentException("section renderer can only operate on non-primitive types");

        objectRenderer = new ListRenderer(fragmentList, (inverted ? containerClass : valueClass));
        this.inverted = inverted;
    }

    public abstract Object getValue(Object o)
        throws Exception;

    @Override
    public void render(final SwitchableWriter writer, final ObjectStack objectStack)
            throws Exception
    {
        final Object value = getValue(objectStack.peekAt(objectDepth));

        if (!inverted)
        {
            if (value == null)
                return;

            objectStack.push(value);
            objectRenderer.render(writer, objectStack);
            objectStack.pop();
        }
        else
        {
            if (value == null)
                objectRenderer.render(writer, objectStack);
        }
    }
}
