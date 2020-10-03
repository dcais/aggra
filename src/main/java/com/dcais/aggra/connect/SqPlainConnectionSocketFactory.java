package com.dcais.aggra.connect;


import com.dcais.aggra.client.SocksProxyHttpContext;
import com.dcais.aggra.common.ProxyRule;
import org.apache.http.HttpHost;
import org.apache.http.annotation.Contract;
import org.apache.http.annotation.ThreadingBehavior;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.protocol.HttpContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

@Contract(threading = ThreadingBehavior.IMMUTABLE)
public class SqPlainConnectionSocketFactory implements ConnectionSocketFactory {
  public static Logger logger = LoggerFactory.getLogger(SqPlainConnectionSocketFactory.class);

  public static final SqPlainConnectionSocketFactory INSTANCE = new SqPlainConnectionSocketFactory();

  public static SqPlainConnectionSocketFactory getSocketFactory() {
    return INSTANCE;
  }

  public SqPlainConnectionSocketFactory() {
    super();
  }

  @Override
  public Socket createSocket(final HttpContext context) throws IOException {
    if (context != null) {
      ProxyRule proxyRule = (ProxyRule) context.getAttribute(SocksProxyHttpContext.HTTP_SOCKS_PROXY_RULE);
      if (proxyRule != null) {
        if (logger.isDebugEnabled()) {
          logger.debug("open socket using proxy: " + proxyRule.getProxy());
        }
        return new Socket(proxyRule.getJavaNetProxy());
      }
    }
    return new Socket();
  }

  @Override
  public Socket connectSocket(
    final int connectTimeout,
    final Socket socket,
    final HttpHost host,
    final InetSocketAddress remoteAddress,
    final InetSocketAddress localAddress,
    final HttpContext context) throws IOException {
    final Socket sock = socket != null ? socket : createSocket(context);
    if (localAddress != null) {
      sock.bind(localAddress);
    }
    try {
      sock.connect(remoteAddress, connectTimeout);
    } catch (final IOException ex) {
      try {
        sock.close();
      } catch (final IOException ignore) {
      }
      throw ex;
    }
    return sock;
  }

}
