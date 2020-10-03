package com.dcais.aggra.connect;

import com.dcais.aggra.client.SocksProxyHttpContext;
import com.dcais.aggra.common.ProxyRule;
import org.apache.http.annotation.Contract;
import org.apache.http.annotation.ThreadingBehavior;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.protocol.HttpContext;
import org.apache.http.ssl.SSLContexts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.SocketFactory;
import java.io.IOException;
import java.net.Socket;

@Contract(threading = ThreadingBehavior.SAFE)
public class SqSSLConnectionSocketFactory extends SSLConnectionSocketFactory {
  public static Logger logger = LoggerFactory.getLogger(SqSSLConnectionSocketFactory.class);

  public SqSSLConnectionSocketFactory() {
    super(SSLContexts.createDefault(), getDefaultHostnameVerifier());
  }

  @Override
  public Socket createSocket(final HttpContext context) throws IOException {
    if (context != null) {
      ProxyRule proxyRule = (ProxyRule) context.getAttribute(SocksProxyHttpContext.HTTP_SOCKS_PROXY_RULE);
      if (proxyRule != null) {
        if (logger.isDebugEnabled()) {
          logger.debug("open ssl socket using proxy: " + proxyRule.getProxy());
        }
        return new Socket(proxyRule.getJavaNetProxy());
      }
    }
    return SocketFactory.getDefault().createSocket();
  }
}
