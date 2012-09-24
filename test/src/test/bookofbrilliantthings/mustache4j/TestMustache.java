package test.bookofbrilliantthings.mustache4j;

import static org.junit.Assert.*;

import java.io.StringReader;
import java.util.HashMap;

import org.junit.Ignore;
import org.junit.Test;

import com.bookofbrilliantthings.mustache4j.Mustache;
import com.bookofbrilliantthings.mustache4j.MustacheRenderer;
import com.bookofbrilliantthings.mustache4j.MustacheValue;
import com.bookofbrilliantthings.mustache4j.Template;
import com.bookofbrilliantthings.mustache4j.util.StringBuilderWriter;


public class TestMustache
{

    public static class M1
    {
        @MustacheValue(tagname = "foo")
        public String xfoo;

        @MustacheValue
        public int bar;
    }

    private void testCorrectTemplate(final String template)
        throws Exception
    {
        final TemplateRecreator recreator = new TemplateRecreator();
        Template.parse(recreator, new StringReader(template));
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

    private static void testHashMap(final MustacheRenderer mustacheRenderer,
            final HashMap<String, ?> hashMap, final String template, final String expected)
        throws Exception
    {
        final StringBuilderWriter stringWriter = new StringBuilderWriter();
        mustacheRenderer.render(stringWriter, hashMap);
        final String result = stringWriter.toString();
        assertEquals(expected, result);
    }

    private static void testObject(final MustacheRenderer mustacheRenderer,
            final Object o, final String template, final String expected)
        throws Exception
    {
        final StringBuilderWriter stringWriter = new StringBuilderWriter();
        mustacheRenderer.render(stringWriter, o);
        final String result = stringWriter.toString();
        assertEquals(expected, result);
    }

    @Test
    public void testVariables()
    {
        final String template1 = "    {{foo}}     {{bar}}  ";

        try
        {
            final HashMap<String, Object> hashMap1 = new HashMap<String, Object>();
            hashMap1.put("foo", "a");
            hashMap1.put("bar", new Integer(3));

            final MustacheRenderer mustacheRendererH1 = Mustache.compileForHashMap(new StringReader(template1));
            testHashMap(mustacheRendererH1, hashMap1, template1, "    a     3  ");


            final M1 m1 = new M1();
            m1.xfoo = "a";
            m1.bar = 3;

            final MustacheRenderer mustacheRendererM1 = Mustache.compile(new StringReader(template1), M1.class);
            testObject(mustacheRendererM1, m1, template1, "    a     3  ");
        }
        catch(Exception e)
        {
            fail(e.toString());
        }
    }

    public static class M2
    {
        @MustacheValue(tagname = "hazPi")
        public boolean hasPi;

        @MustacheValue
        public double pi;
    }

    @Test
    public void testConditionalSections()
    {
        final String template1 = "{{#hazPi}}{{pi}}{{/hazPi}}";

        try
        {
/* TODO
            final HashMap<String, Object> hashMap1 = new HashMap<String, Object>();
            final MustacheRenderer mustacheRendererH1 = Mustache.compileForHashMap(new StringReader(template1));

            testHashMap(mustacheRendererH1, hashMap1, template1, "");

            hashMap1.put("hazPi", Boolean.TRUE);
            hashMap1.put("pi", new Double(Math.PI));

            testHashMap(mustacheRendererH1, hashMap1, template1, Double.toString(Math.PI));
*/

            final M2 m2 = new M2();
            final MustacheRenderer mustacheRendererM2 = Mustache.compile(new StringReader(template1), M2.class);

            m2.hasPi = false;
            m2.pi = 0;

            testObject(mustacheRendererM2, m2, template1, "");

            m2.hasPi = true;
            m2.pi = Math.PI;

            testObject(mustacheRendererM2, m2, template1, Double.toString(Math.PI));

        }
        catch(Exception e)
        {
            fail(e.toString());
        }
    }
}
