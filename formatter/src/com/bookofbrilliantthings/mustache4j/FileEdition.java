package com.bookofbrilliantthings.mustache4j;

import java.io.File;

public class FileEdition
    extends MustacheEdition
{
    private final File file;
    private final long lastModified;

    public FileEdition(MustacheRenderer renderer, File file, long lastModified)
    {
        super(renderer);
        this.file = file;
        this.lastModified = lastModified;
    }

    @Override
    public boolean newerAvailable()
    {
        return (file.lastModified() > lastModified);
    }
}
