package com.bookofbrilliantthings.mustache4j.util;

import java.io.IOException;
import java.io.Writer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CorrectEscapeWriter
    extends Writer
{
    private final StringBuilder stringBuilder;
    private final Writer writer;
    private boolean isClosed;

    private final int MIN_FLUSH_SIZE = 8 * 1024;

    public CorrectEscapeWriter(Writer writer)
    {
        this.writer = writer;
        stringBuilder = new StringBuilder();
        isClosed = false;
    }

    @Override
    public Writer append(char c)
        throws IOException
    {
        checkOpen();
        stringBuilder.append(c);
        maybeFlush();
        return this;
    }

    @Override
    public Writer append(CharSequence csq)
        throws IOException
    {
        checkOpen();
        stringBuilder.append(csq);
        maybeFlush();
        return this;
    }

    @Override
    public Writer append(CharSequence csq, int start, int end)
        throws IOException
    {
        checkOpen();
        stringBuilder.append(csq, start, end);
        maybeFlush();
        return this;
    }

    @Override
    public void close()
        throws IOException
    {
        if (!isClosed)
        {
            flush();
            writer.close();
            isClosed = true;
        }
    }

    private final static Pattern htmlPatterns = Pattern.compile(
            "(<|>|&)|([^<>&]+)");

    @Override
    public void flush()
        throws IOException
    {
        checkOpen();
        if (stringBuilder.length() > 0)
        {
            final Matcher matcher = htmlPatterns.matcher(stringBuilder);
            while(matcher.find())
            {
                final String match = matcher.group();

                // if the match was longer than a single character, we know it can't need escaping
                if (match.length() > 1)
                {
                    writer.write(match);
                    continue;
                }

                final char c = match.charAt(0);
                switch(c)
                {
                case '<':
                    writer.write("&lt;");
                    break;

                case '>':
                    writer.write("&gt;");
                    break;

                case '&':
                    writer.write("&amp;");
                    break;

                default:
                    writer.write(c);
                    break;
                }
            }

            stringBuilder.setLength(0);
        }

        writer.flush();
    }

    @Override public void write(char[] cbuf)
        throws IOException
    {
        checkOpen();
        stringBuilder.append(cbuf);
        maybeFlush();
    }

    @Override
    public void write(char[] cbuf, int off, int len)
        throws IOException
    {
        checkOpen();
        stringBuilder.append(cbuf, off, len);
        maybeFlush();
    }

    @Override
    public void write(int c)
        throws IOException
    {
        checkOpen();
        stringBuilder.append((char)c);
        maybeFlush();
    }

    @Override
    public void write(String str)
        throws IOException
    {
        checkOpen();
        stringBuilder.append(str);
        maybeFlush();
    }

    @Override
    public void write(String str, int off, int len)
        throws IOException
    {
        checkOpen();
        stringBuilder.append(str, off, off + len);
        maybeFlush();
    }

    private void checkOpen()
        throws IOException
    {
        if (isClosed)
            throw new IOException(new IllegalStateException("stream is already closed"));
    }

    private void maybeFlush()
        throws IOException
    {
        if (stringBuilder.length() >= MIN_FLUSH_SIZE)
            flush();
    }
}
