package com.bookofbrilliantthings.mustache4j.util;

import java.io.IOException;
import java.io.Writer;

public class SwitchableWriter
    extends Writer
{
    private final Writer plainWriter;
    private final Writer filteredWriter;
    private Writer writer;
    private boolean isClosed;

    public SwitchableWriter(Writer plainWriter, Writer filteredWriter)
    {
        this.plainWriter = plainWriter;
        this.filteredWriter = filteredWriter;
        writer = filteredWriter;
        isClosed = false;
    }

    public boolean setFiltered(boolean on)
        throws IOException
    {
        if (isClosed)
            throw new IOException(new IllegalStateException("stream has been closed"));

        if (on)
        {
            if (writer == filteredWriter)
                return true;
            else
            {
                writer.flush();
                writer = filteredWriter;
                return false;
            }
        }
        else
        {
            if (writer == plainWriter)
                return false;
            else
            {
                writer.flush();
                writer = plainWriter;
                return true;
            }
        }
    }

    @Override
    public Writer append(char c)
        throws IOException
    {
        return writer.append(c);
    }

    @Override
    public Writer append(CharSequence csq)
        throws IOException
    {
        return writer.append(csq);
    }

    @Override
    public Writer append(CharSequence csq, int start, int end)
        throws IOException
    {
        return writer.append(csq, start, end);
    }

    @Override
    public void close()
        throws IOException
    {
        plainWriter.close();
        filteredWriter.close(); // even if this closes plainWriter again, that's ok
        isClosed = true;
    }

    @Override
    public void flush()
        throws IOException
    {
        writer.flush();
    }

    @Override public void write(char[] cbuf)
        throws IOException
    {
        writer.write(cbuf);
    }

    @Override
    public void write(char[] cbuf, int off, int len)
        throws IOException
    {
        writer.write(cbuf, off, len);
    }

    @Override
    public void write(int c)
        throws IOException
    {
        writer.write(c);
    }

    @Override
    public void write(String str)
        throws IOException
    {
        writer.write(str);
    }

    @Override
    public void write(String str, int off, int len)
        throws IOException
    {
        writer.write(str, off, len);
    }
}
