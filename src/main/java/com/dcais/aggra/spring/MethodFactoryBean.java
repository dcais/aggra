package com.dcais.aggra.spring;

import com.dcais.aggra.binding.MethodProxyFactory;
import com.dcais.aggra.client.HttpClient;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.FactoryBean;

public class MethodFactoryBean<T> implements FactoryBean<T> {
  private Class<T> methodInterface;
  private HttpClient httpClient;
  private BeanFactory beanFactory;

  public MethodFactoryBean() {

  }

  @Override
  public T getObject() throws Exception {
    MethodProxyFactory<T> methodProxyFactory = new MethodProxyFactory(methodInterface);
    return methodProxyFactory.newInstance(httpClient, beanFactory);
  }

  @Override
  public Class<?> getObjectType() {
    return this.methodInterface;
  }

  @Override
  public boolean isSingleton() {
    return true;
  }

  public Class<T> getMethodInterface() {
    return methodInterface;
  }

  public void setMethodInterface(Class<T> methodInterface) {
    this.methodInterface = methodInterface;
  }

  public HttpClient getHttpClient() {
    return httpClient;
  }

  public void setHttpClient(HttpClient httpClient) {
    this.httpClient = httpClient;
  }

  public void setBeanFactory(BeanFactory beanFactory) {
    this.beanFactory = beanFactory;
  }
}
