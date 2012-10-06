package com.bookofbrilliantthings.mustache4j;

import java.util.List;


public class ObjectRenderer
    extends BasicRenderer
{
    private final Class<?> forClass;

    public ObjectRenderer(List<FragmentRenderer> fragmentList, Class<?> forClass)
    {
        super(fragmentList);
        assert forClass != null;
        this.forClass = forClass;
    }

    @Override
    public void render(HtmlEscapeWriter writer, Object o)
        throws Exception
    {
        // check the class argument once at the beginning
        if (!forClass.isAssignableFrom(o.getClass()))
            throw new IllegalArgumentException("object does not have the required class ('" +
                    forClass.getName() + "')");

        super.render(writer, o);
    }
}
