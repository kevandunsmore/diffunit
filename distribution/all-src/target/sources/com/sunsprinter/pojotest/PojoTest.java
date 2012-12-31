package com.sunsprinter.pojotest;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * PojoTest
 *
 * @author Kevan Dunsmore
 * @created 2011/12/07
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface PojoTest
{
}
