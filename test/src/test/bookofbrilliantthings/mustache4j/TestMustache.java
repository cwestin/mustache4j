package test.bookofbrilliantthings.mustache4j;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.StringReader;
import java.io.Writer;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Ignore;
import org.junit.Test;

import com.bookofbrilliantthings.mustache4j.Mustache;
import com.bookofbrilliantthings.mustache4j.MustacheEdition;
import com.bookofbrilliantthings.mustache4j.MustacheLoader;
import com.bookofbrilliantthings.mustache4j.MustacheParserException;
import com.bookofbrilliantthings.mustache4j.MustacheRenderer;
import com.bookofbrilliantthings.mustache4j.MustacheServices;
import com.bookofbrilliantthings.mustache4j.MustacheValue;
import com.bookofbrilliantthings.mustache4j.Template;
import com.bookofbrilliantthings.mustache4j.util.HtmlEscapeWriter;
import com.bookofbrilliantthings.mustache4j.util.StringBuilderWriter;

public class TestMustache
{
    private final static MustacheServices mustacheServices = new MustacheServices(false);

    private final static Pattern ampPattern = Pattern.compile("&");
    private final static Pattern ltPattern = Pattern.compile("<");
    private final static Pattern gtPattern = Pattern.compile(">");
    private final static Pattern quotPattern = Pattern.compile("\"");

    private static String globalReplace(String s, Pattern pattern, String rep)
    {
        final Matcher matcher = pattern.matcher(s);
        return matcher.replaceAll(rep);
    }

    private static String escapeHtml(String s)
    {
        s = globalReplace(s, ampPattern, "&amp;");
        s = globalReplace(s, ltPattern, "&lt;");
        s = globalReplace(s, gtPattern, "&gt;");
        s = globalReplace(s, quotPattern, "&quot;");
        return s;
    }

    @Test
    public void testEscapeWriter()
    {
        Writer escapeWriter = null;

        try
        {
            final String s1 = "<b>Github & beers</b>";
            final char c1[] = s1.toCharArray();
            final String e1 = escapeHtml(s1);

            StringBuilder stringBuilder = new StringBuilder();
            StringBuilderWriter stringWriter = new StringBuilderWriter(stringBuilder);
            escapeWriter = new HtmlEscapeWriter(stringWriter);

            stringBuilder.setLength(0);
            escapeWriter.write(s1);
            escapeWriter.flush();
            assertEquals(e1, stringBuilder.toString());

            stringBuilder.setLength(0);
            escapeWriter.write(c1);
            escapeWriter.flush();
            assertEquals(e1, stringBuilder.toString());

            // test the writers with offsets
            final String s2 = "abc 123 " + s1;
            final char c2[] = s2.toCharArray();

            stringBuilder.setLength(0);
            escapeWriter.write(s2, 8, s1.length());
            escapeWriter.flush();
            assertEquals(e1, stringBuilder.toString());

            stringBuilder.setLength(0);
            escapeWriter.write(c2, 8, s1.length());
            escapeWriter.flush();
            assertEquals(e1, stringBuilder.toString());
        }
        catch(Exception e)
        {
            fail(e.toString());
        }
        finally
        {
            try
            {
                if (escapeWriter != null)
                    escapeWriter.close();
            }
            catch(IOException ioe)
            {
                // nothing
            }
        }
    }

    public static class M0
    {
        @MustacheValue
        public String x;
    }

