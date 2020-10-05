package com.github.dcais.aggra.test;

import com.github.dcais.aggra.cons.HttpConstants;
import com.github.dcais.aggra.converter.StringConverter;
import com.github.dcais.aggra.request.DynamicRequest;
import com.github.dcais.aggra.test.request.TestRequest;
import com.github.dcais.aggra.test.request.TestRmiRequest;
import com.github.dcais.aggra.test.request.TestRmiRequest2;
import com.github.dcais.aggra.test.result.Result;
import com.github.dcais.aggra.test.result.ReturnInfo;
import com.github.dcais.aggra.test.result.UploadFileInfo;
import com.github.dcais.aggra.test.result.tabs.TopicTabsQueryView;
import com.github.dcais.aggra.test.result.tabs.TopicTabsView;
import com.google.gson.Gson;
import com.google.gson.internal.StringMap;
import com.google.gson.reflect.TypeToken;
import org.apache.http.Header;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 *
 */
@Test
@ContextConfiguration(locations = {"classpath:spring_base.xml"})
public class TestMain extends AbstractTestNGSpringContextTests {
  private static final Logger log = org.slf4j.LoggerFactory.getLogger(TestMain.class);
  @Autowired
  private TestRequest testRequest;

  @Autowired
  private TestRmiRequest testRmiRequest;

  @Autowired
  private TestRmiRequest2 testRmiRequest2;

  @Autowired
  DynamicRequest dynamicRequest;

  @Autowired
  StringConverter defaultStringConverter;

//    @Autowired
//    private Test2Request test2Request;

  @Test
  public void basicTest() {
    String result = testRequest.basicTest("ab", "cd", "中尉1", "XMLHttpRequest");
    Gson gson = new Gson();
    ReturnInfo r = gson.fromJson(result, new TypeToken<ReturnInfo>() {
    }.getType());
    Assert.assertNotNull(r);
    Assert.assertEquals(r.getHeaders().get("Content-Type".toLowerCase()), "text/plain");
    Assert.assertEquals(r.getHeaders().get("Method-Head-1".toLowerCase()), "MethodHead1");
    Assert.assertEquals(r.getHeaders().get("Method-Head-2".toLowerCase()), "MethodHead2");
    Assert.assertEquals(r.getHeaders().get("X-Requested-With".toLowerCase()), "XMLHttpRequest");
    Assert.assertEquals(r.getParam().get("p1"), "ab");
    Assert.assertEquals(r.getParam().get("p2"), null);
    Assert.assertEquals(r.getParam().get("like"), "中尉1");
    log.info(result);
  }

  @Test
  public void headersTest() {
    List<Header> heads = new ArrayList<>();
    ReturnInfo returnInfo = testRequest.headerTest(heads);
    boolean found = false;
    for (Map.Entry<String,String> entry : returnInfo.getHeaders().entrySet() ) {
      log.info(entry.getKey() + ":" + entry.getValue());
      //一个Header的值
      if (TestRequest.TEST_HEADER_NAME.equals(entry.getKey()) && TestRequest.TEST_HEADER_VALUE.equals(entry.getValue())) {
        found = true;
      }
    }
    Assert.assertTrue(found);
  }

  @Test
  public void testReturnVoid() {
    String para1 = "ab";
    String para2 = "cd";
    testRequest.testReturnVoid("ab", "cd", "中尉1", "XMLHttpRequest");

    Map<String, Object> complexPara = new HashMap<String, Object>();
    complexPara.put("p1", para1);
    complexPara.put("p2", para2);
    String str = testRequest.testReturnVoidWithSigleParams(complexPara, "中尉1", "XMLHttpRequest");
  }

  @Test
  public void objectResultTest() {
    ReturnInfo r = testRequest.objectResultTest("点赞");
    Assert.assertNotNull(r);
    Assert.assertEquals(r.getHeaders().get("Content-Type".toLowerCase()), "text/plain");
    Assert.assertEquals(r.getHeaders().get("Method-Head-1".toLowerCase()), "MethodHead1");
    Assert.assertEquals(r.getHeaders().get("Method-Head-2".toLowerCase()), "MethodHead2");
    Assert.assertEquals(r.getParam().get("like"), "点赞");
    log.info(defaultStringConverter.toString(r));
    Assert.assertNotNull(r);
  }

