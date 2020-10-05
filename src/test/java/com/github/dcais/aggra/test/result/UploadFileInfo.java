package com.github.dcais.aggra.test.result;

import lombok.Data;

@Data
public class UploadFileInfo {
  String originalname;
  String fieldname;
  String encoding;
  String mimetype;
  String destination;
  String filename;
  String path;
  Long size;
}
