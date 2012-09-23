package com.bookofbrilliantthings.mustache4j;

import java.io.IOException;
import java.io.Reader;
import java.util.LinkedList;

import com.bookofbrilliantthings.mustache4j.util.LocatorReader;

/*
Tokens to look for
{{ starts a variable tag (followed by a tag for the usual variable naming rules)
{{! starts a comment
{{> starts a partial, but seems to imply the remainder of the named item comes from a yaml-like file
{{# section: starts a conditional, introduces a list, or a lambda
{{^ starts an inverted section, the opposite of {{#
{{/ ends a conditional or list, or inverted section
}} close tag for tags, comments, partials, conditional/list, coditional/list-end

{{= set delimeters special pattern {{=<% %>=}}

 */
public class TemplateParser
{
    enum State
    {
        BEGIN,
        BRACE_ONE,
        BRACE_TWO,

        VARIABLE_NAME,
        VARIABLE_CLOSE,

        SECTION,
        SECTION_NAME,
        SECTION_CLOSE,

        CLOSER,
        CLOSER_NAME,
        CLOSER_CLOSE,

        COMMENT,
        COMMENT_CLOSE,

        PARTIAL,
        PARTIAL_CLOSE,

        DELIMITER_ASSIGNMENT,

        UNEXPECTED_CHAR,
        UNEXPECTED_EOF,
        EOF
    }

