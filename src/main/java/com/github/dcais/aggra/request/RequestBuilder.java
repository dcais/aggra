package com.github.dcais.aggra.request;

import com.github.dcais.aggra.common.ProxyRule;
import com.github.dcais.aggra.common.SimpleNameValuePair;
import com.github.dcais.aggra.converter.StringConverter;
import com.github.dcais.aggra.enu.HttpMethod;
import com.github.dcais.aggra.exception.HttpBuildException;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Consts;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.message.BasicHeader;

import java.io.File;
import java.net.URISyntaxException;
import java.util.*;

public class RequestBuilder<T> {
  private static final String DEFAULT_CONTENT_TYPE_GET = "text/plain";
  private static final String DEFAULT_CONTENT_TYPE_POST = "application/x-www-form-urlencoded";
  private static final String DEFAULT_CONTENT_TYPE_FILE = "multipart/form-data";
  private static final String DEFAULT_CONTENT_TYPE_RAW = "application/json";

  private final static String HEAD_KEY_CONTENTTYPE = "Content-Type";
  private String url;

  private String urlSend;

  private HttpMethod httpMethod;

  private Map<String, Object> params;
  private String paramsOrder;  //参数次序

  private String contentType;

  private Map<String, String> headers;

//    public CookieStore getCookieStore() {
//        return cookieStore;
//    }
//
//    public void setCookieStore(CookieStore cookieStore) {
//        this.cookieStore = cookieStore;
//    }
//
//    private CookieStore cookieStore;

  private List<String> secureParaKeys;

  private Object body;

  private String remark;

  private Map<String, File> uploadFiles;

  private Integer timeout;

  public Integer getRetryCount() {
    return retryCount;
  }

  public void setRetryCount(Integer retryCount) {
    this.retryCount = retryCount;
  }

  private Integer retryCount;

  private Integer connectionTimeout;

  private ProxyRule proxyRule;

  private int logMaxCharCountReq;

  private int logMaxCharCountRes;

  public Class getLoggerClass() {
    return loggerClass;
  }

  public void setLoggerClass(Class loggerClass) {
    this.loggerClass = loggerClass;
  }

  private Class loggerClass;

  public Class getReponseHandlerClass() {
    return reponseHandlerClass;
  }

  private List<Header> responseHeaders;

  public List<Header> getResponseHeaders() {
    return responseHeaders;
  }

  public void setResponseHeaders(List<Header> responseHeaders) {
    this.responseHeaders = responseHeaders;
  }

  public void setReponseHandlerClass(Class reponseHandlerClass) {
    this.reponseHandlerClass = reponseHandlerClass;
  }

  private Class reponseHandlerClass;

  public void setLogMaxCharCountReq(int logMaxCharCountReq) {
    this.logMaxCharCountReq = logMaxCharCountReq;
  }

  public int getLogMaxCharCountReq() {
    return this.logMaxCharCountReq;
  }

  public int getLogMaxCharCountRes() {
    return logMaxCharCountRes;
  }

  public void setLogMaxCharCountRes(int logMaxCharCountRes) {
    this.logMaxCharCountRes = logMaxCharCountRes;
  }

  public void setSecureParaKeys(List<String> secureParaKeys) {
    this.secureParaKeys = secureParaKeys;
  }

  public StringConverter getStringConverter() {
    return stringConverter;
  }

  public void setStringConverter(StringConverter stringConverter) {
    this.stringConverter = stringConverter;
  }

  private StringConverter stringConverter;

  public String getUrl() {
    return url;
  }

  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public Object getBody() {
    return body;
  }

  public void setBody(Object body) {
    this.body = body;
  }

  public HttpMethod getHttpMehod() {
    return httpMethod;
  }

  public void setHttpMehod(HttpMethod httpMethod) {
    this.httpMethod = httpMethod;
  }

  public Integer getTimeout() {
    return timeout;
  }

  public void setTimeout(Integer timeout) {
    this.timeout = timeout;
  }

