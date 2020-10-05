package com.github.dcais.aggra.binding;

import com.github.dcais.aggra.client.HttpClient;
import org.springframework.beans.factory.BeanFactory;

import java.lang.reflect.Proxy;

public class MethodProxyFactory<T> {
  private final Class<T> methodInterface;

  public MethodProxyFactory(Class<T> methodInterface) {
    this.methodInterface = methodInterface;
  }

  public T newInstance(MethodProxy<T> methodProxy) {
    return (T) Proxy.newProxyInstance(
      this.methodInterface.getClassLoader(),
      new Class[]{methodInterface},
      methodProxy);
  }

  public T newInstance(HttpClient httpClient, BeanFactory beanFactory) {
    final MethodProxy<T> methodProxy = new MethodProxy<T>(httpClient, beanFactory, this.methodInterface);
    return this.newInstance(methodProxy);
  }
}
