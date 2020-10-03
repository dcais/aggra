package com.dcais.aggra.spring;

import com.dcais.aggra.client.PoolingHttpClientConnectionManagerBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.config.AbstractFactoryBean;

import java.util.concurrent.TimeUnit;

public class PoolingHttpClientConnectionManagerFactory extends AbstractFactoryBean<PoolingHttpClientConnectionManager> {
  private Integer maxTotal = 200;
  private Integer defaultMaxPerRoute = 20;
  private Long timeToLive = 900l;
  private TimeUnit timeToLiveUnit = TimeUnit.SECONDS;

  public Integer getMaxTotal() {
    return maxTotal;
  }

  public void setMaxTotal(Integer maxTotal) {
    this.maxTotal = maxTotal;
  }

  public Integer getDefaultMaxPerRoute() {
    return defaultMaxPerRoute;
  }

  public void setDefaultMaxPerRoute(Integer defaultMaxPerRoute) {
    this.defaultMaxPerRoute = defaultMaxPerRoute;
  }

  @Override
  public Class<?> getObjectType() {
    return PoolingHttpClientConnectionManager.class;
  }

  @Override
  public PoolingHttpClientConnectionManager createInstance() throws Exception {
    PoolingHttpClientConnectionManagerBuilder builder = new PoolingHttpClientConnectionManagerBuilder();
    return builder.setMaxTotal(this.maxTotal)
      .setDefaultMaxPerRoute(this.defaultMaxPerRoute)
      .setTimeToLive(this.timeToLive)
      .setTimeToLimeUnit(this.timeToLiveUnit)
      .build();
  }
}
