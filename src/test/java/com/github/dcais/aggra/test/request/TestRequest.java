package com.github.dcais.aggra.test.request;


import com.github.dcais.aggra.annotation.*;

import com.github.dcais.aggra.cons.HttpConstants;
import com.github.dcais.aggra.enu.HttpMethod;
import com.github.dcais.aggra.test.result.Result;
import com.github.dcais.aggra.test.result.ReturnInfo;
import com.github.dcais.aggra.test.result.UploadFileInfo;
import com.github.dcais.aggra.test.result.tabs.TopicTabsView;
import org.apache.http.Header;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Map;

@HttpRequest(url = "${test.domain.url}/test", logMaxCharCountReq = HttpConstants.LMCC_ALL)
@ReqHead(keys = {"Class-Head-1", "Class-Head-2", "X-Requested-With", "ConfigHeader"}, values = {"ClassHead1", "ClassHead2", "XMLHttpRequest", "${http.header.configVal}"})
public interface TestRequest {
  String TEST_HEADER_NAME = "test-head-name";
  String TEST_HEADER_VALUE = "test-head-value";

  @HttpAttr(method = HttpMethod.GET, url = "get", logMaxCharCountRes = HttpConstants.LMCC_ALL)
  @ReqHead(keys = {"Method-Head-1", "Method-Head-2"}, values = {"MethodHead1", "MethodHead2"})
  String basicTest(@ReqParam("p1") String p1, String p2, @ReqSecure @ReqParam("like") String p3, @ReqHead("X-Requested-With") String xRequestWith);

  @HttpAttr(method = HttpMethod.GET, url = "get")
  @ReqHead(keys = {TEST_HEADER_NAME}, values = {TEST_HEADER_VALUE})
  ReturnInfo headerTest(@ResponseHeaders List<Header> headers);

  @HttpAttr(method = HttpMethod.GET, url = "get", logMaxCharCountReq = HttpConstants.LMCC_ALL)
  @ReqHead(keys = {"Method-Head-1", "Method-Head-2"}, values = {"MethodHead1", "MethodHead2"})
  void testReturnVoid(@ReqParam("p1") String p1, String p2, @ReqSecure @ReqParam("like") String p3, @ReqHead("X-Requested-With") String xRequestWith);

  //多个参数放在一个Map里
  @HttpAttr(method = HttpMethod.POST, url = "post", logMaxCharCountReq = HttpConstants.LMCC_ALL)
  @ReqHead(keys = {"Method-Head-1", "Method-Head-2"}, values = {"MethodHead1", "MethodHead2"})
  String testReturnVoidWithSigleParams(@ReqParam Map<String, Object> aComplexPara, @ReqSecure @ReqParam("like") String p3, @ReqHead("X-Requested-With") String xRequestWith);


  @HttpAttr(method = HttpMethod.GET, url = "get")
  @ReqHead(keys = {"Method-Head-1", "Method-Head-2"}, values = {"MethodHead1", "MethodHead2"})
  ReturnInfo objectResultTest(@ReqParam("like") String like);

  @HttpAttr(method = HttpMethod.POST, url = "post")
  String postRequestBody(@ReqBody TopicTabsView ttv);

  @HttpAttr(method = HttpMethod.POST, url = "post")
  String headerParam0(@ReqHead("single") String headValue);

  @HttpAttr(method = HttpMethod.POST, url = "post")
  String headerParam1(@ReqHead() Map<Object, Object> heads);

  @HttpAttr(method = HttpMethod.GET, url = "result")
  Result<String> resultStringTestPost(@ReqParam("success") Boolean success, @ReqParam("data") String data);

  @HttpAttr(method = HttpMethod.GET, url = "result")
  Result<Date> resultStringTestPost(@ReqParam("success") Boolean success, @ReqParam("data") Date data);


  @HttpAttr(method = HttpMethod.POST, url = "post")
  String postMultipart(@ReqParam("p1") String p1, @ReqParam("p2") String p2);

