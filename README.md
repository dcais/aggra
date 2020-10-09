# aggra

## 安装

### maven

```xml
<dependency>
  <groupId>com.github.dcais</groupId>
  <artifactId>aggra</artifactId>
  <version>1.0.1</version>
</dependency>
```

### spring xml bean config
```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd"
       default-autowire="byName">


  <bean id="pccm" class="com.github.dcais.aggra.spring.PoolingHttpClientConnectionManagerFactory">
    <property name="defaultMaxPerRoute" value="50"/>
    <property name="maxTotal" value="500"/>
  </bean>

  <bean id="proxyRule" class="com.github.dcais.aggra.common.ProxyRule">
    <property name="proxy" value="${test.proxy.ip}"/>
    <property name="useProxy" value="${test.proxy.on}"/>
    <property name="forceUseLocalProxy" value="${test.proxy.on.force}"/>
    <property name="closeWhenProxy" value="${test.proxy.on.closeWhenProxy}"/>
    <property name="proxyUrlRegsExclude">
      <list>
        <value>${test.proxy.exclude}</value>
      </list>
    </property>
  </bean>

  <bean class="com.github.dcais.aggra.logger.BaseLoggerImpl"/>
  <bean class="com.github.dcais.aggra.logger.NoLoggerImpl"/>

  <bean id="httpClientRmi" class="com.github.dcais.aggra.client.HttpClient">
    <property name="pccm" ref="pccm"/>
  </bean>

  <bean class="com.github.dcais.aggra.spring.MethodScannerConfigurer">
    <property name="basePackage" value="com.github.dcais.aggra.request com.github.dcais.aggra.test.request"/>
    <property name="annotationClass" value="com.github.dcais.aggra.annotation.HttpRequest"/>
    <property name="httpClientBeanName" value="httpClientRmi"/>
  </bean>

</beans>

```




## 使用说明

###  指定httpMethod，例如发送一个httpGet请求

``` java
@HttpRequest
public interface SimpleRequest {
  @HttpAttr(url = "https://www.baidu.com", method = HttpMethod.GET)
  String get()
}
```

#### get 请求添加参数

```java
@HttpRequest
public interface SimpleRequest {
  @HttpAttr(url = "https://www.baidu.com", method = HttpMethod.GET)
  String get(@ReqParam p1, @ReqParam p2)
}
```

### 发送一个带有参数的post请求

#### Content-Type: application/x-www-form-urlencoded

``` java
@HttpRequest
public interface SimpleRequest {
  //列举参数
  @HttpAttr(url = "https://www.baidu.com", method = HttpMethod.POST)
  String post(@ReqParam("P1") p1, @ReqParam p2)
}
```

#### Conent-Type: application/json

``` java
@HttpRequest
public interface SimpleRequest {
  //列举参数
  @HttpAttr(url = "https://www.baidu.com", method = HttpMethod.POST)
  String post(@ReqBody SomeEntity entity)
}
```

### 接收返回参数

如果返回参数不是String，将使用注册的converter转换成java object。

``` java
@HttpRequest
public interface SimpleRequest {
  //列举参数
  @HttpAttr(url = "https://www.baidu.com", method = HttpMethod.POST)
  ReturnInfo post(@ReqBody SomeEntity entity)
}
```


### 添加Http头

#### 固定的Http头

```java
@HttpRequest(url="http://localhost:3000/")
public interface SimpleRequest {
  @HttpAttr(method = HttpMethod.GET, url = "get")
  @ReqHead(keys = {"Method-Head-1", "Method-Head-2"}, values = {"MethodHead1", "MethodHead2"})
  ReturnInfo headerTest(@ResponseHeaders List<Header> headers);
}
```

#### Http头中的变量

```java
@HttpRequest(url="http://localhost:3000/")
public interface SimpleRequest {
  @HttpAttr(method = HttpMethod.GET, url = "get")
  ReturnInfo headerTest(@ReqHead("X-Requested-With") String xRequestWith);
}
```

### 接收Http头

```java
@HttpRequest(url="http://localhost:3000/")
public interface SimpleRequest {
  @HttpAttr(method = HttpMethod.GET, url = "get")
  ReturnInfo headerTest(@ResponseHeaders List<Header> headers);
}
```

### 用变量替换url

使用@ReqUrlVariable
用变量替换url中的userId
```java
@HttpRequest(url = "${test.domain.url}/")
public interface TestRmiRequest2 {
  @HttpAttr(method = HttpMethod.GET, url = "${test.domain.url.module}/user/{userId}", timeout = 30000)
  String urlWithVariablePlaceHold(String a, @ReqUrlVariable("userId") Integer userId);
}
```

### 上传文件

使用@ReqFile标签指定需要上传的文件

```java
@HttpRequest(url = "${test.domain.url}/test", logMaxCharCountReq = HttpConstants.LMCC_ALL)
public interface TestRequest {
  @HttpAttr(method = HttpMethod.POST, url = "upload?param=1&msg=hello", logMaxCharCountRes = HttpConstants.LMCC_ALL)
  List<UploadFileInfo> fileTest(@ReqParam("p1") String p1, @ReqFile(GIVE_FILENAME) File file, @ReqFile() File file2, @ReqFile() File file3, @ReqFile() File file4, @ReqFile() File file5);
}
```
