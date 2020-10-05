package com.github.dcais.aggra.spring;

import org.springframework.context.EmbeddedValueResolverAware;
import org.springframework.stereotype.Component;
import org.springframework.util.StringValueResolver;

@Component("propValFill")
public class PropValFill implements EmbeddedValueResolverAware {
  private StringValueResolver stringValueResolver;

  @Override
  public void setEmbeddedValueResolver(StringValueResolver resolver) {
    stringValueResolver = resolver;
  }

  public String fill(String strVal) {
    if (strVal == null) return strVal;
    strVal = strVal.trim();
    return strVal.contains("${") ? stringValueResolver.resolveStringValue(strVal) : strVal;
  }

}