  @HttpAttr(method = HttpMethod.POST, url = "post")
  void voidResult(@ReqParam("p1") String p1, @ReqParam("p2") String p2);

  static final String GIVE_FILENAME = "fileName";

  @HttpAttr(method = HttpMethod.POST, url = "upload?param=1&msg=hello", logMaxCharCountRes = HttpConstants.LMCC_ALL)
  List<UploadFileInfo> fileTest(@ReqParam("p1") String p1, @ReqFile(GIVE_FILENAME) File file, @ReqFile() File file2, @ReqFile() File file3, @ReqFile() File file4, @ReqFile() File file5);

  @HttpAttr(method = HttpMethod.GET, url = "get?msg=hello")
  ReturnInfo queryStringGet(@ReqParam("like") String like);

  @HttpAttr(method = HttpMethod.POST, url = "post?msg=hello")
  ReturnInfo queryStringPost(@ReqParam("like") String like);

  @HttpAttr(method = HttpMethod.POST, url = "post?msg=hello")
  ReturnInfo queryStringBodyPost(@ReqBody String like);

  @HttpAttr(method = HttpMethod.GET, url = "timeout")
  ReturnInfo timeoutNormal(String a);

  @HttpAttr(method = HttpMethod.GET, url = "timeout", timeout = 2000)
  ReturnInfo timeout2S(String a);

  @HttpAttr(method = HttpMethod.GET, url = "timeout", timeout = 10000)
  ReturnInfo timeout10S(String a);

  @HttpAttr(method = HttpMethod.GET, url = "timeout", timeout = 30000)
  ReturnInfo timeout30S(String a);

  @HttpAttr(method = HttpMethod.GET, url = "timeout")
  ReturnInfo timeout30S2(String a, @ReqTimeout Integer timeout, @ReqConnectionTimeout Integer connTimeout);

  @HttpAttr(method = HttpMethod.POST, url = "post", logMaxCharCountRes = HttpConstants.LMCC_UNKNOW)
  void logTest(@ReqParam("p1") String p1, @ReqParam("p2") String p2);

  @HttpAttr(method = HttpMethod.POST, url = "post", logMaxCharCountRes = HttpConstants.LMCC_ALL)
  void logTest1(@ReqParam("p1") String p1, @ReqParam("p2") String p2);

  @HttpAttr(method = HttpMethod.POST, url = "post", logMaxCharCountRes = -90)
  void logTest2(@ReqParam("p1") String p1, @ReqParam("p2") String p2);

  @HttpAttr(method = HttpMethod.POST, url = "post", logMaxCharCountRes = HttpConstants.LMCC_UNKNOW)
  void logTest3(@ReqParam("p1") String p1, @ReqParam("p2") String p2);

  @HttpAttr(method = HttpMethod.POST, url = "post", logMaxCharCountReq = 0)
  void logTest4(@ReqParam("p1") String p1, @ReqParam("p2") String p2);

  @HttpAttr(method = HttpMethod.POST, url = "post", logMaxCharCountRes = 3, logMaxCharCountReq = 3)
  void logTest5(@ReqParam("p1") String p1, @ReqParam("p2") String p2);

  @HttpAttr(method = HttpMethod.POST, url = "post")
  void logTest6(@ReqParam("p1") String p1, @ReqParam("p2") String p2);

  @HttpAttr(method = HttpMethod.POST, url = "post", logMaxCharCountRes = 0, logMaxCharCountReq = 0)
  void logTest7(@ReqParam("p1") String p1, @ReqParam("p2") String p2);


  String NAME_ORDER = "P3,p2,P2,p1";

  @HttpAttr(method = HttpMethod.GET, url = "get")
  @ReqParamOrder(NAME_ORDER)
  String paramOrder4get(@ReqParam("p1") String p1, @ReqParam("p2") String p2);

  @HttpAttr(method = HttpMethod.GET, url = "get")
  @ReqParamOrder(NAME_ORDER)
  String paramOrder4get(@ReqParam("p1") String p1, @ReqParam("p2") String p2, @ReqParam Map<String, Object> aComplexPara);


}
