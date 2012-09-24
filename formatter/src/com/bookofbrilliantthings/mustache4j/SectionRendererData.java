package com.bookofbrilliantthings.mustache4j;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;


public class SectionRendererData
{
    protected final List<FragmentRenderer> fragmentList;
    protected final boolean inverted;
    protected final Field field;
    protected final Method method;

    protected SectionRendererData(final List<FragmentRenderer> fragmentList, final boolean inverted,
            final Field field, final Method method)
    {
        this.fragmentList = fragmentList;
        this.inverted = inverted;
        this.field = field;
        this.method = method;
    }

    protected SectionRendererData(final FactoryClosure other)
    {
        // we clone the list so we can be sure this is immutable
        fragmentList = new ArrayList<FragmentRenderer>(other.fragmentList);
        inverted = other.inverted;
        field = other.field;
        method = other.method;
    }
}
