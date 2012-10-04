package com.bookofbrilliantthings.mustache4j;

import java.io.Writer;
import java.lang.reflect.Field;

public class FieldRenderer
    implements FragmentRenderer
{
    private final Field field;

    public FieldRenderer(final Field field)
    {
        final Class<?> theClass = field.getType();
        final PrimitiveType primitiveType = PrimitiveType.getSwitchType(theClass);
        if (primitiveType == PrimitiveType.OBJECT)
            throw new RuntimeException("only primitive type and String fields are supported for now (" + theClass.getName() + ")"); // TODO

        this.field = field;
    }

    @Override
    public void render(final Writer writer, final Object o)
        throws Exception
    {
        final Object v = field.get(o);
        writer.write(v.toString());
    }
}
