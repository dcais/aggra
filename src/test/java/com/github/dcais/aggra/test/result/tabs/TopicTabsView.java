package com.github.dcais.aggra.test.result.tabs;

import com.github.dcais.aggra.test.result.ReqestPage;

import java.math.BigDecimal;
import java.util.Map;

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

  public TopicTabsView() {
  }

  public String getTitle() {
    return this.title;
  }

  public TopicTabsQueryView getQuery() {
    return this.query;
  }

  public ReqestPage getPage() {
    return this.page;
  }

  public BigDecimal getBigDecimal() {
    return this.bigDecimal;
  }

  public Long getALong() {
    return this.aLong;
  }

  public Integer getInteger() {
    return this.integer;
  }

  public long getA2long() {
    return this.a2long;
  }

  public int getA2Int() {
    return this.a2Int;
  }

  public Map<String, String> getAMap() {
    return this.aMap;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public void setQuery(TopicTabsQueryView query) {
    this.query = query;
  }

  public void setPage(ReqestPage page) {
    this.page = page;
  }

  public void setBigDecimal(BigDecimal bigDecimal) {
    this.bigDecimal = bigDecimal;
  }

  public void setALong(Long aLong) {
    this.aLong = aLong;
  }

  public void setInteger(Integer integer) {
    this.integer = integer;
  }

  public void setA2long(long a2long) {
    this.a2long = a2long;
  }

  public void setA2Int(int a2Int) {
    this.a2Int = a2Int;
  }

  public void setAMap(Map<String, String> aMap) {
    this.aMap = aMap;
  }

  public boolean equals(final Object o) {
    if (o == this) return true;
    if (!(o instanceof TopicTabsView)) return false;
    final TopicTabsView other = (TopicTabsView) o;
    if (!other.canEqual((Object) this)) return false;
    final Object this$title = this.getTitle();
    final Object other$title = other.getTitle();
    if (this$title == null ? other$title != null : !this$title.equals(other$title)) return false;
    final Object this$query = this.getQuery();
    final Object other$query = other.getQuery();
    if (this$query == null ? other$query != null : !this$query.equals(other$query)) return false;
    final Object this$page = this.getPage();
    final Object other$page = other.getPage();
    if (this$page == null ? other$page != null : !this$page.equals(other$page)) return false;
    final Object this$bigDecimal = this.getBigDecimal();
    final Object other$bigDecimal = other.getBigDecimal();
    if (this$bigDecimal == null ? other$bigDecimal != null : !this$bigDecimal.equals(other$bigDecimal))
      return false;
    final Object this$aLong = this.getALong();
    final Object other$aLong = other.getALong();
    if (this$aLong == null ? other$aLong != null : !this$aLong.equals(other$aLong)) return false;
    final Object this$integer = this.getInteger();
    final Object other$integer = other.getInteger();
    if (this$integer == null ? other$integer != null : !this$integer.equals(other$integer)) return false;
    if (this.getA2long() != other.getA2long()) return false;
    if (this.getA2Int() != other.getA2Int()) return false;
    final Object this$aMap = this.getAMap();
    final Object other$aMap = other.getAMap();
    if (this$aMap == null ? other$aMap != null : !this$aMap.equals(other$aMap)) return false;
    return true;
  }

  protected boolean canEqual(final Object other) {
    return other instanceof TopicTabsView;
  }

  public int hashCode() {
    final int PRIME = 59;
    int result = 1;
    final Object $title = this.getTitle();
    result = result * PRIME + ($title == null ? 43 : $title.hashCode());
    final Object $query = this.getQuery();
    result = result * PRIME + ($query == null ? 43 : $query.hashCode());
    final Object $page = this.getPage();
    result = result * PRIME + ($page == null ? 43 : $page.hashCode());
    final Object $bigDecimal = this.getBigDecimal();
    result = result * PRIME + ($bigDecimal == null ? 43 : $bigDecimal.hashCode());
    final Object $aLong = this.getALong();
    result = result * PRIME + ($aLong == null ? 43 : $aLong.hashCode());
    final Object $integer = this.getInteger();
    result = result * PRIME + ($integer == null ? 43 : $integer.hashCode());
    final long $a2long = this.getA2long();
    result = result * PRIME + (int) ($a2long >>> 32 ^ $a2long);
    result = result * PRIME + this.getA2Int();
    final Object $aMap = this.getAMap();
    result = result * PRIME + ($aMap == null ? 43 : $aMap.hashCode());
    return result;
  }

  public String toString() {
    return "TopicTabsView(title=" + this.getTitle() + ", query=" + this.getQuery() + ", page=" + this.getPage() + ", bigDecimal=" + this.getBigDecimal() + ", aLong=" + this.getALong() + ", integer=" + this.getInteger() + ", a2long=" + this.getA2long() + ", a2Int=" + this.getA2Int() + ", aMap=" + this.getAMap() + ")";
  }
  //private List<Date> aList;
}