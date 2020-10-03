package com.dcais.aggra.logger;

import com.dcais.aggra.request.RequestBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Date;

public class CallerLoggerImpl extends AbstractLoggerImpl {
  @Override
  public void doLog(Method method, Date sendDate, Date receiveDate, RequestBuilder requestBuilder, String response, int maxCharCountReq, int maxCharCountRes) {
    Logger log = LoggerFactory.getLogger(method.getDeclaringClass());
    super.logContent(log, sendDate, receiveDate, requestBuilder, response, maxCharCountReq, maxCharCountRes);
  }
}