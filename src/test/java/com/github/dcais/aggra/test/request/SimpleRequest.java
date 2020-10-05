package com.github.dcais.aggra.test.request;


import com.github.dcais.aggra.annotation.HttpAttr;
import com.github.dcais.aggra.annotation.HttpRequest;
import com.github.dcais.aggra.enu.HttpMethod;
import org.springframework.test.context.ContextConfiguration;
import org.testng.annotations.Test;

@Test
@ContextConfiguration(locations = {"classpath:spring_base.xml"})
@HttpRequest
public interface SimpleRequest {
  @HttpAttr(url = "http://www.baidu.com", method = HttpMethod.GET)
  void get();

  @HttpAttr(url = "https://www.baidu.com", method = HttpMethod.GET)
  void sslGet();

}
