package com.github.dcais.aggra.logger;

import com.github.dcais.aggra.request.RequestBuilder;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.Date;

@Slf4j
public class BaseLoggerImpl extends AbstractLoggerImpl {
  @Override
  public void doLog(Method method, Date sendDate, Date receiveDate, RequestBuilder requestBuilder, String response, int maxCharCountReq, int maxCharCountRes) {
    super.logContent(log, sendDate, receiveDate, requestBuilder, response, maxCharCountReq, maxCharCountRes);
  }
}