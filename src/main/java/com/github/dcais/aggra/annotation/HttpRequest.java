package com.github.dcais.aggra.annotation;

import com.github.dcais.aggra.cons.HttpConstants;
import com.github.dcais.aggra.enu.HttpMethod;
import org.springframework.stereotype.Component;

import javax.lang.model.type.NullType;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface HttpRequest {
  String value() default "";

  HttpMethod method() default HttpMethod.UNKOWN;

  String url() default "";

  int timeout() default HttpConstants.DEFAULT_TIMEOUT;

  int connectionTimeout() default HttpConstants.DEFAULT_CONNECT_TIMEOUT;

  int retryCount() default HttpConstants.DEFAULT_RETRY_COUNT;

  Class resConvertClass() default NullType.class;

  Class loggerClass() default NullType.class;

  int logMaxCharCountReq() default HttpConstants.LMCC_UNKNOW; //日志记录调用参数的最大字符数

  int logMaxCharCountRes() default HttpConstants.LMCC_UNKNOW; //日志记录返回结果的最大字符数

  Class responseHandlerClass() default NullType.class;
}
