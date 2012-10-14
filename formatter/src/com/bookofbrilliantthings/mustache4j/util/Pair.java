package com.bookofbrilliantthings.mustache4j.util;

/**
 * Unfortunately, this still isn't part of the SDK, and I don't want to require a whole other library
 * such as Apache Commons just to get it.
 *
 * @author cwestin
 *
 */
public class Pair<F, S>
{
    private final F first;
    private final S second;

    public Pair(F first, S second)
    {
        this.first = first;
        this.second = second;
    }

    public F getFirst()
    {
        return first;
    }

    public S getSecond()
    {
        return second;
    }

    @Override
    public Object clone()
    {
        return new Pair<F, S>(first, second);
    }

    @Override
    public boolean equals(Object o)
    {
        if (o == null)
            return false;

        if (!(o instanceof Pair))
            return false;

        final Pair<?, ?> other = (Pair<?,?>)o;
        if (first == null)
        {
            if (other.first != null)
                return false;
        }
        else
        {
            if (other.first == null)
                return false;

            if (!first.equals(other.first))
                return false;
        }

        if (second == null)
        {
            if (other.second != null)
                return false;
        }
        else
        {
            if (other.second == null)
                return false;

            if (!second.equals(other.second))
                return false;
        }

        return true;
    }

    @Override
    public int hashCode()
    {
        int v = -1;

        if (first != null)
            v -= first.hashCode();

        if (second == null)
            v -= 17;
        else
            v -= second.hashCode();

        return v;
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder();
        sb.append("Pair<");

        if (first == null)
            sb.append("null");
        else
            sb.append(first.toString());

        sb.append(',');

        if (second == null)
            sb.append("null");
        else
            sb.append(second.toString());

        sb.append('>');

        return sb.toString();
    }
}
