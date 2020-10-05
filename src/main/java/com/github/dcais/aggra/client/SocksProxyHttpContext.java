package com.github.dcais.aggra.client;

import com.github.dcais.aggra.common.ProxyRule;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;

public class SocksProxyHttpContext implements HttpContext {
  public static final String HTTP_SOCKS_PROXY_RULE = "socks.proxy.rule";

  public static SocksProxyHttpContext create() {
    return new SocksProxyHttpContext(new BasicHttpContext());
  }

  public static SocksProxyHttpContext adapt(final HttpContext context) {
    Args.notNull(context, "HTTP context");
    if (context instanceof SocksProxyHttpContext) {
      return (SocksProxyHttpContext) context;
    } else {
      return new SocksProxyHttpContext(context);
    }
  }

  private final HttpContext context;

  public SocksProxyHttpContext(final HttpContext context) {
    super();
    this.context = context;
  }

  public SocksProxyHttpContext() {
    super();
    this.context = new BasicHttpContext();
  }

  @Override
  public Object getAttribute(final String id) {
    return context.getAttribute(id);
  }

  @Override
  public void setAttribute(final String id, final Object obj) {
    context.setAttribute(id, obj);
  }

  @Override
  public Object removeAttribute(final String id) {
    return context.removeAttribute(id);
  }

  public <T> T getAttribute(final String attribname, final Class<T> clazz) {
    Args.notNull(clazz, "Attribute class");
    final Object obj = getAttribute(attribname);
    if (obj == null) {
      return null;
    }
    return clazz.cast(obj);
  }

  public void setSocksProxyRule(final ProxyRule proxyRule) {
    setAttribute(HTTP_SOCKS_PROXY_RULE, proxyRule);
  }

  public ProxyRule getSocksProxyRule() {
    return getAttribute(HTTP_SOCKS_PROXY_RULE, ProxyRule.class);
  }

}