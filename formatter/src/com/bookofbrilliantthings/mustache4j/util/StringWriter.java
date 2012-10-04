package com.bookofbrilliantthings.mustache4j.util;

import java.io.IOException;
import java.io.Writer;

/**
 * This was created because java.util.StringWriter uses a StringBuffer, which is known not
 * to perform as well as StringBuilder because it is synchronized.
 *
 * @author cwestin
 *
 */
public class StringWriter
    extends Writer
{
    private final StringBuilder stringBuilder;
    private boolean isClosed;

    public StringWriter()
    {
        stringBuilder = new StringBuilder();
    }

    public StringWriter(final StringBuilder stringBuilder)
    {
        this.stringBuilder = stringBuilder;
        isClosed = false;
    }

    @Override
    public String toString()
    {
        return stringBuilder.toString();
    }

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

    private final static String ALREADY_CLOSED = "Writer is already closed";

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
    }

    @Override
    public void write(char buf[], int offset, int length)
        throws IOException
    {
        assert offset >= 0;
        assert length >= 0;

        if (isClosed)
            throw new IOException(ALREADY_CLOSED);

        if (length > 0)
            stringBuilder.append(buf, offset, length);
    }

    @Override
    public void write(int c)
        throws IOException
    {
        if (isClosed)
            throw new IOException(ALREADY_CLOSED);

        stringBuilder.append((char)c);
    }

    @Override
    public void write(String s)
        throws IOException
    {
        if (isClosed)
            throw new IOException(ALREADY_CLOSED);

        stringBuilder.append(s);
    }

    @Override
    public void write(String s, int offset, int length)
        throws IOException
    {
        assert offset >= 0;
        assert length >= 0;

        if (isClosed)
            throw new IOException(ALREADY_CLOSED);

        if (length > 0)
            stringBuilder.append(s, offset, offset + length);
    }
}
