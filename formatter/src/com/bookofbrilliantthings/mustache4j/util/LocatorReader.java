package com.bookofbrilliantthings.mustache4j.util;

import java.io.IOException;
import java.io.Reader;

import com.bookofbrilliantthings.mustache4j.Locator;

public class LocatorReader
    extends Reader
    implements Locator
{
    private Reader reader;
    private int lineCount;
    private int linePos;

    public LocatorReader(Reader reader)
    {
        this.reader = reader;
        lineCount = 0;
        linePos = 0;
    }

    @Override
    public int getLineCount()
    {
        return lineCount;
    }

    @Override
    public int getLinePos()
    {
        return linePos;
    }

    @Override
    public void close()
        throws IOException
    {
        reader.close();
    }

    @Override
    public int read()
        throws IOException
    {
        final int c = reader.read();
        if (c != '\n')
            ++linePos;
        else
        {
            ++lineCount;
            linePos = 0;
        }

        return c;
    }

    @Override
    public int read(char cbuf[])
        throws IOException
    {
        final int n = reader.read(cbuf);
        update(cbuf, 0, n);
        return n;
    }

    @Override
    public int read(char[] cbuf, int offset, int length)
        throws IOException
    {
        final int n = reader.read(cbuf, offset, length);
        update(cbuf, offset, n);
        return n;
    }

    private void update(final char buf[], final int offset, final int length)
    {
        final int end = offset + length;
        for(int i = offset; i < end; ++i)
        {
            if (buf[i] != '\n')
                ++linePos;
            else
            {
                ++lineCount;
                linePos = 0;
            }
        }
    }
}
