package test.bookofbrilliantthings.mustache4j;

import static org.junit.Assert.*;

import java.io.StringReader;
import java.util.LinkedList;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import com.bookofbrilliantthings.mustache4j.Mustache;
import com.bookofbrilliantthings.mustache4j.MustacheRenderer;
import com.bookofbrilliantthings.mustache4j.MustacheValue;
import com.bookofbrilliantthings.mustache4j.Template;
import com.bookofbrilliantthings.mustache4j.util.StringWriter;

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

/* TODO
    private static void testHashMap(final MustacheRenderer mustacheRenderer,
            final HashMap<String, ?> hashMap, final String template, final String expected)
        throws Exception
    {
        final StringBuilderWriter stringWriter = new StringBuilderWriter();
        mustacheRenderer.render(stringWriter, hashMap);
        final String result = stringWriter.toString();
        assertEquals(expected, result);
    }
*/

    private static void testObject(final MustacheRenderer mustacheRenderer,
            final Object o, final String expected)
        throws Exception
    {
        final StringWriter stringWriter = new StringWriter();
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
/* TODO See Mustache.java
            final HashMap<String, Object> hashMap1 = new HashMap<String, Object>();
            hashMap1.put("foo", "a");
            hashMap1.put("bar", new Integer(3));

            final MustacheRenderer mustacheRendererH1 = Mustache.compileForHashMap(new StringReader(template1));
            testHashMap(mustacheRendererH1, hashMap1, template1, "    a     3  ");
*/

            final M1 m1 = new M1();
            m1.xfoo = "a";
            m1.bar = 3;

            final MustacheRenderer mustacheRendererM1 = Mustache.compile(new StringReader(template1), M1.class);
            testObject(mustacheRendererM1, m1, "    a     3  ");
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
        public Boolean hasPiB;

        @MustacheValue
        public double pi;
    }

    @Test
    public void testConditionalSections()
    {
        final String template1 = "{{#hazPi}}{{pi}}{{/hazPi}}";
        final String template2 = "{{#hasPiB}}{{pi}}{{/hasPiB}}";

        try
        {
/* TODO See Mustache.java
            final HashMap<String, Object> hashMap1 = new HashMap<String, Object>();
            final MustacheRenderer mustacheRendererH1 = Mustache.compileForHashMap(new StringReader(template1));

            testHashMap(mustacheRendererH1, hashMap1, template1, "");

            hashMap1.put("hazPi", Boolean.TRUE);
            hashMap1.put("pi", new Double(Math.PI));

            testHashMap(mustacheRendererH1, hashMap1, template1, Double.toString(Math.PI));
*/

            final M2 m2 = new M2();
            final MustacheRenderer mustacheRendererM2 = Mustache.compile(new StringReader(template1), M2.class);
            final MustacheRenderer mustacheRendererM2a = Mustache.compile(new StringReader(template2), M2.class);

            m2.hasPi = false;
            m2.hasPiB = Boolean.FALSE;
            m2.pi = 0;

            testObject(mustacheRendererM2, m2, "");
            testObject(mustacheRendererM2a, m2, "");

            m2.hasPi = true;
            m2.hasPiB = Boolean.TRUE;
            m2.pi = Math.PI;

            testObject(mustacheRendererM2, m2, Double.toString(Math.PI));
            testObject(mustacheRendererM2a, m2, Double.toString(Math.PI));
        }
        catch(Exception e)
        {
            fail(e.toString());
        }
    }

    public static class M3
    {
        @MustacheValue
        public M2 m2;

        @MustacheValue
        public char flubber;

        @MustacheValue
        public char sonOfFlubber;

        @MustacheValue
        public M2 getF2()
        {
            return m2;
        }
    }

    @Test
    public void testNestedSections()
    {
        final String template1 = "{{flubber}}{{#m2}}{{#hazPi}}{{pi}}{{/hazPi}}{{/m2}}{{sonOfFlubber}}";
        final String template2 = "{{^m2}}{{flubber}}no m2!{{sonOfFlubber}}{{/m2}}";

        final String template3 = "{{flubber}}{{#f2}}{{#hazPi}}{{pi}}{{/hazPi}}{{/f2}}{{sonOfFlubber}}";
        final String template4 = "{{^f2}}{{flubber}}no m2!{{sonOfFlubber}}{{/f2}}";

        try
        {
/* TODO See Mustache.java
            final HashMap<String, Object> hashMap1 = new HashMap<String, Object>();
            final MustacheRenderer mustacheRendererH1 = Mustache.compileForHashMap(new StringReader(template1));

            testHashMap(mustacheRendererH1, hashMap1, template1, "");

            hashMap1.put("hazPi", Boolean.TRUE);
            hashMap1.put("pi", new Double(Math.PI));

            testHashMap(mustacheRendererH1, hashMap1, template1, Double.toString(Math.PI));
*/

            final MustacheRenderer mustacheRenderer1 = Mustache.compile(new StringReader(template1), M3.class);
            final MustacheRenderer mustacheRenderer2 = Mustache.compile(new StringReader(template2), M3.class);
            final MustacheRenderer mustacheRenderer3 = Mustache.compile(new StringReader(template3), M3.class);
            final MustacheRenderer mustacheRenderer4 = Mustache.compile(new StringReader(template4), M3.class);

            final M3 m3 = new M3();
            m3.flubber = '\'';
            m3.sonOfFlubber = '\'';
            m3.m2 = null;

            testObject(mustacheRenderer1, m3, "''");
            testObject(mustacheRenderer2, m3, "'no m2!'");
            testObject(mustacheRenderer3, m3, "''");
            testObject(mustacheRenderer4, m3, "'no m2!'");

            final M2 m2 = new M2();
            m2.hasPi = true;
            m2.pi = Math.PI;
            m3.m2 = m2;
            testObject(mustacheRenderer1, m3, "'" + Double.toString(Math.PI) + "'");
            testObject(mustacheRenderer3, m3, "'" + Double.toString(Math.PI) + "'");
        }
        catch(Exception e)
        {
            fail(e.toString());
        }
    }

    public static class M4
    {
        @MustacheValue
        boolean showList;

        @MustacheValue
        List<M2> listM2;
    }

    @Ignore // TODO
    @Test
    public void testLists()
    {
        final String template1 = "{{#showList}}{{#listM2}}{{pi}}\n{{/listM2}}{{/showList}}";

        try
        {
            final MustacheRenderer mustacheRenderer = Mustache.compile(new StringReader(template1), M4.class);

            final M4 m4 = new M4();

            // start out without even showing the list
            testObject(mustacheRenderer, m4, "");

            // now try showing the list, but the list doesn't exist, so we still should see nothing
            m4.showList = true;
            testObject(mustacheRenderer, m4, "");

            // add a list, but leave it empty
            m4.listM2 = new LinkedList<M2>();
            testObject(mustacheRenderer, m4, "");

            // now put things on the list
            M2 m2 = new M2();
            m2.pi = Math.PI;
            m4.listM2.add(m2);
            m2 = new M2();
            m2.pi = Math.E;
            m4.listM2.add(m2);
            testObject(mustacheRenderer, m4,
                    Double.toString(Math.PI) + "\n" + Double.toString(Math.E) + "\n");
        }
        catch(Exception e)
        {
            fail(e.toString());
        }
    }

    public static class M5
    {
        @MustacheValue
        public int getFoo()
        {
            return 42;
        }

        @MustacheValue(tagname = "baz")
        public String getBar()
        {
            return "bar";
        }
    }

    @Test
    public void testPrimitiveFunctions()
    {
        try
        {
            final String template1 = "{{baz}} {{foo}}";
            final MustacheRenderer mustacheRenderer = Mustache.compile(new StringReader(template1), M5.class);
            final M5 m5 = new M5();
            testObject(mustacheRenderer, m5, "bar 42");
        }
        catch(Exception e)
        {
            fail(e.toString());
        }
    }

    public static class M6
    {
        public boolean showPi;
        public boolean showE;

        @MustacheValue
        public boolean getShowPi()
        {
            return showPi;
        }

        @MustacheValue
        public double getPi()
        {
            return Math.PI;
        }

        @MustacheValue
        public boolean getDontShowE()
        {
            return !showE;
        }

        @MustacheValue
        public Boolean getDontShowEB()
        {
            return getDontShowE() ? Boolean.TRUE : Boolean.FALSE;
        }

        @MustacheValue
        public double getE()
        {
            return Math.E;
        }
    }

    @Test
    public void testConditionalMethods()
    {
        final String template1 = "{{#showPi}}{{pi}}{{/showPi}}{{^dontShowE}}{{e}}{{/dontShowE}}";
        final String template2 = "{{#showPi}}{{pi}}{{/showPi}}{{^dontShowEB}}{{e}}{{/dontShowEB}}";

        try
        {
            final MustacheRenderer mustacheRenderer1 = Mustache.compile(new StringReader(template1), M6.class);
            final MustacheRenderer mustacheRenderer2 = Mustache.compile(new StringReader(template2), M6.class);

            final M6 m6 = new M6();

            m6.showPi = true;
            m6.showE = false;
            testObject(mustacheRenderer1, m6, Double.toString(Math.PI));
            testObject(mustacheRenderer2, m6, Double.toString(Math.PI));

            m6.showPi = false;
            m6.showE = true;
            testObject(mustacheRenderer1, m6, Double.toString(Math.E));
            testObject(mustacheRenderer2, m6, Double.toString(Math.E));
        }
        catch(Exception e)
        {
            fail(e.toString());
        }
    }
}
