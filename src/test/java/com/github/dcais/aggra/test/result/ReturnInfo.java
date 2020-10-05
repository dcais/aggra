package com.github.dcais.aggra.test.result;

import java.util.Map;

public class ReturnInfo {
  Map<String, String> headers;
  Map<String, String> param;
  Object body;
  Map<String, String> file;

  public Map<String, String> getHeaders() {
    return headers;
  }

  public void setHeaders(Map<String, String> headers) {
    this.headers = headers;
  }

  public Map<String, String> getParam() {
    return param;
  }

  public void setParam(Map<String, String> param) {
    this.param = param;
  }

  public Object getBody() {
    return body;
  }

  public void setBody(Object body) {
    this.body = body;
  }

  public Map<String, String> getFile() {
    return file;
  }

  public void setFile(Map<String, String> file) {
    this.file = file;
  }
}
