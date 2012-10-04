package com.bookofbrilliantthings.mustache4j;

import java.io.Writer;
import java.lang.reflect.Method;

public class MethodReturnRenderer
    implements FragmentRenderer
{
    private final Method method;

    public MethodReturnRenderer(Method method)
    {
        final PrimitiveType primitiveType = PrimitiveType.getSwitchType(method.getReturnType());
        if (primitiveType == PrimitiveType.OBJECT)
            throw new RuntimeException("only primitive type and String returns are supported for now"); // TODO

        this.method = method;
    }

    @Override
    public void render(Writer writer, Object o)
        throws Exception
    {
        final Object resultObject = method.invoke(o, (Object [])null);
        if (resultObject == null)
            return;

        // TODO if this isn't a primitive type, it might be a nested object
        writer.write(resultObject.toString());
    }
}