  public Integer getConnectionTimeout() {
    return connectionTimeout;
  }

  public void setConnectionTimeout(Integer connectionTimeout) {
    this.connectionTimeout = connectionTimeout;
  }

  public String getContentType() {
    if (StringUtils.isNotBlank(this.contentType)) {
      return this.contentType;
    }

    String contentType = DEFAULT_CONTENT_TYPE_GET;

    if (this.getBody() != null) {
      contentType = DEFAULT_CONTENT_TYPE_RAW;
    } else if (this.getUploadFiles() != null
      && this.getUploadFiles().entrySet().size() > 0) {
      contentType = DEFAULT_CONTENT_TYPE_FILE;
    } else {
      if (this.httpMethod == HttpMethod.POST) {
        contentType = DEFAULT_CONTENT_TYPE_POST;
      } else {
        contentType = DEFAULT_CONTENT_TYPE_GET;
      }
    }
    return contentType;
  }

  public void setContentType(String contentType) {
    this.contentType = contentType;
  }

  public Map<String, String> getHeaders() {
    return headers;
  }

  public void setHeaders(Map<String, String> headers) {
    this.headers = headers;
  }

  public ProxyRule getProxyRule() {
    return proxyRule;
  }

  public void setProxyRule(ProxyRule proxyRule) {
    this.proxyRule = proxyRule;
  }

  public Map<String, Object> getParams() {
    return params;
  }

  public void setParams(Map<String, Object> params) {
    this.params = params;
  }

  public String getParamsOrder() {
    return paramsOrder;
  }

  public void setParamsOrder(String paramsOrder) {
    this.paramsOrder = paramsOrder;
  }

  public Map<String, File> getUploadFiles() {
    return uploadFiles;
  }

  public void setUploadFiles(Map<String, File> uploadFiles) {
    this.uploadFiles = uploadFiles;
  }

  public HttpRequestBase build(
    RequestConfig defaultRequestConfig
  ) throws URISyntaxException, HttpBuildException {
    if (StringUtils.isBlank(this.url)) {
      throw new HttpBuildException(String.format("empty url [remark]%s", remark));
    }
    if (this.httpMethod == null
      || this.httpMethod == HttpMethod.UNKOWN) {
      throw new HttpBuildException(String.format("unkown http method.[url]%s[remark]%s", this.url, remark));
    }
    HttpRequestBase req = null;
    if (this.httpMethod == HttpMethod.GET) {
      req = buildRequestGet();
    } else {
      req = buildRequestPost();
    }
    RequestConfig.Builder requestConfigBuilder = RequestConfig.copy(defaultRequestConfig);
    if (this.timeout != null) {
      requestConfigBuilder.setSocketTimeout(timeout);
    }
    if (this.connectionTimeout != null) {
      requestConfigBuilder.setConnectTimeout(connectionTimeout);
    }
    if (this.proxyRule != null) {
      requestConfigBuilder.setProxy(new HttpHost(proxyRule.getProxyHost(), proxyRule.getProxyPort(), proxyRule.getProxySchema()));
    }
    RequestConfig requestConfig = requestConfigBuilder.build();
    req.setConfig(requestConfig);

    urlSend = req.getURI().toString();

    return req;
  }

  private HttpGet buildRequestGet() throws URISyntaxException, HttpBuildException {
    final List<NameValuePair> params = buildQueries();
    URIBuilder uriBuilder = new URIBuilder(this.url);
    if (params != null && params.size() > 0) {
      uriBuilder.setParameters(params);
    }
    HttpGet httpGet = new HttpGet(uriBuilder.build());

    List<BasicHeader> headers = this.buildHeaders();
    for (BasicHeader header : headers) {
      httpGet.addHeader(header);
    }
    httpGet.addHeader(new BasicHeader(HEAD_KEY_CONTENTTYPE, getContentType()));
    return httpGet;
  }