    @Test
    public void testHtml()
    {
        final String template1 = "<html><head><title>{{x}}</title></head><body>foo</body></html>";
        final String e1 = "<html><head><title>Fear &amp; Loathing</title></head><body>foo</body></html>";

        try
        {
            final MustacheRenderer mustacheRenderer1 =
                    Mustache.compile(mustacheServices, new StringReader(template1), M0.class);
            final M0 m0 = new M0();
            m0.x = "Fear & Loathing";

            testObject(mustacheRenderer1, m0, e1);
        }
        catch(Exception e)
        {
            fail(e.toString());
        }
    }

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
            testCorrectTemplate(
                "{{foo}}{{^bar}}{{> wombat}} {{& baz}}{{/bar}}");
        }
        catch(Exception e)
        {
            fail(e.toString());
        }
    }

    private static void testObject(final MustacheRenderer mustacheRenderer,
            final Object o, final String expected)
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
        final String template2 = "    {{& foo}}     {{bar}}  ";

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
            m1.xfoo = "&";
            m1.bar = 3;

            final MustacheRenderer mustacheRenderer1 =
                    Mustache.compile(mustacheServices, new StringReader(template1), M1.class);
            testObject(mustacheRenderer1, m1, "    &amp;     3  ");

            final MustacheRenderer mustacheRenderer2 =
                    Mustache.compile(mustacheServices, new StringReader(template2), M1.class);
            testObject(mustacheRenderer2, m1, "    &     3  ");
        }
        catch(Exception e)
        {
            fail(e.toString());
        }
    }

    public static class M2
    {
        M2()
        {
        }

        M2(double d)
        {
            pi = d;
        }

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
            final MustacheRenderer mustacheRendererM2 =
                    Mustache.compile(mustacheServices, new StringReader(template1), M2.class);
            final MustacheRenderer mustacheRendererM2a =
                    Mustache.compile(mustacheServices, new StringReader(template2), M2.class);

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

            final MustacheRenderer mustacheRenderer1 =
                    Mustache.compile(mustacheServices, new StringReader(template1), M3.class);
            final MustacheRenderer mustacheRenderer2 =
                    Mustache.compile(mustacheServices, new StringReader(template2), M3.class);
            final MustacheRenderer mustacheRenderer3 =
                    Mustache.compile(mustacheServices, new StringReader(template3), M3.class);
            final MustacheRenderer mustacheRenderer4 =
                    Mustache.compile(mustacheServices, new StringReader(template4), M3.class);

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
        public boolean showList;

        @MustacheValue
        public List<M2> listM2;
    }

    @Test
    public void testLists()
    {
        final String template1 = "{{#showList}}{{#listM2}}{{pi}}\n{{/listM2}}{{/showList}}";

        try
        {
            final MustacheRenderer mustacheRenderer =
                    Mustache.compile(mustacheServices, new StringReader(template1), M4.class);

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
            final MustacheRenderer mustacheRenderer =
                    Mustache.compile(mustacheServices, new StringReader(template1), M5.class);
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
            final MustacheRenderer mustacheRenderer1 =
                    Mustache.compile(mustacheServices, new StringReader(template1), M6.class);
            final MustacheRenderer mustacheRenderer2 =
                    Mustache.compile(mustacheServices, new StringReader(template2), M6.class);

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

    public static class M7
    {
        @MustacheValue
        public String myString;

        @MustacheValue
        public String yourString;

        public String theirString;

        @MustacheValue
        public String getTheirString()
        {
            return theirString;
        }
    }

    @Test
    public void testStringSections()
    {
        final String template1 = "{{#myString}}{{myString}}{{yourString}}{{/myString}}";
        final String template2 = "{{^myString}}{{yourString}}{{/myString}}";
        final String template3 = "{{#theirString}}{{myString}}{{/theirString}}";
        final String template4 = "{{^theirString}}{{yourString}}{{/theirString}}";

        try
        {
            final MustacheRenderer mustacheRenderer1 =
                    Mustache.compile(mustacheServices, new StringReader(template1), M7.class);
            final MustacheRenderer mustacheRenderer2 =
                    Mustache.compile(mustacheServices, new StringReader(template2), M7.class);
            final MustacheRenderer mustacheRenderer3 =
                    Mustache.compile(mustacheServices, new StringReader(template3), M7.class);
            final MustacheRenderer mustacheRenderer4 =
                    Mustache.compile(mustacheServices, new StringReader(template4), M7.class);

            final M7 m7 = new M7();

            m7.myString = "foo";
            m7.yourString = "bar";
            testObject(mustacheRenderer1, m7, "foobar");
            testObject(mustacheRenderer2, m7, "");

            m7.myString = null;
            testObject(mustacheRenderer2, m7, "bar");

            testObject(mustacheRenderer3, m7, "");
            testObject(mustacheRenderer4, m7, "bar");

            m7.myString = "foo";
            m7.theirString = "baz";
            testObject(mustacheRenderer3, m7, "foo");
            testObject(mustacheRenderer4, m7, "");
        }
        catch(Exception e)
        {
            fail(e.toString());
        }
    }

    @Test
    public void testPartials()
    {
        final String template1 = "in template1\n{{> testPartials/template2}}\nback to template1";
        final String template2 = " {{pi}} ";

        final MustacheLoader oldLoader = mustacheServices.getLoader();

        // add a new loader to the chain
        final MustacheLoader mustacheLoader = new MustacheLoader()
        {
            @Override
            public MustacheEdition load(MustacheServices services, String name, Class<?> forClass)
                    throws MustacheParserException
            {
                if (name.equals("testPartials/template2"))
                    return new MustacheEdition(Mustache.compile(services, new StringReader(template2), forClass));

                return oldLoader.load(services, name, forClass);
            }
        };

        mustacheServices.setLoader(mustacheLoader);

        try
        {
            final MustacheRenderer renderer1 =
                    Mustache.compile(mustacheServices, new StringReader(template1), M2.class);
            final M2 m2 = new M2();
            m2.pi = Math.PI;

            testObject(renderer1, m2,
                    "in template1\n " + Double.toString(Math.PI) + " \nback to template1");;
        }
        catch(Exception e)
        {
            fail(e.toString());
        }
        finally
        {
            // restore previous loader
            mustacheServices.setLoader(oldLoader);
        }
    }

    private static class VersionedTemplate
    {
        final String template;
        final int version;

        VersionedTemplate(String template, int version)
        {
            this.template = template;
            this.version = version;
        }
    }

    private static class CacheTestLoader
        implements MustacheLoader
    {
        final HashMap<String, VersionedTemplate> templateMap;

        CacheTestLoader()
        {
            templateMap = new HashMap<String, VersionedTemplate>();
        }

        private class TestEdition
            extends MustacheEdition
        {
            private final String name;
            private final int version;

            public TestEdition(MustacheRenderer renderer, String name, int version)
            {
                super(renderer);
                this.name = name;
                this.version = version;
            }

            @Override
            public boolean newerAvailable()
            {
                final VersionedTemplate vTemplate = templateMap.get(name);
                return (vTemplate.version > version);
            }
        }

        @Override
        public MustacheEdition load(MustacheServices services, String name,
                Class<?> forClass) throws MustacheParserException
        {
            final VersionedTemplate vTemplate = templateMap.get(name);
            if (vTemplate == null)
                throw new MustacheParserException("can't find template named \"" + name + "\"");

            final MustacheRenderer renderer = Mustache.compile(
                    services, new StringReader(vTemplate.template), forClass);
            return new TestEdition(renderer, name, vTemplate.version);
        }
    }

    @Test
    public void testCache()
    {
        final CacheTestLoader cacheTestLoader = new CacheTestLoader();
        final MustacheServices services = new MustacheServices(true);
        services.setLoader(cacheTestLoader);

        try
        {
            final M4 m4 = new M4();
            m4.listM2 = new LinkedList<M2>();
            m4.listM2.add(new M2(42.17));

            // prepare to load some stuff; simulate this coming from a file system or similar
            cacheTestLoader.templateMap.put("outer", new VersionedTemplate("outer", 1));
            cacheTestLoader.templateMap.put("inner", new VersionedTemplate("showList:{{showList}}\n", 1));

            final MustacheRenderer outerRenderer = services.getRenderer("outer", M4.class);
            testObject(outerRenderer, m4, "outer");

            // put in a new version of the outer template, and see if we get it
            cacheTestLoader.templateMap.put("outer", new VersionedTemplate("outer\n{{> inner}}", 2));
            testObject(outerRenderer, m4, "outer\nshowList:false\n");

            // put in a new version of the inner template, and see if we get it
            cacheTestLoader.templateMap.put("inner", new VersionedTemplate(
                    "{{#listM2}}{{pi}}\n{{/listM2}}", 2));
            testObject(outerRenderer, m4, "outer\n42.17\n");
        }
        catch(Exception e)
        {
            fail(e.toString());
        }
    }
}
