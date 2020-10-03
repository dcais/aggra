package com.dcais.aggra.test;

import com.dcais.aggra.test.request.SimpleRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

@Slf4j
@Test
@ContextConfiguration(locations = {"classpath:spring_base.xml"})
public class SimpleTestMain extends AbstractTestNGSpringContextTests {
  @Autowired
  private SimpleRequest simpleRequest;

  @Test
  public void testMain() {
    simpleRequest.get();
    simpleRequest.sslGet();
  }

}
