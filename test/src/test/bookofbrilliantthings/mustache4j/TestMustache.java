package test.bookofbrilliantthings.mustache4j;

import static org.junit.Assert.*;

import java.io.StringReader;

import org.junit.Ignore;
import org.junit.Test;

import com.bookofbrilliantthings.mustache4j.Mustache;
import com.bookofbrilliantthings.mustache4j.MustacheValue;
import com.bookofbrilliantthings.mustache4j.TemplateParser;
import com.bookofbrilliantthings.mustache4j.util.StringBuilderWriter;

public class TestMustache
{

    public static class M1
    {
        @MustacheValue(tagname = "foo")
        public String xfoo;

        @MustacheValue
        public String bar;
    }

    private void testCorrectTemplate(final String template)
        throws Exception
    {
        final TemplateRecreator recreator = new TemplateRecreator();
        TemplateParser.parse(recreator, new StringReader(template));
        final String recreatedTemplate = recreator.getTemplate();
        assertEquals(template, recreatedTemplate);
    }

    @Test
    public void testParser()
    {
        try
        {
            testCorrectTemplate("  {{foo}} {{#bar}} a {{baz}} {{/bar}}");
            testCorrectTemplate(
                "{{a}}{{#b}}    {{x}}{{#c}} a {{^d}}b{{/d}} c x{{/c}}{{/b}}");
            testCorrectTemplate(
                "{a{b}c} {!not a comment}{{!this is a comment }x{}}");
        }
        catch(Exception e)
        {
            fail(e.toString());
        }
    }

    @Ignore
    @Test
    public void testSimpleStrings()
    {
        final String template = "    {{foo}}     {{bar}}  ";
        final StringReader stringReader = new StringReader(template);
        final Mustache mustache = Mustache.compile(stringReader, M1.class);

        final M1 m1 = new M1();
        m1.xfoo = "a";
        m1.bar = "b";

        final StringBuilderWriter stringWriter = new StringBuilderWriter();
        //TODO mustache.format(stringWriter, m1);
        final String result = stringWriter.toString();


        fail("Not yet implemented");
    }
}
