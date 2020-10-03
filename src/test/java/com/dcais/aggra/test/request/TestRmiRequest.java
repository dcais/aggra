package com.dcais.aggra.test.request;


import com.dcais.aggra.annotation.*;
import com.dcais.aggra.converter.DefaultStringConverter;
import com.dcais.aggra.enu.HttpMethod;
import com.dcais.aggra.test.result.ReturnInfo;

import java.util.Map;

@HttpRequest(url = "")
@ReqHead(keys = {"Class-Head-1", "Class-Head-2", "X-Requested-With", "ConfigHeader"}, values = {"ClassHead1", "ClassHead2", "XMLHttpRequest", "${http.header.configVal}"})
public interface TestRmiRequest {
  @HttpAttr(method = HttpMethod.GET, url = "${test.domain.url}/test/get", timeout = 30000, resConvertClass = DefaultStringConverter.class)
  ReturnInfo urlWithVariablePlaceHold(String a);

  @HttpAttr(method = HttpMethod.GET, url = "{dynamicUrl}")
  String expressInfo(@ReqUrlVariable("dynamicUrl") String url, @ReqConnectionTimeout int connectionTimeout, @ReqTimeout int timeout, @ReqHead Map<String, String> heads);
}
