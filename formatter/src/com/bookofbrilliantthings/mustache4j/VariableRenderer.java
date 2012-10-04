package com.bookofbrilliantthings.mustache4j;


public abstract class VariableRenderer
    implements FragmentRenderer
{
    protected VariableRenderer(Class<?> forClass)
    {
        // this constructor is really more for type checking than anything else
        final PrimitiveType primitiveType = PrimitiveType.getSwitchType(forClass);
        if (primitiveType == PrimitiveType.OBJECT)
            throw new RuntimeException("only primitive type and String variables are supported (" +
                    forClass.getName() + ")");
    }

    public abstract Object getValue(Object o)
        throws Exception;

    @Override
    public void render(final HtmlEscapeWriter writer, final Object o)
        throws Exception
    {
        final Object value = getValue(o);
        if (value != null)
            writer.write(value.toString());
    }
}
