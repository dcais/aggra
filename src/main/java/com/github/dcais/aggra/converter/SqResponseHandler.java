package com.github.dcais.aggra.converter;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class SqResponseHandler implements ResponseHandler<String> {
  private static final Logger log = org.slf4j.LoggerFactory.getLogger(SqResponseHandler.class);
  List<Header> heads;

  public List<Header> getHeads() {
    return heads;
  }

  public void setHeads(List<Header> heads) {
    this.heads = heads;
  }

  public String handleResponse(final HttpResponse response)
    throws HttpResponseException, IOException {
    if (heads != null) {
      Header[] rHeaders = response.getAllHeaders();
      for (Header aHeader : rHeaders) {
        heads.add(aHeader);
      }
    }
    final StatusLine statusLine = response.getStatusLine();
    final HttpEntity entity = response.getEntity();
    if (statusLine.getStatusCode() >= 300) {
      EntityUtils.consume(entity);
      //log.error("RMI Exception:" + statusLine.getStatusCode() + statusLine.getReasonPhrase());
      throw new HttpResponseException(statusLine.getStatusCode(),
        statusLine.getReasonPhrase());
    }
    return entity == null ? null : EntityUtils.toString(entity, StandardCharsets.UTF_8);
  }
}
