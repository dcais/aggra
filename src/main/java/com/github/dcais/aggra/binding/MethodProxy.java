package com.github.dcais.aggra.binding;


import com.github.dcais.aggra.client.HttpClient;
import com.github.dcais.aggra.client.SocksProxyHttpContext;
import com.github.dcais.aggra.common.ProxyRule;
import com.github.dcais.aggra.converter.SqResponseHandler;
import com.github.dcais.aggra.logger.BaseLoggerImpl;
import com.github.dcais.aggra.logger.HttpClientLogger;
import com.github.dcais.aggra.request.RequestBuilder;
import com.github.dcais.aggra.request.RequestBuilderParser;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.protocol.HttpContext;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.BeanFactory;

import javax.lang.model.type.NullType;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Date;

public class MethodProxy<T> implements InvocationHandler {
  public static final String NO_STRCVT_EXCEP_MSG = "aggra Interface return type is not 'String' while no StringConverter present.";
  private static final Logger log = Logger.getLogger(MethodProxy.class);
  private HttpClient httpClient;
  private final Class<T> methodInterface;
  private BeanFactory beanFactory;

  public MethodProxy(HttpClient httpClient, BeanFactory beanFactory, Class<T> methodInterface) {
    this.methodInterface = methodInterface;
    this.httpClient = httpClient;
    this.beanFactory = beanFactory;
  }

  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    Date sendTime, receiveTime;

    Object res = null;

    RequestBuilder requestBuilder = RequestBuilderParser.parse(this.methodInterface, method, args, this.httpClient, beanFactory);
    try {
      HttpRequestBase request = requestBuilder.build(httpClient.defaultRequestConfig());

      CloseableHttpClient closeableHttpClient = httpClient.getCloseableHttpClient();

      SqResponseHandler sqResponseHandler = new SqResponseHandler();
      if (requestBuilder.getResponseHeaders() != null) {
        sqResponseHandler.setHeads(requestBuilder.getResponseHeaders());
      }

      HttpContext httpContext = null;
      ProxyRule proxyRule = httpClient.getProxyRule();
      if (proxyRule != null && "socks".equals(proxyRule.getProxySchema()) && proxyRule.isUseProxy(requestBuilder.getUrl())) {
        httpContext = new SocksProxyHttpContext();
        ((SocksProxyHttpContext) httpContext).setSocksProxyRule(proxyRule);
      }
      sendTime = new Date();
      String result = closeableHttpClient.execute(request, sqResponseHandler, httpContext);
      receiveTime = new Date();

      Type returnType = method.getGenericReturnType();
      if (returnType == void.class) {
        res = null;
      } else if (returnType.equals(String.class)) {
        res = result;
      } else {
        if (requestBuilder.getStringConverter() == null) {
          throw new RuntimeException(NO_STRCVT_EXCEP_MSG);
        } else {
          res = requestBuilder.getStringConverter().fromString(result, returnType);
        }
      }

      Class loggerClass = requestBuilder.getLoggerClass();
      if (NullType.class.equals(loggerClass)) {
        loggerClass = BaseLoggerImpl.class;
      }


      try {
        HttpClientLogger logger = (HttpClientLogger) this.beanFactory.getBean(loggerClass);
        logger.doLog(method, sendTime, receiveTime, requestBuilder, result, requestBuilder.getLogMaxCharCountReq(), requestBuilder.getLogMaxCharCountRes());
      } catch (Exception e) {
        log.warn("do log error.", e);
      }

    } catch (HttpResponseException e) {
      String msg = "Exception happened, statusCode:" + e.getStatusCode() + ". " + requestBuilder.toString();
      log.error(msg, e);
      throw e;
    } catch (Exception e) {
      String msg = "Exception happened." + requestBuilder.toString();
      log.error(msg, e);
      throw e;
    }
    return res;
  }
}
