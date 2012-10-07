package test.bookofbrilliantthings.mustache4j;

import com.bookofbrilliantthings.mustache4j.MustacheParserException;
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
        throws MustacheParserException
    {
        stringBuilder.append(literal);
    }

    @Override
    public void variable(String varName)
        throws MustacheParserException
    {
        stringBuilder.append("{{");
        stringBuilder.append(varName);
        stringBuilder.append("}}");
    }

    @Override
    public void sectionBegin(String secName, boolean inverted)
        throws MustacheParserException
    {
        stringBuilder.append("{{" + (inverted ? '^' : '#'));
        stringBuilder.append(secName);
        stringBuilder.append("}}");
    }

    @Override
    public void sectionEnd(String secName)
        throws MustacheParserException
    {
        stringBuilder.append("{{/");
        stringBuilder.append(secName);
        stringBuilder.append("}}");
    }

    @Override
    public void partial(String partialName)
        throws MustacheParserException
    {
        stringBuilder.append("{{> ");
        stringBuilder.append(partialName);
        stringBuilder.append("}}");
    }

    @Override
    public void unescape(String varName)
        throws MustacheParserException
    {
        stringBuilder.append("{{& ");
        stringBuilder.append(varName);
        stringBuilder.append("}}");

    }

    @Override
    public void comment(String comment)
        throws MustacheParserException
    {
        stringBuilder.append("{{!");
        stringBuilder.append(comment);
        stringBuilder.append("}}");
    }
}
