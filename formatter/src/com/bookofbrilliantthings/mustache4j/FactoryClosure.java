package com.bookofbrilliantthings.mustache4j;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

public abstract class FactoryClosure
    extends SectionRendererData
    implements RendererFactory
{
    protected FactoryClosure(final List<FragmentRenderer> fragmentList, final boolean inverted,
            final Field field, final Method method)
    {
        super(fragmentList, inverted, field, method);
    }

    public abstract FragmentRenderer create();
}
