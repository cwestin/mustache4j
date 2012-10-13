package com.bookofbrilliantthings.mustache4j.util;

import java.io.PrintStream;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;

public class Generic
{
    private static String indentString(int n)
    {
        final StringBuilder sb = new StringBuilder();
        for(int i = 0; i < n; ++i)
        {
            sb.append("  ");
        }

        return sb.toString();
    }

    public static void print(PrintStream ps, Type type)
    {
        print(ps, 0, type);
    }

    private static void print(PrintStream ps, int depth, Type type)
    {
        if (type instanceof GenericArrayType)
        {
            ps.println(indentString(depth) + "GenericArrayType");

            return;
        }

        if (type instanceof ParameterizedType)
        {
            ps.println(indentString(depth) + "ParameterizedType");
            final ParameterizedType parameterizedType = (ParameterizedType)type;
            final Type typeArgs[] = parameterizedType.getActualTypeArguments();
            for(Type typeArg : typeArgs)
                print(ps, depth + 1, typeArg);

            return;
        }

        if (type instanceof TypeVariable)
        {
            ps.println(indentString(depth) + "TypeVariable");

            return;
        }

        if (type instanceof WildcardType)
        {
            ps.println(indentString(depth) + "WildcardType");

            return;
        }

        if (type instanceof Class<?>)
        {
            final Class<?> concreteClass = (Class<?>)type;
            ps.println(indentString(depth) + "Class<" + concreteClass.getName() + ">");

            return;
        }

        // NOTREACHED
        throw new IllegalStateException();
    }
}
