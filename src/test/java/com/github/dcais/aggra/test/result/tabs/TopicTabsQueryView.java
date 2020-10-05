package com.github.dcais.aggra.test.result.tabs;

public class TopicTabsQueryView {
  private String isHot;
  private Long categoryId;

  public TopicTabsQueryView() {
  }

  public String getIsHot() {
    return this.isHot;
  }

  public Long getCategoryId() {
    return this.categoryId;
  }

  public void setIsHot(String isHot) {
    this.isHot = isHot;
  }

  public void setCategoryId(Long categoryId) {
    this.categoryId = categoryId;
  }

  public boolean equals(final Object o) {
    if (o == this) return true;
    if (!(o instanceof TopicTabsQueryView)) return false;
    final TopicTabsQueryView other = (TopicTabsQueryView) o;
    if (!other.canEqual((Object) this)) return false;
    final Object this$isHot = this.getIsHot();
    final Object other$isHot = other.getIsHot();
    if (this$isHot == null ? other$isHot != null : !this$isHot.equals(other$isHot)) return false;
    final Object this$categoryId = this.getCategoryId();
    final Object other$categoryId = other.getCategoryId();
    if (this$categoryId == null ? other$categoryId != null : !this$categoryId.equals(other$categoryId))
      return false;
    return true;
  }

  protected boolean canEqual(final Object other) {
    return other instanceof TopicTabsQueryView;
  }

  public int hashCode() {
    final int PRIME = 59;
    int result = 1;
    final Object $isHot = this.getIsHot();
    result = result * PRIME + ($isHot == null ? 43 : $isHot.hashCode());
    final Object $categoryId = this.getCategoryId();
    result = result * PRIME + ($categoryId == null ? 43 : $categoryId.hashCode());
    return result;
  }

  public String toString() {
    return "TopicTabsQueryView(isHot=" + this.getIsHot() + ", categoryId=" + this.getCategoryId() + ")";
  }
}