  @Test
  public void testPostBody() {
    TopicTabsView ttv = new TopicTabsView();
    ttv.setTitle("票提1");
    TopicTabsQueryView queryView = new TopicTabsQueryView();
    queryView.setCategoryId(1001l);
    queryView.setIsHot("N");
    ttv.setQuery(queryView);
    String result = testRequest.postRequestBody(ttv);
    log.info(result);
    Gson gson = new Gson();
    ReturnInfo r = gson.fromJson(result, ReturnInfo.class);
    Assert.assertNotNull(r);
    Assert.assertEquals(r.getHeaders().get("Content-Type".toLowerCase()), "application/json");
    StringMap body = (StringMap) r.getBody();
    Assert.assertEquals(body.get("title"), "票提1");
    StringMap query = (StringMap) body.get("query");
    Assert.assertEquals(query.get("isHot"), "N");

    Double categoryId = (Double) query.get("categoryId");

    Assert.assertTrue(categoryId.equals(new Double(1001)));

  }

  @Test
  public void headParam() {
    String headValue = "sqHeadsValue";
    Assert.assertTrue(testRequest.headerParam0(headValue).contains(headValue), "False");
    Map<Object, Object> heads = new HashMap<>();
    heads.put(HttpConstants.HEAD_KEY_CONTENT_TYPE, HttpConstants.CONTENT_TYPE_TXT_PLAIN);
    heads.put("sqHeads1", "sqHeadsValue1");
    heads.put("sqHeads2", 1);
    heads.put(1000, new Date());
    Assert.assertTrue(testRequest.headerParam1(heads).contains(headValue), "False");
  }

  @Test
  public void testQueryStringget() {
    testRequest.queryStringGet("aa");
  }

  @Test
  public void testQueryStringPost() {
    testRequest.queryStringPost("aa");
  }

  @Test
  public void testQueryStringBodyPost() {
    testRequest.queryStringBodyPost("{\"key\":\"sqmall\"}");
  }

  @Test
  public void testResultStringPost() {
    Result<String> res = testRequest.resultStringTestPost(true, "hello");
  }


  @Test(groups = {"manual"})
  public void testUrlPropValFill() {
    testRmiRequest.urlWithVariablePlaceHold("aa");
  }


  @Test(groups = {"manual"})
  public void testUrlPropValFill2NonVal() {
    boolean ok = false;
    try {
      testRmiRequest2.urlWithNoneExistVariablePlaceHold(null, 1002);
    } catch (Exception e) {
      if (e.getMessage().contains("Could not resolve placeholder 'non.val'")) {
        ok = true;
        return;
      }
      throw e;
    }
    throw new RuntimeException("No Exception expect happens!");

  }

//    @Test(groups = {"manual"})
//    public void noStringConvertPresent(){
//        boolean ok = false;
//        try {
//            testRmiRequest2.noStringConvertre(null, 1002);
//        }catch (Exception e){
//            if(e.getMessage().contains(MethodProxy.NO_STRCVT_EXCEP_MSG)){
//                ok = true;
//                return;
//            }
//            throw e;
//        }
//        throw  new RuntimeException("No Exception expect happens!");
//
//    }




  @Test
  public void postMultipart() {
    String result = testRequest.postMultipart("pv值1", "pv2");
    log.info(result);
    Gson gson = new Gson();
    ReturnInfo r = gson.fromJson(result, ReturnInfo.class);
    Assert.assertNotNull(r);
    Assert.assertEquals(r.getHeaders().get("Content-Type".toLowerCase()), "application/x-www-form-urlencoded");
    StringMap body = (StringMap) r.getBody();
    Assert.assertEquals(body.get("p1"), "pv值1");
    Assert.assertEquals(body.get("p2"), "pv2");
  }

  @Test
  public void voidResult() {
    testRequest.voidResult("pv1", "pv2");
  }

  @Test
  public void voidTimeout2S() {
    try {
      ReturnInfo r = testRequest.timeout2S("aaa");
    } catch (Exception e) {
      log.info("", e);
      ReturnInfo r = testRequest.timeout30S("aaa");
      Gson gson = new Gson();
      log.info("successed:" + gson.toJson(r));
    }
  }

