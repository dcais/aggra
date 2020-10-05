package com.github.dcais.aggra.converter;

import java.lang.reflect.Type;

public interface StringConverter {
  String toString(Object o);

  <T> T fromString(String json, Type type);
}
