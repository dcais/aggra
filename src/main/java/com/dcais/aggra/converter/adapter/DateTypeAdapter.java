package com.dcais.aggra.converter.adapter;


import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateTypeAdapter extends TypeAdapter<Date> {
  public final String Y_M_D_HMS = "yyyy-MM-dd HH:mm:ss";
  public final String Y_M_D_HMSS = "yyyy-MM-dd HH:mm:ss.SSS";
  public final int POS_YEAR = "yyyy".length();
  public final int POS_MONTH = "yyyy-MM".length();
  public final int POS_DAY = "yyyy-MM-dd".length();
  public final int POS_HOUR = "yyyy-MM-dd HH".length();
  public final int POS_MINUTE = "yyyy-MM-dd HH:mm".length();
  public final int POS_SEC = "yyyy-MM-dd HH:mm:ss".length();
  public final int POS_MIS_SEC = "yyyy-MM-dd HH:mm:ss.SSS".length();

  @Override
  public void write(JsonWriter out, Date value) throws IOException {
    SimpleDateFormat sdfFrom = new SimpleDateFormat(Y_M_D_HMS);
    out.value(sdfFrom.format(value));

  }

  @Override
  public Date read(JsonReader in) throws IOException {
    String dateStr = in.nextString();
    int strLen = dateStr.length();
    int year = 1970;
    int month = 1;
    int day = 1;
    int hour = 0;
    int minute = 0;
    int sec = 0;
    int misSec = 0;

    if (strLen >= POS_YEAR) {
      year = Integer.parseInt(dateStr.substring(0, POS_YEAR));
      if (strLen >= POS_MONTH) {
        month = Integer.parseInt(dateStr.substring(POS_YEAR + 1, POS_MONTH));
        if (strLen >= POS_DAY) {
          day = Integer.parseInt(dateStr.substring(POS_MONTH + 1, POS_DAY));
          if (strLen >= POS_HOUR) {
            hour = Integer.parseInt(dateStr.substring(POS_DAY + 1, POS_HOUR));
            if (strLen >= POS_MINUTE) {
              minute = Integer.parseInt(dateStr.substring(POS_HOUR + 1, POS_MINUTE));
              if (strLen >= POS_SEC) {
                sec = Integer.parseInt(dateStr.substring(POS_MINUTE + 1, POS_SEC));
                if (strLen >= POS_MIS_SEC) {
                  misSec = Integer.parseInt(dateStr.substring(POS_SEC + 1, POS_MIS_SEC));
                }
              }
            }
          }
        }
      }
    }

    Calendar c = Calendar.getInstance();
    c.set(Calendar.YEAR, year);
    c.set(Calendar.MONTH, month - 1);
    c.set(Calendar.DAY_OF_MONTH, day);
    c.set(Calendar.HOUR_OF_DAY, hour);
    c.set(Calendar.MINUTE, minute);
    c.set(Calendar.SECOND, sec);
    c.set(Calendar.MILLISECOND, misSec);

    return c.getTime();
  }
}
