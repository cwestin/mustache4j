package com.bookofbrilliantthings.mustache4j;

import java.util.List;


public abstract class ObjectSectionRenderer
    implements FragmentRenderer
{
    private final ObjectRenderer objectRenderer;
    private final boolean inverted;

    public ObjectSectionRenderer(final List<FragmentRenderer> fragmentList,
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
    public void render(final HtmlEscapeWriter writer, final Object o)
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

    protected static abstract class Factory
        implements RendererFactory
    {
        protected final List<FragmentRenderer> fragmentList;
        protected final boolean inverted;

        Factory(List<FragmentRenderer> fragmentList, boolean inverted)
        {
            this.fragmentList = fragmentList;
            this.inverted = inverted;
        }

        public abstract FragmentRenderer createRenderer();
    }
}
