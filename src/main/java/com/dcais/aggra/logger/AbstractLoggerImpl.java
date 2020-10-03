package com.dcais.aggra.logger;

import com.dcais.aggra.cons.HttpConstants;
import com.dcais.aggra.request.RequestBuilder;
import org.slf4j.Logger;

import java.util.Date;

public abstract class AbstractLoggerImpl implements HttpClientLogger {
  protected void logContent(Logger log, Date sendDate, Date receiveDate, RequestBuilder requestBuilder, String response, int maxCharCountReq, int maxCharCountRes) {
    if (maxCharCountReq == HttpConstants.LMCC_UNKNOW) {
      maxCharCountReq = HttpConstants.LMCC_DEFAULT;
    }
    if (maxCharCountRes == HttpConstants.LMCC_UNKNOW) {
      maxCharCountRes = HttpConstants.LMCC_DEFAULT;
    }

    if (maxCharCountReq == 0 && maxCharCountRes == 0) return;
    if (log.isDebugEnabled()) {
      log.debug("Client Exec param:" + requestBuilder.info());
    }
    log.info(String.format("Send(%tH:%<tM:%<tS.%<tL) - Receive(%tH:%<tM:%<tS.%<tL)\nSendContent:%s\nReceiveContent:%s",
      sendDate, receiveDate, left(requestBuilder.toString(), maxCharCountReq), left(response, maxCharCountRes)));
  }

  private String left(Object obj, int maxCount) {
    if (obj == null || (maxCount < 0 && maxCount != HttpConstants.LMCC_ALL)) return "";
    String res = obj.toString();
    if (maxCount == HttpConstants.LMCC_ALL) return res;
    if (res.length() > maxCount) {
      return res.substring(0, maxCount) + " ...";
    } else {
      return res;
    }
  }
}