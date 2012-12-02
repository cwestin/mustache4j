package com.bookofbrilliantthings.mustache4j;

import java.util.ArrayList;

public class ObjectStack
{
    private final ArrayList<Object> objectStack;

    public ObjectStack()
    {
        objectStack = new ArrayList<Object>();
    }

    public void push(Object o)
    {
        assert o != null;
        objectStack.add(o);
    }

    public void repush()
    {
        push(peekAt(0));
    }

    public void pop()
    {
        final int size = objectStack.size();
        if (size == 0)
            throw new IllegalStateException();
        objectStack.remove(size - 1);
    }

    public Object peekAt(int n)
    {
        assert n >= 0;

        final int size = objectStack.size();
        assert n < objectStack.size();
        return objectStack.get((size - 1) - n);
    }
}
