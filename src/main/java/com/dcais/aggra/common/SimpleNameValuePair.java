package com.dcais.aggra.common;

import org.apache.http.NameValuePair;

import java.util.Date;

public class SimpleNameValuePair implements NameValuePair {
  String name;
  String value;

  public SimpleNameValuePair(String name, Object value) {
    this.name = name;
    if (value instanceof Date) {
      this.value = DateUtils.dateFormat((Date) value, DateUtils.Y_M_D_HMSS);
    } else {
      this.value = value.toString();
    }
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public String getValue() {
    return value;
  }
}
