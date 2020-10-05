package com.github.dcais.aggra.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Component
/**
 * 参数的名字列表，主要用于get请求时，对参数次序有要求的场景
 */
public @interface ReqParamOrder {
  String value() default "";
}
