package com.github.dcais.aggra.common;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 网络代理配置
 */
public class ProxyRule {
  static Logger log = LoggerFactory.getLogger(ProxyRule.class);
  private List<String> proxyUrlRegsExclude;
  private List<Pattern> proxyExcludePattern = null;
  private String proxy;
  private Boolean useProxy = false;
  private Boolean forceUseLocalProxy = false;
  private Boolean closeWhenProxy = true;  //使用代理时，是否每次使用完都关系连接
  private static String localIp;
  String proxySchema;
  String proxyHost;
  int proxyPort;

  static {
    InetAddress ia = null;
    try {
      localIp = getHostIp();
    } catch (Exception e) {
      log.error("", e);
    }
  }

  public Boolean getCloseWhenProxy() {
    return closeWhenProxy;
  }

  public void setCloseWhenProxy(Boolean closeWhenProxy) {
    this.closeWhenProxy = closeWhenProxy;
  }

  public List<String> getProxyUrlRegsExclude() {
    return proxyUrlRegsExclude;
  }

  public void setProxyUrlRegsExclude(List<String> proxyUrlRegsExclude) {
    this.proxyUrlRegsExclude = proxyUrlRegsExclude;
    List<Pattern> patterns = new ArrayList<>();
    if (proxyUrlRegsExclude != null) {
      for (String reg : proxyUrlRegsExclude) {
        patterns.add(Pattern.compile(reg, Pattern.CASE_INSENSITIVE));
      }
      proxyExcludePattern = patterns;
    }
  }

  public String getProxySchema() {
    return proxySchema;
  }

  public String getProxyHost() {
    return proxyHost;
  }

  public int getProxyPort() {
    return proxyPort;
  }

  public String getProxy() {
    return proxy;
  }

  public Proxy getJavaNetProxy() {
    Proxy javaNetProxy = null;
    if (proxy != null && getUseProxy()) {
      InetSocketAddress sa = new InetSocketAddress(getProxyHost(), getProxyPort());
      javaNetProxy = new Proxy("socks".equals(getProxySchema()) ? Proxy.Type.SOCKS : Proxy.Type.HTTP, sa);
    }
    return javaNetProxy;
  }

  public void setProxy(String proxy) {
    this.proxy = proxy;
    String[] info = proxy.split(":");
    if (info.length == 2) {
      proxySchema = "http";
      proxyHost = info[0].trim();
      proxyPort = Integer.valueOf(info[1]);
    } else if (info.length == 3) {
      proxySchema = info[0].trim().toLowerCase();
      proxyHost = info[1].trim();
      proxyPort = Integer.valueOf(info[2]);
    } else {
      throw new RuntimeException("代理配置错误：" + proxy + "    正确配置应该类似：[schema:]<host>:<port>");
    }
  }

  public Boolean getUseProxy() {
    return useProxy;
  }

  public void setUseProxy(Boolean useProxy) {
    this.useProxy = useProxy;
  }

  public Boolean getForceUseLocalProxy() {
    return forceUseLocalProxy;
  }

  public void setForceUseLocalProxy(Boolean forceUseLocalProxy) {
    this.forceUseLocalProxy = forceUseLocalProxy;
  }

  private static String getHostIp() {
    String localhost = "";
    try {
      localhost = InetAddress.getByName(getHostName()).getHostAddress();
    } catch (Exception e) {
      log.error("", e);
    }
    return localhost;
  }

  private static String getHostName() {
    String hostName = "";
    try {
      hostName = InetAddress.getLocalHost().getHostName();
    } catch (Exception e) {
      log.error("", e);
    }
    return hostName;
  }

  public boolean isUseProxy(String url) {
    if (StringUtils.isBlank(proxy)) {
      return false;
    }

    //本机不适用代理
    if (
      this.proxy.contains(this.localIp)
        || this.proxy.contains("127.0.0.1")
        || this.proxy.contains("localhost")
    ) {
      //强制使用本机代理
      if (this.forceUseLocalProxy) {
        return true;
      } else {
        return false;
      }
    }

    if (this.useProxy == false) {
      return false;
    }
    boolean isUseProxy = true;

    if (this.proxyExcludePattern != null && proxyExcludePattern.size() > 0) {
      for (Pattern reg : proxyExcludePattern) {
        Matcher matcher = reg.matcher(url);
        if (matcher.matches()) {
          isUseProxy = false;
          break;
        }
      }
    }
    return isUseProxy;
  }

}