  private HttpPost buildRequestPost() throws URISyntaxException, HttpBuildException {
    final List<NameValuePair> ParamList = buildQueries();
    URIBuilder uriBuilder = new URIBuilder(this.url);
    if (this.body != null && ParamList != null && ParamList.size() > 0) {
      uriBuilder.addParameters(ParamList);
    }

    HttpPost httpPost = new HttpPost(uriBuilder.build());

    List<BasicHeader> headers = this.buildHeaders();
    for (BasicHeader header : headers) {
      httpPost.addHeader(header);
    }

    ContentType contentType = ContentType.create(this.getContentType(), Consts.UTF_8);

    if (this.body != null) {
      if (!httpPost.containsHeader(HEAD_KEY_CONTENTTYPE)) {
        httpPost.addHeader(new BasicHeader(HEAD_KEY_CONTENTTYPE, getContentType()));
      }
      EntityBuilder entityBuilder = EntityBuilder.create();
      entityBuilder.setContentType(contentType);
      if (this.body instanceof String) {
        entityBuilder.setText((String) this.body);
      } else {
        entityBuilder.setText(stringConverter.toString(this.body));
      }
      httpPost.setEntity(entityBuilder.build());
    } else if (this.uploadFiles != null) {
      MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
      multipartEntityBuilder.setCharset(Consts.UTF_8);
      multipartEntityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
//            String boundary = "-------------" + System.currentTimeMillis();
//            multipartEntityBuilder.setBoundary(boundary);

      for (Map.Entry<String, File> entry : uploadFiles.entrySet()) {
        String name = entry.getKey();
        File file = entry.getValue();
        FileBody fileBody = new FileBody(file, ContentType.DEFAULT_BINARY, file.getName());
        multipartEntityBuilder.addPart(name, fileBody);
      }
      if (ParamList.size() > 0) {
        for (NameValuePair param : ParamList) {
          multipartEntityBuilder.addTextBody(param.getName(), param.getValue(), ContentType.create("text/plain", Consts.UTF_8));
        }
      }
      httpPost.setEntity(multipartEntityBuilder.build());
    } else if (ParamList.size() > 0) {
      httpPost.addHeader(new BasicHeader(HEAD_KEY_CONTENTTYPE, getContentType()));
      EntityBuilder entityBuilder = EntityBuilder.create();
      entityBuilder.setContentType(contentType);
      entityBuilder.setParameters(ParamList);
      httpPost.setEntity(entityBuilder.build());
    }
    return httpPost;
  }

  private List<NameValuePair> buildQueries() throws HttpBuildException {
    if (paramsOrder != null) {
      return buildOrderedParams();
    }

    List<NameValuePair> queries = new ArrayList<>();
    if (this.params == null) {
      return queries;
    }
    for (final Map.Entry<String, Object> entry : this.params.entrySet()) {
      Object value = entry.getValue();
      if (value instanceof Collection) {
        addArrayObject(queries, entry.getKey(), (Collection) value);
      } else {
        queries.add(new SimpleNameValuePair(entry.getKey(), entry.getValue()));
      }
    }

    return queries;
  }

  private void addArrayObject(List<NameValuePair> queries, String name, Collection<Object> values) {
    values.forEach(item -> {
      queries.add(new SimpleNameValuePair(name, item));
    });
  }

  private void addArrayObject(List<NameValuePair> queries, String name, Object[] values) {
    Arrays.stream(values).forEach(item -> {
      queries.add(new SimpleNameValuePair(name, item));
    });
  }


