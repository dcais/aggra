package com.github.dcais.aggra.logger;

import com.github.dcais.aggra.request.RequestBuilder;
import org.slf4j.Logger;

import java.lang.reflect.Method;
import java.util.Date;

public class BaseLoggerImpl extends AbstractLoggerImpl {
  private static final Logger log = org.slf4j.LoggerFactory.getLogger(BaseLoggerImpl.class);

  @Override
  public void doLog(Method method, Date sendDate, Date receiveDate, RequestBuilder requestBuilder, String response, int maxCharCountReq, int maxCharCountRes) {
    super.logContent(log, sendDate, receiveDate, requestBuilder, response, maxCharCountReq, maxCharCountRes);
  }
}