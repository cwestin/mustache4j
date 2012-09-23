package com.bookofbrilliantthings.mustache4j;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface MustacheValue
{
    String tagname() default "";
}