  private List<NameValuePair> buildOrderedParams() {
    List<NameValuePair> queries = new ArrayList<>();

    String[] names = paramsOrder.split(",");
    Set<String> allNames = params.keySet();
    Map<String, String> orderNamesMap = new HashMap<>(names.length);
    Map<String, String> allNamesMap = new HashMap<>(allNames.size());
    List<String> allWantedAfterOrder = new ArrayList();

    for (String name : names) {
      String t1 = name.trim();
      String t2 = t1.toLowerCase();
      orderNamesMap.put(t2, t1);
      allWantedAfterOrder.add(t2);
    }

    for (String name : allNames) {
      allNamesMap.put(name.toLowerCase(), name);
    }

    Set<String> otherNames = new HashSet(allNamesMap.keySet());

    otherNames.removeAll(allWantedAfterOrder);

    allWantedAfterOrder.addAll(otherNames);

    for (String name : allWantedAfterOrder) {
      String fieldName = allNamesMap.get(name);
      if (fieldName == null) continue;
      ;
      queries.add(new SimpleNameValuePair(orderNamesMap.containsKey(fieldName) ? orderNamesMap.get(fieldName) : fieldName,
        params.get(fieldName)));
    }
    return queries;
  }

  private List<BasicHeader> buildHeaders() {
    List<BasicHeader> listHeader = new ArrayList<>();
    if (this.headers == null) {
      return listHeader;
    }
    for (Map.Entry<String, String> entry : this.headers.entrySet()) {
      listHeader.add(new BasicHeader(entry.getKey(), entry.getValue()));
    }
    return listHeader;
  }

  public void addUploadFile(String filename, File file) {
    if (this.uploadFiles == null) {
      this.uploadFiles = new HashMap<>();
    }
    this.uploadFiles.put(filename, file);
  }

  public String info() {
    StringBuffer s = new StringBuffer();
    s.append("timeout=").append(timeout);
    s.append(",connectionTimeout=").append(connectionTimeout);
    s.append(",retryCount=").append(retryCount);
    s.append(",proxy=").append(proxyRule == null ? "" : proxyRule.getProxy());
    return s.toString();
  }

  public String toString() {
    StringBuffer stringBuffer = new StringBuffer();

    stringBuffer.append("[url]");
    if (StringUtils.isNotBlank(this.urlSend)) {
      stringBuffer.append(this.urlSend);
    } else if (StringUtils.isNotBlank(this.url)) {
      stringBuffer.append(this.url);
    }

    stringBuffer.append("[method]");
    if (this.httpMethod != null) {
      stringBuffer.append(this.httpMethod.toString());
    }
    stringBuffer.append("[contentType]");
    stringBuffer.append(getContentType());

    if (this.headers != null) {
      stringBuffer.append("[headers]");
      if (stringConverter != null) {
        stringBuffer.append(stringConverter.toString(this.headers));
      } else {
        for (Map.Entry<String, String> entry : this.headers.entrySet()) {
          stringBuffer.append(entry.getKey());
          stringBuffer.append("-");
          stringBuffer.append(entry.getValue());
          stringBuffer.append("|");
        }
      }
    }
    if (this.params != null) {
      stringBuffer.append("[params]");
      int idx = 0;
      for (Map.Entry<String, Object> entry : this.params.entrySet()) {
        stringBuffer.append(entry.getKey());
        stringBuffer.append("-");
        stringBuffer.append(this.secureParaKeys.contains(entry.getKey()) ? "****" : entry.getValue().toString());
        stringBuffer.append("|");
        idx++;
      }
    }
    if (this.body != null) {
      stringBuffer.append("[body]");
      if (stringConverter != null) {
        stringBuffer.append(stringConverter.toString(this.body));
      } else {
        stringBuffer.append(this.body.toString());
      }
    }
    if (this.uploadFiles != null) {
      stringBuffer.append("[uploadFiles]");
      {
        for (Map.Entry<String, File> entry : this.uploadFiles.entrySet()) {
          stringBuffer.append(entry.getKey());
          stringBuffer.append("-");
          stringBuffer.append(entry.getValue().getAbsolutePath());
          stringBuffer.append("|");
        }
      }
    }
    if (StringUtils.isNotBlank(this.remark)) {
      stringBuffer.append("[remark]");
      stringBuffer.append(this.remark);
    }
    return stringBuffer.toString();
  }
}
