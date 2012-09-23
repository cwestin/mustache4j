package com.bookofbrilliantthings.mustache4j.util;

import java.io.IOException;
import java.io.Writer;

public class RecentCharBuffer
    extends Writer
{
    private final StringBuilder stringBuilder; // we'll use this as the buffer
    private final int maxLength;

    public RecentCharBuffer(int n)
    {
        stringBuilder = new StringBuilder(n);
        maxLength = n;
    }

    @Override
    public String toString()
    {
        return stringBuilder.toString();
    }

    private boolean isClosed;

    @Override
    public void close()
        throws IOException
    {
        if (!isClosed)
        {
            flush();
            isClosed = true;
        }
    }

    private final static String ALREADY_CLOSED = "stream is already closed";

    @Override
    public void flush()
        throws IOException
    {
        if (isClosed)
            throw new IOException(ALREADY_CLOSED);
    }

    @Override
    public void write(char buf[])
        throws IOException
    {
        if (isClosed)
            throw new IOException(ALREADY_CLOSED);

        stringBuilder.append(buf);
        downsize();
    }

    @Override
    public void write(char buf[], int offset, int length)
        throws IOException
    {
        if (isClosed)
            throw new IOException(ALREADY_CLOSED);

        stringBuilder.append(buf, offset, length);
        downsize();
    }

    @Override
    public void write(int c)
        throws IOException
    {
        if (isClosed)
            throw new IOException(ALREADY_CLOSED);

        stringBuilder.append(c);
        downsize();
    }

    @Override
    public void write(String s)
        throws IOException
    {
        if (isClosed)
            throw new IOException(ALREADY_CLOSED);

        stringBuilder.append(s);
        downsize();
    }

    @Override
    public void write(String s, int offset, int length)
        throws IOException
    {
        if (isClosed)
            throw new IOException(ALREADY_CLOSED);

        stringBuilder.append(s, offset, length);
        downsize();
    }

    private void downsize()
    {
        final int overage = stringBuilder.length() - maxLength;
        if (overage > 0)
            stringBuilder.delete(0, overage);
    }
}
