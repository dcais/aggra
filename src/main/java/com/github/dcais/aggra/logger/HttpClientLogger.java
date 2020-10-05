package com.github.dcais.aggra.logger;


import com.github.dcais.aggra.request.RequestBuilder;

import java.lang.reflect.Method;
import java.util.Date;

public interface HttpClientLogger {
  void doLog(Method method, Date sendDate, Date receiveDate, RequestBuilder requestBuilder, String response, int maxCharCountReq, int maxCharCountRes);
}
