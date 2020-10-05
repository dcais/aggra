package com.github.dcais.aggra.test.request;


import com.github.dcais.aggra.annotation.HttpAttr;
import com.github.dcais.aggra.annotation.HttpRequest;
import com.github.dcais.aggra.annotation.ReqHead;
import com.github.dcais.aggra.annotation.ReqUrlVariable;
import com.github.dcais.aggra.enu.HttpMethod;

@HttpRequest(url = "${test.domain.url}/")
@ReqHead(keys = {"Class-Head-1", "Class-Head-2", "X-Requested-With"}, values = {"ClassHead1", "ClassHead2", "XMLHttpRequest"})
public interface TestRmiRequest2 {
  @HttpAttr(method = HttpMethod.GET, url = "${test.domain.url.module}/user/{userId}", timeout = 30000)
  String urlWithVariablePlaceHold(String a, @ReqUrlVariable("userId") Integer userId);

  @HttpAttr(method = HttpMethod.GET, url = "${test.domain.url.module}/${non.val}/{userId}", timeout = 30000)
  String urlWithNoneExistVariablePlaceHold(String a, @ReqUrlVariable("userId") Integer userId);


}
