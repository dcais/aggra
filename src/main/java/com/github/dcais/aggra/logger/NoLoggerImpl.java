package com.github.dcais.aggra.logger;

import com.github.dcais.aggra.request.RequestBuilder;
import org.slf4j.Logger;

import java.lang.reflect.Method;
import java.util.Date;

public class NoLoggerImpl implements HttpClientLogger {
  private static final Logger log = org.slf4j.LoggerFactory.getLogger(NoLoggerImpl.class);

  @Override
  public void doLog(Method method, Date sendDate, Date receiveDate, RequestBuilder requestBuilder, String response, int maxCharCountReq, int maxCharCountRes) {
    //do nothing
  }
}