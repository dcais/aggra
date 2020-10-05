package com.github.dcais.aggra.client;

import com.github.dcais.aggra.connect.SqPlainConnectionSocketFactory;
import com.github.dcais.aggra.connect.SqSSLConnectionSocketFactory;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

import java.util.concurrent.TimeUnit;

public class PoolingHttpClientConnectionManagerBuilder {
  private Integer maxTotal = 200;
  private Integer defaultMaxPerRoute = 20;
  private Long timeToLive = 900l;
  private TimeUnit timeToLiveUnit = TimeUnit.SECONDS;

  public PoolingHttpClientConnectionManager build() {
    PoolingHttpClientConnectionManager pcm = new PoolingHttpClientConnectionManager(RegistryBuilder.<ConnectionSocketFactory>create()
      .register("http", new SqPlainConnectionSocketFactory())
      .register("https", new SqSSLConnectionSocketFactory())
//                .register("http", PlainConnectionSocketFactory.getSocketFactory())
//                .register("https", SSLConnectionSocketFactory.getSocketFactory())
      .build(), null, null, null, this.timeToLive, this.timeToLiveUnit);
    pcm.setMaxTotal(this.maxTotal);
    pcm.setDefaultMaxPerRoute(this.defaultMaxPerRoute);
    return pcm;
  }

  public Integer getMaxTotal() {
    return maxTotal;
  }

  public PoolingHttpClientConnectionManagerBuilder setMaxTotal(Integer maxTotal) {
    this.maxTotal = maxTotal;
    return this;
  }

  public PoolingHttpClientConnectionManagerBuilder setTimeToLive(Long timeToLive) {
    this.timeToLive = timeToLive;
    return this;
  }

  public PoolingHttpClientConnectionManagerBuilder setTimeToLimeUnit(TimeUnit timeToLiveUnit) {
    this.timeToLiveUnit = timeToLiveUnit;
    return this;
  }

  public Integer getDefaultMaxPerRoute() {
    return defaultMaxPerRoute;
  }

  public PoolingHttpClientConnectionManagerBuilder setDefaultMaxPerRoute(Integer defaultMaxPerRoute) {
    this.defaultMaxPerRoute = defaultMaxPerRoute;
    return this;
  }
}
