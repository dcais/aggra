package com.github.dcais.aggra.test;

import com.github.dcais.aggra.test.request.SimpleRequest;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

@Test
@ContextConfiguration(locations = {"classpath:spring_base.xml"})
public class SimpleTestMain extends AbstractTestNGSpringContextTests {
  private static final Logger log = org.slf4j.LoggerFactory.getLogger(SimpleTestMain.class);
  @Autowired
  private SimpleRequest simpleRequest;

  @Test
  public void testMain() {
    simpleRequest.get();
    simpleRequest.sslGet();
  }

}
