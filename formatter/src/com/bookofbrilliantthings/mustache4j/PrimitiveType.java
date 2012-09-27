package com.bookofbrilliantthings.mustache4j;

enum PrimitiveType
{
    OBJECT,
    STRING,
    VOID,
    BOOLEAN,
    BYTE,
    CHAR,
    SHORT,
    INT,
    LONG,
    FLOAT,
    DOUBLE;

    public static PrimitiveType getSwitchType(final Class<?> theClass)
    {
        if (!theClass.isPrimitive())
        {
            if (theClass.equals(String.class))
                return STRING;

            return OBJECT;
        }

        if (theClass == void.class)
            return VOID;
        if (theClass == boolean.class)
            return BOOLEAN;
        if (theClass == byte.class)
            return BYTE;
        if (theClass == char.class)
            return CHAR;
        if (theClass == short.class)
            return SHORT;
        if (theClass == int.class)
            return INT;
        if (theClass == long.class)
            return LONG;
        if (theClass == float.class)
            return FLOAT;
        if (theClass == double.class)
            return DOUBLE;

        throw new IllegalArgumentException("class is not a primitive or an object");
    }
}
