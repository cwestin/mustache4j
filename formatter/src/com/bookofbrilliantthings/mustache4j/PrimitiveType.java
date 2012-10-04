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

            // since we use wrapped types in evaluation, check for those, and treat as primitive
            if (theClass.equals(Boolean.class))
                return BOOLEAN;
            if (theClass.equals(Byte.class))
                return BYTE;
            if (theClass.equals(Character.class))
                return CHAR;
            if (theClass.equals(Short.class))
                return SHORT;
            if (theClass.equals(Integer.class))
                return INT;
            if (theClass.equals(Long.class))
                return LONG;
            if (theClass.equals(Float.class))
                return FLOAT;
            if (theClass.equals(Double.class))
                return DOUBLE;

            return OBJECT;
        }

        if (theClass.equals(void.class))
            return VOID;
        if (theClass.equals(boolean.class))
            return BOOLEAN;
        if (theClass.equals(byte.class))
            return BYTE;
        if (theClass.equals(char.class))
            return CHAR;
        if (theClass.equals(short.class))
            return SHORT;
        if (theClass.equals(int.class))
            return INT;
        if (theClass.equals(long.class))
            return LONG;
        if (theClass.equals(float.class))
            return FLOAT;
        if (theClass.equals(double.class))
            return DOUBLE;

        throw new IllegalArgumentException("class is not a primitive or an object");
    }
}
