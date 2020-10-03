package com.dcais.aggra.converter.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.math.BigDecimal;

public class BigDecimalAdapter extends TypeAdapter<BigDecimal> {
  @Override
  public void write(JsonWriter out, BigDecimal value) throws IOException {
    out.value(value.toString());
  }

  @Override
  public BigDecimal read(JsonReader in) throws IOException {
    String valStr = in.nextString();
    return new BigDecimal(valStr);

  }
}