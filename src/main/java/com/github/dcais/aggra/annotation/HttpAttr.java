package com.github.dcais.aggra.annotation;


import com.github.dcais.aggra.cons.HttpConstants;
import com.github.dcais.aggra.enu.HttpMethod;

import javax.lang.model.type.NullType;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface HttpAttr {
  HttpMethod method() default HttpMethod.UNKOWN;

  String url() default "";

  int timeout() default -1;

  int connectionTimeout() default -1;

  int retryCount() default -1;

  Class resConvertClass() default NullType.class;

  int logMaxCharCountReq() default HttpConstants.LMCC_UNKNOW; //日志记录调用参数的最大字符数

  int logMaxCharCountRes() default HttpConstants.LMCC_UNKNOW; //日志记录返回结果的最大字符数

  Class loggerClass() default NullType.class;

  Class responseHandlerClass() default NullType.class;
}
