package com.github.dcais.aggra.converter;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
public class SqResponseHandler implements ResponseHandler<String> {
  List<Header> heads;

  public List<Header> getHeads() {
    return heads;
  }

  public void setHeads(List<Header> heads) {
    this.heads = heads;
  }

  /**
   * Returns the response body as a String if the response was successful (a
   * 2xx status code). If no response body exists, this returns null. If the
   * response was unsuccessful (>= 300 status code), throws an
   * {@link HttpResponseException}.
   */
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
