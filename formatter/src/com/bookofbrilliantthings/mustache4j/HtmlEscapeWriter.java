package com.bookofbrilliantthings.mustache4j;

import java.io.IOException;
import java.io.Writer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HtmlEscapeWriter
    extends Writer
{
    private final Writer writer;
    private boolean isClosed;

    public HtmlEscapeWriter(Writer writer)
    {
        this.writer = writer;
        isClosed = false;
    }

    public Writer getUnescapedWriter()
    {
        return writer;
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

    private final static String ALREADY_CLOSED = "stream is already closed";

    @Override
    public void flush()
        throws IOException
    {
        if (isClosed)
            throw new IOException(ALREADY_CLOSED);

        writer.flush();
    }

    @Override
    public void write(char buf[])
        throws IOException
    {
        write(buf, 0, buf.length);
    }

    @Override
    public void write(char buf[], int offset, int length)
        throws IOException
    {
        if (isClosed)
            throw new IOException(ALREADY_CLOSED);

        final int endOffset = length + offset;
        int lastStart = offset;
        int i = offset;
        for(; i < endOffset; ++i)
        {
            final char c = buf[i];

            // check for the characters we escape
            if ("<>&\"".indexOf(c) < 0)
                continue;

            // we found a character that needs escaping; write everything up to that point
            final int len = i - lastStart;
            if (len > 0)
                writer.write(buf, lastStart, len);

            // escape the current character
            write(c);

            lastStart = i + 1;
        }

        final int len = i - lastStart;
        if (len > 0)
            writer.write(buf, lastStart, len);
    }

    private static String getEscape(int c)
    {
        switch(c)
        {
        case '<':
            return "&lt;";

        case '>':
            return "&gt;";

        case '&':
            return "&amp;";

        case '"':
            return "&quot;";

        default:
            return null;
        }
    }

    @Override
    public void write(int c)
        throws IOException
    {
        if (isClosed)
            throw new IOException(ALREADY_CLOSED);

        final String escape = getEscape(c);
        if (escape != null)
            writer.write(escape);
        else
            writer.write(c);
    }

    @Override
    public void write(String s)
        throws IOException
    {
        write(s, 0, s.length());
    }

    private final static Pattern pattern = Pattern.compile("([<>&\"])");

    @Override
    public void write(String s, int offset, int length)
        throws IOException
    {
        if (isClosed)
            throw new IOException(ALREADY_CLOSED);

        int lastStart = 0;
        final CharSequence charSequence = s.subSequence(offset, offset + length);
        final Matcher matcher = pattern.matcher(charSequence);
        while(matcher.find())
        {
            assert matcher.start(1) == matcher.end(1) - 1; // only matched one character
            final int preMatchLength = matcher.start(1) - lastStart;
            if (preMatchLength > 0)
                writer.write(s, lastStart + offset, preMatchLength);

            final String htmlStr = matcher.group(1);
            write(htmlStr.charAt(0));
            lastStart = matcher.end(1);
        }

        if (lastStart < length)
            writer.write(s, lastStart + offset, length - lastStart);
    }
}
