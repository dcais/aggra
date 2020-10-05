package com.github.dcais.aggra.enu;

public enum HttpMethod {
  POST("post"),
  GET("get"),
  UNKOWN("unkown");
  private final String value;

  HttpMethod(String value) {
    this.value = value;
  }

  public String value() {
    return this.value;
  }
}