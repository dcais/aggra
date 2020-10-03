package com.dcais.aggra.client;

import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.protocol.HttpContext;

import javax.net.ssl.SSLException;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.UnknownHostException;

public class HttpRequestRetryHandlerImpl implements HttpRequestRetryHandler {
  private Integer retryCount = 1;

  @Override
  public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
    if (executionCount >= retryCount) {
      // Do not retry if over max retry count
      return false;
    }
    if (exception instanceof InterruptedIOException) {
      // Timeout
      return true;
    }
    if (exception instanceof UnknownHostException) {
      // Unknown host
      return true;
    }
    if (exception instanceof ConnectTimeoutException) {
      // Connection refused
      return false;
    }
    if (exception instanceof SSLException) {
      // SSL handshake exception
      return false;
    }
    HttpClientContext clientContext = HttpClientContext.adapt(context);
    HttpRequest request = clientContext.getRequest();
    boolean idempotent = !(request instanceof HttpEntityEnclosingRequest);
    if (idempotent) {
      // Retry if the request is considered idempotent
      return true;
    }
    return false;
  }

  public Integer getRetryCount() {
    return retryCount;
  }

  public void setRetryCount(Integer retryCount) {
    this.retryCount = retryCount;
  }
}