  @Test
  public void testLog() {
    testRequest.logTest("" + HttpConstants.LMCC_UNKNOW, "" + HttpConstants.LMCC_UNKNOW);
    testRequest.logTest1("" + HttpConstants.LMCC_ALL, "" + HttpConstants.LMCC_UNKNOW);
    testRequest.logTest2("" + HttpConstants.LMCC_UNKNOW, "" + "-90");
    testRequest.logTest3("" + HttpConstants.LMCC_UNKNOW, "" + HttpConstants.LMCC_UNKNOW);
    testRequest.logTest4("" + "0", "" + HttpConstants.LMCC_UNKNOW);
    testRequest.logTest5("" + "3", "" + "3");
    testRequest.logTest6("" + HttpConstants.LMCC_UNKNOW, "" + HttpConstants.LMCC_UNKNOW);
    testRequest.logTest7("" + "0", "" + "0");
  }

  @Test
  public void voidTimeout10S() {
    ReturnInfo r = testRequest.timeout10S("aaa");
  }

  @Test
  public void voidTimeout30S() {
    ReturnInfo r = testRequest.timeout30S("aaa");
  }

  @Test
  public void voidTimeout30S2() {
    try {
      ReturnInfo r = testRequest.timeout30S2("aaa", 2000, 2000);
    } catch (Exception e) {
      log.info("", e);
      ReturnInfo r = testRequest.timeout30S2("aaa", 30000, 2000);
      Gson gson = new Gson();
      log.info("successed:" + gson.toJson(r));
    }

  }

  //    @Test
//    public void multiTargetTest(){
//        String r = test2Request.postMultipart("pv1","pv2");
//        log.info(r);
//    }
  @Test
  public void testFileUpload() throws Exception {
    String[] fileName = {"logback.xml", "中文测试文件.txt", "linux.jpg", "google.png"};
    File[] files = {new File(URLDecoder.decode(ClassLoader.getSystemResource(fileName[0]).getFile(), StandardCharsets.UTF_8.name())),
      new File(URLDecoder.decode(ClassLoader.getSystemResource(fileName[1]).getFile(), StandardCharsets.UTF_8.name())),
      new File(URLDecoder.decode(ClassLoader.getSystemResource(fileName[2]).getFile(), StandardCharsets.UTF_8.name())),
      new File(URLDecoder.decode(ClassLoader.getSystemResource(fileName[3]).getFile(), StandardCharsets.UTF_8.name()))
    };

    List<UploadFileInfo> result = testRequest.fileTest("lala啦", files[0], files[0], files[1], files[2], files[3]);
    int count = 0;
    for (UploadFileInfo uploadFileInfo : result) {
      if (TestRequest.GIVE_FILENAME.equals(uploadFileInfo.getFieldname())) {
        continue;
      }
      for (int i = 0; i < files.length; i++) {
        if (files[i].getName().equals(uploadFileInfo.getOriginalname())) {
          //验证文件大小
          Assert.assertEquals(uploadFileInfo.getSize().longValue(), files[i].length());
          log.info(files[i].getName() + " OK！");
          count++;
        }
      }
    }
    Assert.assertEquals(fileName.length, count); //验证文件名个数
  }

  @Test
  public void testExpress() {
    System.out.print(testRmiRequest.expressInfo("http://www.kuaidi100.com/query?type=shentong&postid=3346342356872", 3000, 3000, null));
  }

//    @Test
//    public void testKdn(){
//        String param = "RequestData=%7B%27OrderCode%27%3A%27%27%2C%27ShipperCode%27%3A%27ANE%27%2C%27LogisticCode%27%3A%2730000086155462%27%7D&EBusinessID=1263883&DataType=2&DataSign=NDc5YWY2MDFkMDIyYWUxZTRhNDBlZjA0MWRmODk2YzY%3D&RequestType=1002";
//        log.info("ExpressBirdResult:getResultByApi=======request: "+param);
//        Map<String,String> headers=new HashMap<>();
//        headers.put("Content-Type", "application/x-www-form-urlencoded");
//        String str = dynamicRequest.commonPostRequest("http://api.kdniao.cc/Ebusiness/EbusinessOrderHandle.aspx",param,headers);
//    }


  @Test
  public void thirdPost() {
    testRequest.headerParam0("0");
    testRequest.headerParam0("0");
    testRequest.headerParam0("0");
  }

  @Test
  public void testParamOrder4Get() {
    String res = testRequest.paramOrder4get("1", "2");
    System.out.println(res);
    Map<String, Object> paras = new HashMap();
    paras.put("p3", "3");
    paras.put("p5", "5");
    paras.put("p6", "6");
    paras.put("p8", "8");
    res = testRequest.paramOrder4get("1", "2", paras);
    System.out.println(res);
  }
}
