package com.dcais.aggra.request;

import com.dcais.aggra.annotation.*;
import com.dcais.aggra.enu.HttpMethod;

import java.util.Map;

@HttpRequest
@Deprecated
public interface DynamicRequest {

  @HttpAttr(method = HttpMethod.POST, url = "{dynamicUrl}")
  String commonPostRequest(@ReqUrlVariable("dynamicUrl") String url, @ReqBody Object objParam);

  @HttpAttr(method = HttpMethod.GET, url = "{dynamicUrl}")
  String commonGetRequest(@ReqUrlVariable("dynamicUrl") String url);

  @HttpAttr(method = HttpMethod.POST, url = "{dynamicUrl}")
  String commonPostRequest(@ReqUrlVariable("dynamicUrl") String url, @ReqBody Object objParam, @ReqConnectionTimeout int connectionTimeout, @ReqTimeout int timeout);

  @HttpAttr(method = HttpMethod.POST, url = "{dynamicUrl}")
  String commonPostRequest(@ReqUrlVariable("dynamicUrl") String url, @ReqBody Object objParam, @ReqHead Map<String, String> heads);

  @HttpAttr(method = HttpMethod.GET, url = "{dynamicUrl}")
  String commonGetRequest(@ReqUrlVariable("dynamicUrl") String url, @ReqConnectionTimeout int connectionTimeout, @ReqTimeout int timeout, @ReqHead Map<String, String> heads);

  @HttpAttr(method = HttpMethod.GET, url = "{dynamicUrl}")
  String commonGetRequest(@ReqUrlVariable("dynamicUrl") String url, @ReqConnectionTimeout int connectionTimeout, @ReqTimeout int timeout);

  @HttpAttr(method = HttpMethod.POST, url = "{dynamicUrl}")
  String commonPostRequest(@ReqUrlVariable("dynamicUrl") String url, @ReqBody Object objParam, @ReqConnectionTimeout int connectionTimeout, @ReqTimeout int timeout, @ReqHead Map<String, String> heads);

}
