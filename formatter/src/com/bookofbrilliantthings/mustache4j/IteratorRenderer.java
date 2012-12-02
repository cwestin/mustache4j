package com.bookofbrilliantthings.mustache4j;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.LinkedList;

import com.bookofbrilliantthings.mustache4j.util.SwitchableWriter;

public abstract class IteratorRenderer
    extends StackingRenderer
{
    private final ListRenderer objectRenderer;

    public IteratorRenderer(LinkedList<FragmentRenderer> fragmentList, int objectDepth, boolean inverted, Type type)
    {
        super(objectDepth);

        assert !inverted; // not allowed for lists

        final ParameterizedType pType = (ParameterizedType)type;
        final Type argTypes[] = pType.getActualTypeArguments();

        /*
         * Management of generics is incomplete in the JDK, and this demonstrates a problem.
         *
         * We've already stipulated that the caller should only be calling this if the class satisifies
         * Iterable.class.isAssignableFrom(the value).  However, this could be true even if we have more
         * than one type parameter.  Consider class X<A, B> extends<Iterable<B>> {...}. This would be
         * assignable to Iterable<B>, but would have two type parameters. There's no easy way to obtain
         * the result of casting a variable of type X<A, B> to Iterable<B> in order to ask for the type
         * parameters of that. So here we assume the situation is not that complicated.  We could also
         * be wrong about the type parameter here is class X<B> extends Iterable<Y> {...}.
         */
        assert argTypes.length == 1;

        this.objectRenderer = new ListRenderer(fragmentList, (Class<?>)argTypes[0]);
    }

    public abstract Object getIterator(Object o)
            throws Exception;

    @Override
    public void render(SwitchableWriter writer, ObjectStack objectStack)
            throws Exception
    {
        final Object i = getIterator(objectStack.peekAt(objectDepth));
        if (i == null)
            return;

        final Iterator<?> iterator = (Iterator<?>)i;
        while(iterator.hasNext())
        {
            final Object io = iterator.next();
            objectStack.push(io);
            objectRenderer.render(writer, objectStack);
            objectStack.pop();
        }
    }
}
