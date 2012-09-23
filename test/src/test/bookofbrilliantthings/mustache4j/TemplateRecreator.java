package test.bookofbrilliantthings.mustache4j;

import com.bookofbrilliantthings.mustache4j.ParserHandler;

public class TemplateRecreator
    extends ParserHandler
{
    private final StringBuilder stringBuilder;

    public TemplateRecreator()
    {
        stringBuilder = new StringBuilder();
    }

    public String getTemplate()
    {
        return stringBuilder.toString();
    }

    @Override
    public void literal(String literal)
    {
        stringBuilder.append(literal);
    }

    @Override
    public void variable(String varName)
    {
        stringBuilder.append("{{");
        stringBuilder.append(varName);
        stringBuilder.append("}}");
    }

    @Override
    public void sectionBegin(String secName, boolean inverted)
    {
        stringBuilder.append("{{" + (inverted ? '^' : '#'));
        stringBuilder.append(secName);
        stringBuilder.append("}}");
    }

    @Override
    public void sectionEnd(String secName)
    {
        stringBuilder.append("{{/");
        stringBuilder.append(secName);
        stringBuilder.append("}}");
    }

    @Override
    public void comment(String comment)
    {
        stringBuilder.append("{{!");
        stringBuilder.append(comment);
        stringBuilder.append("}}");
    }
}