    public static void parse(final ParserHandler parserHandler, final Reader templateReader)
        throws IOException, MustacheParserException
    {
        final LocatorReader reader = new LocatorReader(templateReader);
        int c = -1;
        final StringBuilder stringBuilder = new StringBuilder();
        final LinkedList<String> sectionStack = new LinkedList<String>();
        boolean inverted = false;

        State state = State.BEGIN;
        while(true)
        {
            switch(state)
            {
            case BEGIN:
                c = reader.read();

                // check for any literal
                if (c != '{')
                {
                    // check for EOF
                    if (c == -1)
                    {
                        state = State.EOF;
                        break;
                    }

                    stringBuilder.append((char)c);
                    break;
                }

                state = State.BRACE_ONE;
                // FALLTHROUGH

            case BRACE_ONE:
                c = reader.read();

                // check for anything other than another brace
                if (c != '{')
                {
                    // add the previous singular brace
                    stringBuilder.append('{');

                    if (c == -1)
                    {
                        state = State.UNEXPECTED_EOF;
                        break;
                    }

                    // append the character we just read
                    stringBuilder.append((char)c);
                    state = State.BEGIN;
                    break;
                }

                // end the current literal, if any
                if (stringBuilder.length() > 0)
                {
                    parserHandler.literal(stringBuilder.toString());
                    stringBuilder.setLength(0);
                }

                state = State.BRACE_TWO;
                // FALLTHROUGH

            case BRACE_TWO:
                c = reader.read();

                // check for common cases
                if (Character.isJavaIdentifierStart(c))
                {
                    stringBuilder.append((char)c);
                    state = State.VARIABLE_NAME;
                    break;
                }

                if (c == '#')
                {
                    state = State.SECTION;
                    break;
                }

                if (c == '/')
                {
                    state = State.CLOSER;
                    break;
                }

                if (c == '^')
                {
                    inverted = true;
                    state = State.SECTION;
                    break;
                }

                if (c == '!')
                {
                    state = State.COMMENT;
                    break;
                }

                if (c == '>')
                {
                    state = State.PARTIAL;
                    break;
                }

                if (c == '=')
                {
                    state = State.DELIMITER_ASSIGNMENT;
                    break;
                }

                state = State.UNEXPECTED_CHAR;
                break;

            case VARIABLE_NAME:
                c = reader.read();

                if (c == '}')
                {
                    state = State.VARIABLE_CLOSE;
                    break;
                }

                if (Character.isJavaIdentifierPart(c))
                {
                    stringBuilder.append((char)c);
                    // no change of state
                    break;
                }

                state = State.UNEXPECTED_CHAR;
                break;

            case VARIABLE_CLOSE:
                c = reader.read();

                if (c == '}')
                {
                    parserHandler.variable(stringBuilder.toString());
                    stringBuilder.setLength(0);
                    state = State.BEGIN;
                    break;
                }

                state = State.UNEXPECTED_CHAR;
                break;

            case SECTION:
                c = reader.read();

                if (Character.isJavaIdentifierStart(c))
                {
                    stringBuilder.append((char)c);
                    state = State.SECTION_NAME;
                    break;
                }

                if (c == -1)
                {
                    state = State.UNEXPECTED_EOF;
                    break;
                }

                state = State.UNEXPECTED_CHAR;
                break;

            case SECTION_NAME:
                c = reader.read();

                if (c == '}')
                {
                    state = State.SECTION_CLOSE;
                    break;
                }

                if (Character.isJavaIdentifierPart(c))
                {
                    stringBuilder.append((char)c);
                    // no change of state
                    break;
                }

                state = State.UNEXPECTED_CHAR;
                break;

            case SECTION_CLOSE:
                c = reader.read();

                if (c == '}')
                {
                    final String secName = stringBuilder.toString();
                    sectionStack.addFirst(secName);
                    parserHandler.sectionBegin(secName, inverted);
                    stringBuilder.setLength(0);
                    inverted = false;
                    state = State.BEGIN;
                    break;
                }

                state = State.UNEXPECTED_CHAR;
                break;

            case CLOSER:
                c = reader.read();

                if (Character.isJavaIdentifierStart(c))
                {
                    stringBuilder.append((char)c);
                    state = State.CLOSER_NAME;
                    break;
                }

                state = State.UNEXPECTED_CHAR;
                break;

            case CLOSER_NAME:
                c = reader.read();

                if (c == '}')
                {
                    state = State.CLOSER_CLOSE;
                    break;
                }

                if (Character.isJavaIdentifierPart(c))
                {
                    stringBuilder.append((char)c);
                    // no change of state;
                    break;
                }

                state = State.UNEXPECTED_CHAR;
                break;

            case CLOSER_CLOSE:
                c = reader.read();

                if (c == '}')
                {
                    final String secName = stringBuilder.toString();
                    final String expectedName = sectionStack.peekFirst();
                    if (expectedName == null)
                        throw new MustacheParserException(reader, "unexpected section end");
                    if (!expectedName.equals(secName))
                        throw new MustacheParserException(reader, "mismatched section name closed");

                    parserHandler.sectionEnd(secName);
                    sectionStack.removeFirst();
                    stringBuilder.setLength(0);
                    state = State.BEGIN;
                    break;
                }

                state = State.UNEXPECTED_CHAR;
                break;

            case COMMENT:
                c = reader.read();

                if (c != '}')
                {
                    if (c == -1)
                    {
                        state = State.UNEXPECTED_EOF;
                        break;
                    }

                    stringBuilder.append((char)c);
                    // no change of state
                    break;
                }

                // FALLTHROUGH

            case COMMENT_CLOSE:
                c = reader.read();

                if (c == '}')
                {
                    parserHandler.comment(stringBuilder.toString());
                    stringBuilder.setLength(0);
                    state = State.BEGIN;
                    break;
                }

                // it wasn't a second closing brace, so treat it (and the first closing brace) as part of the comment
                stringBuilder.append('}');
                stringBuilder.append((char)c);
                state = State.COMMENT;
                break;

            case PARTIAL:
            case PARTIAL_CLOSE:
            case DELIMITER_ASSIGNMENT:
                throw new MustacheParserException(reader, "unimplemented feature");

            case UNEXPECTED_CHAR:
                if (c == -1)
                {
                    state = State.UNEXPECTED_EOF;
                    break;
                }

                throw new MustacheParserException(reader, "unexpected character '" + (char)c + "'");

            case UNEXPECTED_EOF:
                throw new MustacheParserException(reader, "unexpected EOF");

            case EOF:
                if (stringBuilder.length() != 0)
                {
                    parserHandler.literal(stringBuilder.toString());
                    stringBuilder.setLength(0);
                }

                parserHandler.done();
                return;
            }
        }
    }
}
