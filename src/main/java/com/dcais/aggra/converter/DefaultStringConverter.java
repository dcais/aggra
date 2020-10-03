package com.dcais.aggra.converter;

import com.dcais.aggra.converter.adapter.BigDecimalAdapter;
import com.dcais.aggra.converter.adapter.DateTypeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.Date;


@Service("defaultStringConverter")
public class DefaultStringConverter implements StringConverter {

  private Gson getGsonObj() {
    Gson gson = new GsonBuilder()
      .registerTypeAdapter(Date.class, new DateTypeAdapter().nullSafe())
      .registerTypeAdapter(BigDecimal.class, new BigDecimalAdapter().nullSafe())
      .create();
    return gson;
  }

  @Override
  public String toString(Object o) {
    Gson gson = getGsonObj();
    return gson.toJson(o);
  }


  @Override
  public <T> T fromString(String json, Type clazz) {
    Gson gson = getGsonObj();
    return gson.fromJson(json, clazz);
  }
}
