package com.github.dcais.aggra.spring;

import org.slf4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyResourceConfigurer;
import org.springframework.beans.factory.config.TypedStringValue;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.StringUtils;

import java.lang.annotation.Annotation;
import java.util.Map;

import static org.springframework.util.Assert.notNull;

public class MethodScannerConfigurer implements BeanDefinitionRegistryPostProcessor, InitializingBean, ApplicationContextAware, BeanFactoryAware, BeanNameAware {


  private static final Logger log = org.slf4j.LoggerFactory.getLogger(MethodScannerConfigurer.class);
  private String basePackage;

  private boolean processPropertyPlaceHolders;

  private ApplicationContext applicationContext;

  private String beanName;

  private String httpClientBeanName = "httpClient";

  private Class<?> methodInterface;

  private Class<? extends Annotation> annotationClass;

  private BeanNameGenerator nameGenerator;

  private BeanFactory beanFactory;

  public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
    this.beanFactory = beanFactory;
  }

  public void setHttpClientBeanName(String httpClientBeanName) {
    this.httpClientBeanName = httpClientBeanName;
  }

  public Class<? extends Annotation> getAnnotationClass() {
    return annotationClass;
  }

  public void setAnnotationClass(Class<? extends Annotation> annotationClass) {
    this.annotationClass = annotationClass;
  }

  public Class<?> getMethodInterface() {
    return methodInterface;
  }

  public void setMethodInterface(Class<?> methodInterface) {
    this.methodInterface = methodInterface;
  }

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    log.info("setApplicationContext ");
    this.applicationContext = applicationContext;
  }

  @Override
  public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
    log.info("postProcessBeanDefinitionRegistry ");
    if (this.processPropertyPlaceHolders) {
      processPropertyPlaceHolders();
    }
    ClassPathMethodScanner scanner = new ClassPathMethodScanner(registry);
    scanner.setAnnotationClass(this.annotationClass);
    scanner.setMethodInterface(this.methodInterface);
    scanner.setResourceLoader(this.applicationContext);
    scanner.setBeanNameGenerator(this.nameGenerator);
    scanner.setBeanFactory(this.beanFactory);
    scanner.setHttpClientBeanName(httpClientBeanName);
    scanner.registerFilters();
    scanner.scan(StringUtils.tokenizeToStringArray(this.basePackage, ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS));
  }

  public void setProcessPropertyPlaceHolders(boolean processPropertyPlaceHolders) {
    this.processPropertyPlaceHolders = processPropertyPlaceHolders;
  }

  @Override
  public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
    log.info("postProcessBeanFactory");

  }


  @Override
  public void setBeanName(String name) {
    log.info("setBeanName=" + name);
    this.beanName = name;
  }

  /**
   * Gets beanNameGenerator to be used while running the scanner.
   *
   * @return the beanNameGenerator BeanNameGenerator that has been configured
   * @since 1.2.0
   */
  public BeanNameGenerator getNameGenerator() {
    return nameGenerator;
  }

  /**
   * Sets beanNameGenerator to be used while running the scanner.
   *
   * @param nameGenerator the beanNameGenerator to set
   * @since 1.2.0
   */
  public void setNameGenerator(BeanNameGenerator nameGenerator) {
    this.nameGenerator = nameGenerator;
  }


  @Override
  public void afterPropertiesSet() throws Exception {
    notNull(this.basePackage, "Property 'basePackage' is required");
  }

  /*
   * BeanDefinitionRegistries are called early in application startup, before
   * BeanFactoryPostProcessors. This means that PropertyResourceConfigurers will not have been
   * loaded and any property substitution of this class' properties will fail. To avoid this, find
   * any PropertyResourceConfigurers defined in the context and run them on this class' bean
   * definition. Then update the values.
   */
  private void processPropertyPlaceHolders() {
    Map<String, PropertyResourceConfigurer> prcs = applicationContext.getBeansOfType(PropertyResourceConfigurer.class);

    if (!prcs.isEmpty() && applicationContext instanceof ConfigurableApplicationContext) {
      BeanDefinition mapperScannerBean = ((ConfigurableApplicationContext) applicationContext)
        .getBeanFactory().getBeanDefinition(beanName);

      // PropertyResourceConfigurer does not expose any methods to explicitly perform
      // property placeholder substitution. Instead, create a BeanFactory that just
      // contains this mapper scanner and post process the factory.
      DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
      factory.registerBeanDefinition(beanName, mapperScannerBean);

      for (PropertyResourceConfigurer prc : prcs.values()) {
        prc.postProcessBeanFactory(factory);
      }

      PropertyValues values = mapperScannerBean.getPropertyValues();

      this.basePackage = updatePropertyValue("basePackage", values);
    }
  }

  public String getBasePackage() {
    return basePackage;
  }

  public void setBasePackage(String basePackage) {
    this.basePackage = basePackage;
  }

  private String updatePropertyValue(String propertyName, PropertyValues values) {
    PropertyValue property = values.getPropertyValue(propertyName);

    if (property == null) {
      return null;
    }

    Object value = property.getValue();

    if (value == null) {
      return null;
    } else if (value instanceof String) {
      return value.toString();
    } else if (value instanceof TypedStringValue) {
      return ((TypedStringValue) value).getValue();
    } else {
      return null;
    }
  }
}