package com.dcais.aggra.spring;

import com.dcais.aggra.client.HttpClient;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.core.type.filter.TypeFilter;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Set;


public class ClassPathMethodScanner extends ClassPathBeanDefinitionScanner {
  private Class<? extends Annotation> annotationClass;

  private Class<?> methodInterface;

  private HttpClient httpClient;

  public void setHttpClientBeanName(String httpClientBeanName) {
    this.httpClientBeanName = httpClientBeanName;
  }

  private String httpClientBeanName;
  private BeanFactory beanFactory;

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


  public MethodFactoryBean<?> getMethodFactoryBean() {
    return methodFactoryBean;
  }

  public void setMethodFactoryBean(MethodFactoryBean<?> methodFactoryBean) {
    this.methodFactoryBean = methodFactoryBean;
  }

  public HttpClient getHttpClient() {
    return httpClient;
  }

  public void setHttpClient(HttpClient httpClient) {
    this.httpClient = httpClient;
  }

  public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
    this.beanFactory = beanFactory;
  }

  private MethodFactoryBean<?> methodFactoryBean = new MethodFactoryBean<>();

  public ClassPathMethodScanner(BeanDefinitionRegistry registry) {
    super(registry, false);
  }

  /**
   * Configures parent scanner to search for the right interfaces. It can search
   * for all interfaces or just for those that extends a markerInterface or/and
   * those annotated with the annotationClass
   */
  public void registerFilters() {
    boolean acceptAllInterfaces = true;

    // if specified, use the given annotation and / or marker interface
    addIncludeFilter(new AnnotationTypeFilter(this.annotationClass));
    acceptAllInterfaces = false;

    // override AssignableTypeFilter to ignore matches on the actual marker interface
    if (this.methodInterface != null) {
      addIncludeFilter(new AssignableTypeFilter(this.methodInterface) {
        @Override
        protected boolean matchClassName(String className) {
          return false;
        }
      });
      acceptAllInterfaces = false;
    }

    if (acceptAllInterfaces) {
      // default include filter that accepts all classes
      addIncludeFilter(new TypeFilter() {
        @Override
        public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory) throws IOException {
          return true;
        }
      });
    }

    // exclude package-info.java
    addExcludeFilter(new TypeFilter() {
      @Override
      public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory) throws IOException {
        String className = metadataReader.getClassMetadata().getClassName();
        return className.endsWith("package-info");
      }
    });
  }

  /**
   * Calls the parent search that will search and register all the candidates.
   * Then the registered objects are post processed to set them as
   * MapperFactoryBeans
   */
  @Override
  public Set<BeanDefinitionHolder> doScan(String... basePackages) {
    Set<BeanDefinitionHolder> beanDefinitions = super.doScan(basePackages);

    if (beanDefinitions.isEmpty()) {
      logger.warn("No Http RMI interface was found in '" + Arrays.toString(basePackages) + "' package. Please check your configuration.");
    } else {
      processBeanDefinitions(beanDefinitions);
    }

    return beanDefinitions;
  }

  private void processBeanDefinitions(Set<BeanDefinitionHolder> beanDefinitions) {
    GenericBeanDefinition definition;
    for (BeanDefinitionHolder holder : beanDefinitions) {
      definition = (GenericBeanDefinition) holder.getBeanDefinition();

      if (logger.isDebugEnabled()) {
        logger.debug("Creating FactoryBean with name '" + holder.getBeanName()
          + "' and '" + definition.getBeanClassName() + "' Interface");
      }
      // the mapper interface is the original class of the bean
      // but, the actual class of the bean is MapperFactoryBean
//            definition.getConstructorArgumentValues().addGenericArgumentValue(definition.getBeanClassName()); // issue #59
      definition.getPropertyValues().add("methodInterface", definition.getBeanClassName());
      definition.setBeanClass(this.methodFactoryBean.getClass());

      boolean explicitFactoryUsed = false;
      definition.getPropertyValues().add("beanFactory", this.beanFactory);
      definition.getPropertyValues().add("httpClient", new RuntimeBeanReference(httpClientBeanName));
      explicitFactoryUsed = true;
      if (!explicitFactoryUsed) {
        if (logger.isDebugEnabled()) {
          logger.debug("Enabling autowire by type for FactoryBean with name '" + holder.getBeanName() + "'.");
        }
        definition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
    return beanDefinition.getMetadata().isInterface() && beanDefinition.getMetadata().isIndependent();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected boolean checkCandidate(String beanName, BeanDefinition beanDefinition) {
    if (super.checkCandidate(beanName, beanDefinition)) {
      return true;
    } else {
      logger.warn("Skipping FactoryBean with name '" + beanName
        + "' and '" + beanDefinition.getBeanClassName() + "' Interface"
        + ". Bean already defined with the same name!");
      return false;
    }
  }

}
