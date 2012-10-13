package com.bookofbrilliantthings.mustache4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;

public class FileLoader
    implements MustacheLoader
{
    private final File root;
    private final Charset charset;

    public FileLoader(File root, Charset charset)
    {
        this.root = root;
        this.charset = charset;
    }

    @Override
    public MustacheEdition load(MustacheServices services, String name,
            Class<?> forClass) throws MustacheParserException
    {
        final File file = new File(root, name);
        try
        {
            final long lastModified = file.lastModified(); // capture this before we read the file
            final FileInputStream inputStream = new FileInputStream(file);
            final Reader reader = new InputStreamReader(inputStream, charset);
            final MustacheRenderer renderer = Mustache.compile(services, reader, forClass);
            return new FileEdition(renderer, file, lastModified);
        }
        catch(IOException ioe)
        {
            throw new MustacheParserException(null, "couldn't open mustache template file " + file.getPath());
        }
    }
}
