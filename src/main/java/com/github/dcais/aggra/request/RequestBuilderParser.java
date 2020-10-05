package com.github.dcais.aggra.request;

import com.github.dcais.aggra.annotation.*;
import com.github.dcais.aggra.client.HttpClient;
import com.github.dcais.aggra.common.ProxyRule;
import com.github.dcais.aggra.cons.HttpConstants;
import com.github.dcais.aggra.converter.StringConverter;
import com.github.dcais.aggra.enu.HttpMethod;
import com.github.dcais.aggra.spring.PropValFill;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.BeanFactory;

import javax.lang.model.type.NullType;
import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Pattern;

/**
 *
 */
@Slf4j
public class RequestBuilderParser<T> {
  private static final Pattern SLASH_TAIL_PATTERN = Pattern.compile("/$");
  private final static String BEAN_NAME_PROPVALFILL = className2BeanName(PropValFill.class);

  private static String className2BeanName(Class clazz) {
    String clazzName = clazz.getSimpleName();
    return clazzName.substring(0, 1).toLowerCase() + clazzName.substring(1);

  }

  public static <T> RequestBuilder parse(
    Class<T> methodInterface,
    Method method,
    Object[] args,
    HttpClient httpClient,
    BeanFactory beanFactory
  ) {
    PropValFill propValFill = (PropValFill) beanFactory.getBean(BEAN_NAME_PROPVALFILL);
    RequestBuilder requestBuilder = new RequestBuilder();
    String urlStrBuilder = "";
    String resConverterBeanName = "defaultStringConverter";

    Map<String, String> headers = new HashMap<>();
    Map<String, Object> params = new HashMap<>();
    requestBuilder.setParams(params);
    requestBuilder.setRemark(String.format("%s.%s", method.getClass().getName(), method.getName()));

    HttpMethod httpMethod = HttpMethod.UNKOWN;

    requestBuilder.setHeaders(headers);

    //Class Anno
    Annotation[] classAnnotations = methodInterface.getAnnotations();
    for (Annotation anno : classAnnotations) {
      if (anno.annotationType().equals(HttpRequest.class)) {

        HttpRequest httpRequest = (HttpRequest) anno;

        if (httpRequest.method() != HttpMethod.UNKOWN) {
          httpMethod = httpRequest.method();
        }
        if (StringUtils.isNotBlank(httpRequest.url())) {
          urlStrBuilder = appendUrl(urlStrBuilder, httpRequest.url());
        }
        if (httpRequest.resConvertClass() != NullType.class) {
          resConverterBeanName = className2BeanName(httpRequest.resConvertClass());
        }
        requestBuilder.setTimeout(httpRequest.timeout());
        requestBuilder.setConnectionTimeout(httpRequest.connectionTimeout());
        requestBuilder.setRetryCount(httpRequest.retryCount());
        requestBuilder.setLoggerClass(httpRequest.loggerClass());
        requestBuilder.setReponseHandlerClass(httpRequest.responseHandlerClass());
        requestBuilder.setLogMaxCharCountReq(httpRequest.logMaxCharCountReq());
        requestBuilder.setLogMaxCharCountRes(httpRequest.logMaxCharCountRes());
      }
      if (anno.annotationType().equals(ReqHead.class)) {
        headerAnnoDeal(anno, propValFill, headers, "class");
      }
    }

    //Method Anno
    Annotation[] methodAnnotaions = method.getAnnotations();
    for (Annotation anno : methodAnnotaions) {
      Class annoClazz = anno.annotationType();
      if (annoClazz.equals(HttpAttr.class)) {
        HttpAttr httpAttr = (HttpAttr) anno;
        if (httpAttr.method() != HttpMethod.UNKOWN) {
          httpMethod = httpAttr.method();
        }
        if (StringUtils.isNotBlank(httpAttr.url())) {
          urlStrBuilder = appendUrl(urlStrBuilder, httpAttr.url());
        }
        if (httpAttr.timeout() > 0) {
          requestBuilder.setTimeout(httpAttr.timeout());
        }
        if (httpAttr.connectionTimeout() > 0) {
          requestBuilder.setConnectionTimeout(httpAttr.connectionTimeout());
        }
        if (httpAttr.retryCount() > 0) {
          requestBuilder.setRetryCount(httpAttr.retryCount());
        }
        if (HttpConstants.LMCC_UNKNOW != httpAttr.logMaxCharCountReq()) {
          requestBuilder.setLogMaxCharCountReq(httpAttr.logMaxCharCountReq());
        }
        if (HttpConstants.LMCC_UNKNOW != httpAttr.logMaxCharCountRes()) {
          requestBuilder.setLogMaxCharCountRes(httpAttr.logMaxCharCountRes());
        }
        if (!NullType.class.equals(httpAttr.loggerClass())) {
          requestBuilder.setLoggerClass(httpAttr.loggerClass());
        }
        if (!NullType.class.equals(httpAttr.responseHandlerClass())) {
          requestBuilder.setReponseHandlerClass(httpAttr.responseHandlerClass());
        }
        if (httpAttr.resConvertClass() != NullType.class) {
          resConverterBeanName = className2BeanName(httpAttr.resConvertClass());
        }
      } else if (annoClazz.equals(ReqHead.class)) {
        headerAnnoDeal(anno, propValFill, headers, "method");
      } else if (annoClazz.equals(ReqParamOrder.class)) {
        requestBuilder.setParamsOrder(((ReqParamOrder) anno).value());
      }
    }

    String url = propValFill.fill(urlStrBuilder);

    //Param Anno
    Object body = null;
    List<String> secureParaKeys = new ArrayList<>();
    int uploadCount = 0;
    if (args != null) {
      for (int i = 0; i < args.length; i++) {
//            boolean isBodyEntity = false;
        boolean isParam = false;
        boolean isParamSecure = false;
//            boolean isHead = false;
        String paramKey = "";

        Annotation[] annos = method.getParameterAnnotations()[i];
        Object arg = args[i];
        if (annos.length == 0) {

        }
        for (Annotation anno : annos) {
          Class<? extends Annotation> annotationType = anno.annotationType();
          if (annotationType.equals(ReqUrlVariable.class)) {
            ReqUrlVariable reqParam = (ReqUrlVariable) anno;
            String varName = reqParam.value();
            url = url.replaceAll("\\{" + varName + "\\}", arg.toString());
          } else if (annotationType.equals(ReqParam.class)) {
            ReqParam reqParam = (ReqParam) anno;
            if (arg instanceof Map) {
              params.putAll((Map) arg);
            } else {
              paramKey = reqParam.value();
              if (StringUtils.isNotBlank(paramKey)) {
                params.put(paramKey, arg);
              } else {
                log.warn("param key is empty!");
              }
            }
          } else if (annotationType.equals(ReqBody.class)) {
            body = arg;
            requestBuilder.setBody(body);
          } else if (annotationType.equals(ReqHead.class)) {
            String key = "";
            ReqHead header = (ReqHead) anno;
            key = header.value();
            if (StringUtils.isBlank(key)) {
              if (arg instanceof Map) {
                Iterator<Map.Entry> it = ((Map) arg).entrySet().iterator();
                while (it.hasNext()) {
                  Map.Entry entry = it.next();
                  String sKey = entry.getKey().toString();
                  String sValue = entry.getValue().toString();
                  if (StringUtils.isNotBlank(sKey) && StringUtils.isNotBlank(sValue)) {
                    headers.put(sKey, sValue);
                  }
                }
              } else {
                log.warn("ReqHead in parameter. value is empty");
              }
            } else {
              headers.put(key, arg.toString());
            }
          } else if (annotationType.equals(ReqFile.class)) {
            if (!(arg instanceof File)) {
              log.warn("parameter is not file type");
              continue;
            }
            ReqFile annoFile = (ReqFile) anno;
            String fieldName = annoFile.value();
            if (StringUtils.isBlank(fieldName)) {
              fieldName = "upload_" + uploadCount;
              uploadCount++;
            }
            requestBuilder.addUploadFile(fieldName, (File) arg);
          } else if (annotationType.equals(ReqSecure.class)) {
            isParamSecure = true;
          } else if (annotationType.equals(ReqTimeout.class)) {
            requestBuilder.setTimeout(new Integer(arg.toString()));
          } else if (annotationType.equals(ReqConnectionTimeout.class)) {
            requestBuilder.setConnectionTimeout(new Integer(arg.toString()));
          } else if (annotationType.equals(ResponseHeaders.class)) {
            requestBuilder.setResponseHeaders((List) arg);
          }
        }
        if (isParamSecure && !StringUtils.isBlank(paramKey)) {
          secureParaKeys.add(paramKey);
        }
      }
    }

    requestBuilder.setSecureParaKeys(secureParaKeys);

    requestBuilder.setHttpMehod(httpMethod);
    requestBuilder.setUrl(url);
    if (resConverterBeanName != null) {
      requestBuilder.setStringConverter((StringConverter) beanFactory.getBean(resConverterBeanName));
    }

    if (headers.containsKey(HttpConstants.HEAD_KEY_CONTENT_TYPE)) {
      requestBuilder.setContentType(headers.get(HttpConstants.HEAD_KEY_CONTENT_TYPE));
    }

    //Proxy
    ProxyRule proxyRule = httpClient.getProxyRule();
    if (proxyRule != null) {
      if (proxyRule.getProxySchema().startsWith("http") && proxyRule.isUseProxy(url)) {
        if (proxyRule.getCloseWhenProxy()) {
          String key = "Connection";
          if (!headers.containsKey(key)) {
            requestBuilder.getHeaders().put(key, "close");  //设置每次请求都使用新连接
          }
        }
        requestBuilder.setProxyRule(proxyRule);
      }
    }
    return requestBuilder;
  }

