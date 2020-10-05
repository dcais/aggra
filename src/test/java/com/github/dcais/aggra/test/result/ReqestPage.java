package com.github.dcais.aggra.test.result;

public class ReqestPage {
  private Integer pageSize;
  private Integer start;

  public ReqestPage() {
  }

  public Integer getPageSize() {
    return this.pageSize;
  }

  public Integer getStart() {
    return this.start;
  }

  public void setPageSize(Integer pageSize) {
    this.pageSize = pageSize;
  }

  public void setStart(Integer start) {
    this.start = start;
  }

  public boolean equals(final Object o) {
    if (o == this) return true;
    if (!(o instanceof ReqestPage)) return false;
    final ReqestPage other = (ReqestPage) o;
    if (!other.canEqual((Object) this)) return false;
    final Object this$pageSize = this.getPageSize();
    final Object other$pageSize = other.getPageSize();
    if (this$pageSize == null ? other$pageSize != null : !this$pageSize.equals(other$pageSize)) return false;
    final Object this$start = this.getStart();
    final Object other$start = other.getStart();
    if (this$start == null ? other$start != null : !this$start.equals(other$start)) return false;
    return true;
  }

  protected boolean canEqual(final Object other) {
    return other instanceof ReqestPage;
  }

  public int hashCode() {
    final int PRIME = 59;
    int result = 1;
    final Object $pageSize = this.getPageSize();
    result = result * PRIME + ($pageSize == null ? 43 : $pageSize.hashCode());
    final Object $start = this.getStart();
    result = result * PRIME + ($start == null ? 43 : $start.hashCode());
    return result;
  }

  public String toString() {
    return "ReqestPage(pageSize=" + this.getPageSize() + ", start=" + this.getStart() + ")";
  }
}
