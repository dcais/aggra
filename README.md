# aggra




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