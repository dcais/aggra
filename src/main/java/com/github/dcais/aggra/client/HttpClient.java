package com.github.dcais.aggra.client;

import com.github.dcais.aggra.common.ProxyRule;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

import java.io.IOException;

@Slf4j
public class HttpClient {
  private Integer timeout = 20 * 1000;
  private Integer connectionTimeout = 10 * 1000;
  private Integer retryCount = 1;
  private ProxyRule proxyRule;

  private PoolingHttpClientConnectionManager pccm;

  private CloseableHttpClient closeableHttpClient;

  public RequestConfig defaultRequestConfig() {
    RequestConfig config =
      RequestConfig.custom().setSocketTimeout(timeout).setConnectTimeout(connectionTimeout).build();
    return config;
  }

  private synchronized CloseableHttpClient build() {
    HttpRequestRetryHandlerImpl handler = new HttpRequestRetryHandlerImpl();
    handler.setRetryCount(this.retryCount);
    RequestConfig config = this.defaultRequestConfig();

    org.apache.http.impl.client.HttpClientBuilder builder = HttpClients.custom();

    builder.setRetryHandler(handler).setDefaultRequestConfig(config);
    if (pccm != null) {
      builder.setConnectionManager(pccm);
    }
    CloseableHttpClient client = builder.build();
    return client;
  }

  public void close() {
    if (this.closeableHttpClient == null) {
      return;
    }
    try {
      this.closeableHttpClient.close();
    } catch (IOException e) {
      log.error("", e);
    }
    this.closeableHttpClient = null;
  }

  public Integer getTimeout() {
    return timeout;
  }

  public void setTimeout(Integer timeout) {
    this.timeout = timeout;
  }

  public Integer getConnectionTimeout() {
    return connectionTimeout;
  }

  public void setConnectionTimeout(Integer connectionTimeout) {
    this.connectionTimeout = connectionTimeout;
  }

  public Integer getRetryCount() {
    return retryCount;
  }

  public void setRetryCount(Integer retryCount) {
    this.retryCount = retryCount;
  }

  public PoolingHttpClientConnectionManager getPccm() {
    return pccm;
  }

  public void setPccm(PoolingHttpClientConnectionManager pccm) {
    this.pccm = pccm;
  }

  public CloseableHttpClient getCloseableHttpClient() {
    if (this.closeableHttpClient == null) {
      this.closeableHttpClient = this.build();
    }
    return this.closeableHttpClient;
  }

  public void setCloseableHttpClient(CloseableHttpClient closeableHttpClient) {
    this.closeableHttpClient = closeableHttpClient;
  }

  public ProxyRule getProxyRule() {
    return proxyRule;
  }

  public void setProxyRule(ProxyRule proxyRule) {
    this.proxyRule = proxyRule;
  }

}
