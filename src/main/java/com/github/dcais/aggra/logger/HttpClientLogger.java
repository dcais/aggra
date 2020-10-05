package com.github.dcais.aggra.logger;


import com.github.dcais.aggra.request.RequestBuilder;

import java.lang.reflect.Method;
import java.util.Date;

public interface HttpClientLogger {
  /**
   * @param method
   * @param sendDate
   * @param receiveDate
   * @param requestBuilder
   * @param response
   * @param maxCharCountReq req日志输出的最大字符数，超出部分被截取，并被后缀" ..."
   * @param maxCharCountRes res日志输出的最大字符数，超出部分被截取，并被后缀" ..."
   */
  void doLog(Method method, Date sendDate, Date receiveDate, RequestBuilder requestBuilder, String response, int maxCharCountReq, int maxCharCountRes);
}