  private static void headerAnnoDeal(Annotation anno, PropValFill propValFill, Map<String, String> headers, String containerName) {
    ReqHead head = (ReqHead) anno;
    String[] keys = head.keys();
    String[] values = head.values();
    if (keys.length != values.length) {
      log.warn("ReqHead in " + containerName + ". keys.length not equal values.length");
    }
    for (int i = 0; i < keys.length; i++) {
      if (i >= values.length) {
        continue;
      }
      String key = keys[i];
      String value = values[i];
      if (StringUtils.isBlank(key)) {
        log.warn("ReqHead in " + containerName + ". key is empty");
        continue;
      }
      if (StringUtils.isBlank(value)) {
        log.warn("ReqHead in " + containerName + ". value is empty");
        continue;
      }
      headers.put(key, propValFill.fill(value));
    }
  }

  private static String appendUrl(String url1, String url2) {
    if (StringUtils.isBlank(url2)) {
      return url1;
    }

    if (url1 == null) {
      url1 = "";
    }

    if (StringUtils.isNotBlank(url1)) {
      if (!SLASH_TAIL_PATTERN.matcher(url1).find()) {
        url1 += "/";
      }
    }
    url1 = String.format("%s%s", url1, url2.replaceAll("^/+", ""));
    return url1;
  }


}
