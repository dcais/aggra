package com.github.dcais.aggra.test.result;

public class UploadFileInfo {
  String originalname;
  String fieldname;
  String encoding;
  String mimetype;
  String destination;
  String filename;
  String path;
  Long size;

  public UploadFileInfo() {
  }

  public String getOriginalname() {
    return this.originalname;
  }

  public String getFieldname() {
    return this.fieldname;
  }

  public String getEncoding() {
    return this.encoding;
  }

  public String getMimetype() {
    return this.mimetype;
  }

  public String getDestination() {
    return this.destination;
  }

  public String getFilename() {
    return this.filename;
  }

  public String getPath() {
    return this.path;
  }

  public Long getSize() {
    return this.size;
  }

  public void setOriginalname(String originalname) {
    this.originalname = originalname;
  }

  public void setFieldname(String fieldname) {
    this.fieldname = fieldname;
  }

  public void setEncoding(String encoding) {
    this.encoding = encoding;
  }

  public void setMimetype(String mimetype) {
    this.mimetype = mimetype;
  }

  public void setDestination(String destination) {
    this.destination = destination;
  }

  public void setFilename(String filename) {
    this.filename = filename;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public void setSize(Long size) {
    this.size = size;
  }

  public boolean equals(final Object o) {
    if (o == this) return true;
    if (!(o instanceof UploadFileInfo)) return false;
    final UploadFileInfo other = (UploadFileInfo) o;
    if (!other.canEqual((Object) this)) return false;
    final Object this$originalname = this.getOriginalname();
    final Object other$originalname = other.getOriginalname();
    if (this$originalname == null ? other$originalname != null : !this$originalname.equals(other$originalname))
      return false;
    final Object this$fieldname = this.getFieldname();
    final Object other$fieldname = other.getFieldname();
    if (this$fieldname == null ? other$fieldname != null : !this$fieldname.equals(other$fieldname)) return false;
    final Object this$encoding = this.getEncoding();
    final Object other$encoding = other.getEncoding();
    if (this$encoding == null ? other$encoding != null : !this$encoding.equals(other$encoding)) return false;
    final Object this$mimetype = this.getMimetype();
    final Object other$mimetype = other.getMimetype();
    if (this$mimetype == null ? other$mimetype != null : !this$mimetype.equals(other$mimetype)) return false;
    final Object this$destination = this.getDestination();
    final Object other$destination = other.getDestination();
    if (this$destination == null ? other$destination != null : !this$destination.equals(other$destination))
      return false;
    final Object this$filename = this.getFilename();
    final Object other$filename = other.getFilename();
    if (this$filename == null ? other$filename != null : !this$filename.equals(other$filename)) return false;
    final Object this$path = this.getPath();
    final Object other$path = other.getPath();
    if (this$path == null ? other$path != null : !this$path.equals(other$path)) return false;
    final Object this$size = this.getSize();
    final Object other$size = other.getSize();
    if (this$size == null ? other$size != null : !this$size.equals(other$size)) return false;
    return true;
  }

  protected boolean canEqual(final Object other) {
    return other instanceof UploadFileInfo;
  }

  public int hashCode() {
    final int PRIME = 59;
    int result = 1;
    final Object $originalname = this.getOriginalname();
    result = result * PRIME + ($originalname == null ? 43 : $originalname.hashCode());
    final Object $fieldname = this.getFieldname();
    result = result * PRIME + ($fieldname == null ? 43 : $fieldname.hashCode());
    final Object $encoding = this.getEncoding();
    result = result * PRIME + ($encoding == null ? 43 : $encoding.hashCode());
    final Object $mimetype = this.getMimetype();
    result = result * PRIME + ($mimetype == null ? 43 : $mimetype.hashCode());
    final Object $destination = this.getDestination();
    result = result * PRIME + ($destination == null ? 43 : $destination.hashCode());
    final Object $filename = this.getFilename();
    result = result * PRIME + ($filename == null ? 43 : $filename.hashCode());
    final Object $path = this.getPath();
    result = result * PRIME + ($path == null ? 43 : $path.hashCode());
    final Object $size = this.getSize();
    result = result * PRIME + ($size == null ? 43 : $size.hashCode());
    return result;
  }

  public String toString() {
    return "UploadFileInfo(originalname=" + this.getOriginalname() + ", fieldname=" + this.getFieldname() + ", encoding=" + this.getEncoding() + ", mimetype=" + this.getMimetype() + ", destination=" + this.getDestination() + ", filename=" + this.getFilename() + ", path=" + this.getPath() + ", size=" + this.getSize() + ")";
  }
}
