package com.bookofbrilliantthings.mustache4j;

public class ValueReference
{
    public final ValueSource valueSource;
    public final int stackDepth;

    public ValueReference(ValueSource valueSource, int stackDepth)
    {
        this.valueSource = valueSource;
        this.stackDepth = stackDepth;
    }
}
