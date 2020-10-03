package com.dcais.aggra.test.result.tabs;

import com.dcais.aggra.test.result.ReqestPage;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;

@Data
public class TopicTabsView {
  private String title;
  private TopicTabsQueryView query;
  private ReqestPage page;
  //private Date gmtCreate;
  private BigDecimal bigDecimal;
  private Long aLong;
  private Integer integer;
  private long a2long;
  private int a2Int;
  private Map<String, String> aMap;
  //private List<Date> aList;
}