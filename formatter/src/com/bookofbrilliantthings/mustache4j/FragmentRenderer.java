package com.bookofbrilliantthings.mustache4j;


public interface FragmentRenderer
{
    public void render(HtmlEscapeWriter writer, Object o)
        throws Exception;
}
