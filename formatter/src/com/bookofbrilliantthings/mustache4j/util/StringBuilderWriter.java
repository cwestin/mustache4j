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
public class StringBuilderWriter
    extends Writer
{
    private final StringBuilder stringBuilder;
    private boolean isClosed;

    public StringBuilderWriter()
    {
        stringBuilder = new StringBuilder();
    }

    public StringBuilderWriter(final StringBuilder stringBuilder)
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
    public Writer append(char c)
        throws IOException
    {
        checkOpen();
        stringBuilder.append(c);
        return this;
    }

    @Override
    public Writer append(CharSequence csq)
        throws IOException
    {
        checkOpen();
        stringBuilder.append(csq);
        return this;
    }

    @Override
    public Writer append(CharSequence csq, int start, int end)
        throws IOException
    {
        assert start >= 0;
        assert end <= csq.length();

        checkOpen();
        stringBuilder.append(csq, start, end);
        return this;
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

    @Override
    public void flush()
        throws IOException
    {
        checkOpen();
    }

    @Override
    public void write(char buf[])
        throws IOException
    {
        checkOpen();
        stringBuilder.append(buf);
    }

    @Override
    public void write(char buf[], int off, int len)
        throws IOException
    {
        assert off >= 0;
        assert len >= 0;

        checkOpen();
        stringBuilder.append(buf, off, len);
    }

    @Override
    public void write(int c)
        throws IOException
    {
        checkOpen();
        stringBuilder.append((char)c);
    }

    @Override
    public void write(String s)
        throws IOException
    {
        checkOpen();
        stringBuilder.append(s);
    }

    @Override
    public void write(String s, int off, int len)
        throws IOException
    {
        assert off >= 0;
        assert len >= 0;

        checkOpen();
        stringBuilder.append(s, off, off + len);
    }

    private void checkOpen()
        throws IOException
    {
        if (isClosed)
            throw new IOException(new IllegalStateException("stream is already closed"));
    }
}
