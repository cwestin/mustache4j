package com.bookofbrilliantthings.mustache4j;

import java.io.Writer;

public interface FragmentRenderer
{
    public void render(Writer writer, Object o)
        throws Exception;
}
