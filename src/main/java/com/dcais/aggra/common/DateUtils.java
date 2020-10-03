package com.dcais.aggra.common;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class DateUtils {
  public static final String Y_M_D = "yyyy-MM-dd";
  public static final String Y_M = "yyyy-MM";
  public static final String Y = "yyyy";
  public static final String Y_M_D_H = "yyyy-MM-dd HH";
  public static final String Y_M_D_HM = "yyyy-MM-dd HH:mm";
  public static final String Y_M_D_HMS = "yyyy-MM-dd HH:mm:ss";
  public static final String Y_M_D_HMSS = "yyyy-MM-dd HH:mm:ss.SSS";
  public static final String Y_M_D_HMS_CN = "yyyy年M月d日 HH:mm:ss";
  public static final String YMD = "yyyyMMdd";
  public static final String YM = "yyyyMM";
  public static final String D = "dd";
  public static final String MD = "MMdd";
  public static final String YMDHM = "yyyyMMddHHmm";
  public static final String YMDHMS = "yyyyMMddHHmmss";
  public static final String ymd = "yyyy/MM/dd";
  public static final String ymd_HM = "yyyy/MM/dd HH:mm";
  public static final String ymd_HMS = "yyyy/MM/dd HH:mm:ss";
  public static final String YYMMDD = "yyMMdd";
  public static final String HM = "HH:mm";
  public static final String H = "HH";
  public static final String HMS = "HH:mm:ss";
  public static final String Y_POINT_M_POINT_D = "yyyy.MM.dd";
  public static final String Y_POINT_POINT_M_POINT_D_POINT_H_POINT_M_POINT_S = "yyyy.MM.dd.HH.mm.ss";
  public static final String ISO_DATE_TIME = "yyyy-MM-dd'T'HH:mm:ss.SSSZZ";

  public final static Long YEAR_MILLIS = 12 * 30 * 24 * 3600 * 1000L;

  public final static Long MONTH_MILLIS = 30 * 24 * 3600 * 1000L;

  public final static Long DAY_MILLIS = 24 * 3600 * 1000L;

  public final static String DAY_FIRST_TIME = " 00:00:00";

  public final static String DAY_LAST_TIME = " 23:59:59";

  public final static String DAY_FIFTEEN_TIME = " 15:00:00";

  private static final Logger logger = LoggerFactory.getLogger(DateUtils.class);


  /**
   * 获取当前星期几
   * 0表示星期天
   *
   * @param date
   * @return
   */
  public static int getDayOfWeek(Date date) {
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    return cal.get(Calendar.DAY_OF_WEEK) - 1;
  }


  public static Date getStartTimeDate(Date date) {
    return DateUtils.formatToData(DateUtils.dateFormat(date, DateUtils.Y_M_D) + DAY_FIRST_TIME, Y_M_D_HMS);
  }

  public static Date getEndTimeDate(Date date) {
    return DateUtils.formatToData(DateUtils.dateFormat(date, DateUtils.Y_M_D) + DAY_LAST_TIME, Y_M_D_HMS);
  }


  public static Date getFifteenTimeDate(Date date) {
    return DateUtils.formatToData(DateUtils.dateFormat(date, DateUtils.Y_M_D) + DAY_FIFTEEN_TIME, Y_M_D_HMS);
  }

  /**
   * 获取当月第一天
   *
   * @return
   */
  public static Date getMonthFirstDay(Date date, Integer diffMonth) {
    Calendar cal = Calendar.getInstance();//获取当前日期
    cal.setTime(date);
    Integer month = cal.get(Calendar.MONTH);
    cal.set(Calendar.MONTH, month + diffMonth);
    cal.set(Calendar.DAY_OF_MONTH, 1);//设置为1号,当前日期既为本月第一天
    return cal.getTime();

  }

  public static Date getCurMonthFirstDay() {
    return getMonthFirstDay(new Date(), 0);
  }


  public static Date getCurMonthLastDay() {
    return getMonthLastDay(new Date(), 0);
  }


  /**
   * 获取当月最后一天
   *
   * @return
   */
  public static Date getMonthLastDay(Date date, Integer diffMonth) {
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    Integer month = cal.get(Calendar.MONTH);
    cal.set(Calendar.MONTH, month + diffMonth);
    cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
    return cal.getTime();
  }


  /**
   * @param d1
   * @param d2
   * @return 1 d1比d2多1天
   * 0 d1与d2同天
   * －1 d1比d2少1天
   */
  public static Long compareDays(Date d1, Date d2) {
    Long days1 = d1.getTime() / DAY_MILLIS;
    Long days2 = d2.getTime() / DAY_MILLIS;
    return days1 - days2;
  }

  /**
   * @param d1
   * @param d2
   * @return n d2大于d1的天数
   */
  public static Integer diffDays(Date d1, Date d2) {
    Long diffHour = (d2.getTime() - d1.getTime()) / 1000 / 3600;
    return Integer.parseInt(String.valueOf(diffHour / 24)) + 1;
  }

  public static String dateFormat(Date date, String format) {
    if (date == null || format == null) {
      return null;
    }
    DateFormat dateFormat = new SimpleDateFormat(format);
    return dateFormat.format(date);
  }

  public static Date formatToData(String str, String format) {
    if (StringUtils.isBlank(str) || StringUtils.isBlank(format)) {
      return null;
    }
    DateFormat dateFormat = new SimpleDateFormat(format);
    try {
      return dateFormat.parse(str);
    } catch (ParseException e) {
      return null;
    }
  }

  /**
   * @param date
   * @return
   */
  public static String smartFormat(Date date) {
    String dateStr = null;
    if (date == null) {
      dateStr = "";
    } else {
      try {
        dateStr = formatDate(date, Y_M_D_HMS);
        //时分秒
        if (dateStr.endsWith(" 00:00:00")) {
          dateStr = dateStr.substring(0, 10);
        }
        //时分
        else if (dateStr.endsWith("00:00")) {
          dateStr = dateStr.substring(0, 16);
        }
        //秒
        else if (dateStr.endsWith(":00")) {
          dateStr = dateStr.substring(0, 16);
        }
      } catch (Exception ex) {
        throw new IllegalArgumentException("转换日期失败: " + ex.getMessage(), ex);
      }
    }
    return dateStr;
  }

  /**
   * @param text
   * @return
   */
  public static Date smartFormat(String text) {
    Date date = null;
    try {
      if (text == null || text.length() == 0) {
        date = null;
      } else if (text.length() == 10) {
        date = formatStringToDate(text, Y_M_D);
      } else if (text.length() == 13) {
        date = formatStringToDate(text, Y_M_D_H);
      } else if (text.length() == 16) {
        date = formatStringToDate(text, Y_M_D_HM);
      } else if (text.length() == 19) {
        date = formatStringToDate(text, Y_M_D_HMS);
      } else {
        throw new IllegalArgumentException("日期长度不符合要求!");
      }
    } catch (Exception e) {
      throw new IllegalArgumentException("日期转换失败!");
    }
    return date;
  }

  /**
   * 获取当前日期
   *
   * @param format
   * @return
   * @throws Exception
   */
  public static String getNow(String format) throws Exception {
    return formatDate(new Date(), format);
  }

  /**
   * 格式化日期格式
   *
   * @param argDate
   * @param argFormat
   * @return 格式化后的日期字符串
   */
  public static String formatDate(Date argDate, String argFormat) {
    if (argDate == null) {
      throw new RuntimeException("参数[日期]不能为空!");
    }
    if (StringUtils.isEmpty(argFormat)) {
      argFormat = Y_M_D;
    }
    SimpleDateFormat sdfFrom = new SimpleDateFormat(argFormat);
    return sdfFrom.format(argDate).toString();
  }

  /**
   * 把字符串格式化成日期
   *
   * @param argDateStr
   * @param argFormat
   * @return
   */
  public static Date formatStringToDate(String argDateStr, String argFormat) {
    if (argDateStr == null || argDateStr.trim().length() < 1) {
      throw new RuntimeException("参数[日期]不能为空!");
    }
    String strFormat = argFormat;
    if (StringUtils.isEmpty(strFormat)) {
      strFormat = Y_M_D;
      if (argDateStr.length() > 16) {
        strFormat = Y_M_D_HMS;
      } else if (argDateStr.length() > 10) {
        strFormat = Y_M_D_HM;
      }
    }
    SimpleDateFormat sdfFormat = new SimpleDateFormat(strFormat);
    //严格模式
    sdfFormat.setLenient(false);
    try {
      return sdfFormat.parse(argDateStr);
    } catch (ParseException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * 获取当日最晚时间的时间戳
   *
   * @return
   */
  public static Long getTodayLastTimestamp() {
    Date date = new Date();
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    calendar.add(Calendar.DAY_OF_YEAR, 1);
    Date tomorrow = new Date(calendar.getTimeInMillis());

    try {
      String tomorrowStr = formatDate(tomorrow, Y_M_D);
      tomorrowStr += " 00:00:00";
      Date resultDate = formatStringToDate(tomorrowStr, Y_M_D_HMS);
      return resultDate.getTime() / 1000;
    } catch (Exception e) {
      logger.error("getTodayLastTimestamp[" + tomorrow + "]", e);
    }


    return calendar.getTimeInMillis();
  }


  public static Date convertStringToDate(String dateStr) {
    SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    try {
      return f.parse(dateStr);
    } catch (Exception e) {
      logger.error("转化为时间失败", e);
    }
    return null;
  }

  /**
   * {date}日期的{days}天后
   *
   * @param date
   * @param days
   * @return
   */
  public static Date afterNDays(Date date, int days) {

    if (date == null) {
      return null;
    }
    Calendar c = Calendar.getInstance();
    c.setTime(date);
    c.add(Calendar.DATE, days);
    Date resultDate = c.getTime();
    return resultDate;
  }


  public static Date afterNHours(Date date, int hours) {

    if (date == null) {
      return null;
    }
    Calendar c = Calendar.getInstance();
    c.setTime(date);
    c.add(Calendar.HOUR, hours);
    Date resultDate = c.getTime();
    return resultDate;
  }

  /**
   * {date}日期的{days}天后
   *
   * @param dateStr 格式(yyyy-MM-DD HH:mm:ss)
   * @param days
   * @return
   */
  public static Date afterNDays(String dateStr, int days) {

    if (StringUtils.isBlank(dateStr)) {
      return null;
    }
    Date date = convertStringToDate(dateStr);
    Date resultDate = afterNDays(date, days);
    return resultDate;
  }

  /**
   * {date}日期的{days}天前
   *
   * @param date
   * @param days
   * @return
   */
  public static Date lastNDays(Date date, int days) {
    return afterNDays(date, -days);
  }

  /**
   * {date}日期的{days}天前
   *
   * @param dateStr
   * @param days
   * @return
   */
  public static Date lastNDays(String dateStr, int days) {
    return afterNDays(dateStr, -days);
  }

  private static Date getMonthEnd(Date date) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    calendar.add(Calendar.MONTH, 1);
    int index = calendar.get(Calendar.DAY_OF_MONTH);
    calendar.add(Calendar.DATE, (-index));
    return calendar.getTime();
  }


  /**
   * 校验是否符合时间格式
   *
   * @param time
   * @param argFormat
   * @return
   */
  public static boolean isValidTime(String time, String argFormat) {
    try {
      if (argFormat.equals(HM)) {
        int hour = Integer.parseInt(time.substring(0, 2));
        if (hour < 0 || hour > 23)
          return false;
        int minute = Integer.parseInt(time.substring(3, 5));
        if (minute < 0 || minute > 59)
          return false;
      } else if (argFormat.equals(HMS)) {
        int hour = Integer.parseInt(time.substring(0, 2));
        if (hour < 0 || hour > 23)
          return false;
        int minute = Integer.parseInt(time.substring(3, 5));
        if (minute < 0 || minute > 59)
          return false;
        int second = Integer.parseInt(time.substring(6, 8));
        if (second < 0 || second > 59)
          return false;
      }
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
    return true;
  }

  /**
   * 根据格式比较日期
   *
   * @param date1
   * @param date2
   * @param argFormat
   * @return
   */
  public static int compareDate(String date1, String date2, String argFormat) {
    int result = 0;
    try {
      DateFormat dateFormat = new SimpleDateFormat(argFormat);
      Date dt1 = dateFormat.parse(date1);
      Date dt2 = dateFormat.parse(date2);
      if (dt1.getTime() > dt2.getTime()) {
        result = 1;
      } else if (dt1.getTime() < dt2.getTime()) {
        result = -1;
      } else {
        result = 0;
      }
    } catch (Exception e) {
      logger.error("compareTime:" + date1 + "and" + date2, e);
      return result;
    }
    return result;
  }


  public static int dayForWeek(Date date) {
    Calendar c = Calendar.getInstance();
    c.setTime(date);
    int dayForWeek = 0;
    if (c.get(Calendar.DAY_OF_WEEK) == 1) {
      dayForWeek = 7;
    } else {
      dayForWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
    }
    return dayForWeek;
  }

  /**
   * 本方法带有业务逻辑，用于杨梅节活动每期开奖<br/>
   * 距离到2017-06-19 23:59:59（周一晚最后一秒）的周数
   *
   * @param date
   * @return
   */
  public static int originWeekNums(Date date) {

    String originDate = "2017-06-19 23:59:59";
    Date fd = convertStringToDate(originDate);

    long time = date.getTime() - fd.getTime();

    long weeks = time / WEEK_TIMES;
    long left = time % WEEK_TIMES;

    if (left > 0) {
      weeks = weeks + 1;
    }

    return Integer.valueOf(String.valueOf(weeks));
  }

  /**
   * 本方法带有业务逻辑，用于开奖开始时间
   *
   * @param weeks
   * @return
   */
  public static Date startPrizeDate(int weeks) {

    String startDateStr = execPrizeDate(weeks) + " 00:00:00";

    return convertStringToDate(startDateStr);
  }

  /**
   * 本方法带有业务逻辑，用于开奖结束时间
   *
   * @param weeks
   * @return
   */
  public static Date endPrizeDate(int weeks) {

    String startDateStr = execPrizeDate(weeks) + " 23:59:59";

    return convertStringToDate(startDateStr);
  }

  /**
   * 本方法带有业务逻辑，用于计算开奖日期
   *
   * @param weeks
   * @return
   */
  private static String execPrizeDate(int weeks) {

    String originDate = "2017-06-19 23:59:59";
    Date fd = convertStringToDate(originDate);

    //计算出开奖开始时间
    long startTime = fd.getTime() + weeks * WEEK_TIMES;
    Date start = new Date(startTime);

    return DateUtils.dateFormat(start, Y_M_D);
  }

  private static final int WEEK_TIMES = 1000 * 60 * 60 * 24 * 7;

  public static void main(String[] args) {

//        String currentDate = "2017-06-26 00:00:00";
//        Date cd = convertStringToDate(currentDate);
//
//        int weeks = originWeekNums(cd);
//
//        System.out.println("第["+weeks+"]周");
//        System.out.println("开奖时间:"+DateUtils.formatDate(startPrizeDate(weeks), DateUtils.Y_M_D_HMS));
//        System.out.println("结束时间:"+DateUtils.formatDate(endPrizeDate(weeks), DateUtils.Y_M_D_HMS));

//        String currentDate = "2017-08-13 00:00:00";
//        Date cd = convertStringToDate(currentDate);
//
//        System.out.println(DateUtils.getDayOfWeek(cd));
//        System.out.println(dayForWeek(cd));
//        System.out.println(getDayOfWeek(cd));

    String a = "{\"1467120\":\"46\",\"296590\":\"42\",\"296591\":\"42\",\"304188\":\"195\",\"304867\":\"44\",\"320958\":\"48\",\"355605\":\"7.5\",\"358235\":\"28\",\"361700\":\"13.5\",\"363494\":\"18.8\",\"364312\":\"9.5\",\"371476\":\"288\",\"374449\":\"238\",\"374451\":\"85\",\"394421\":\"55\",\"407696\":\"60\",\"409297\":\"440\",\"411144\":\"49\",\"412558\":\"49.9\",\"438878\":\"49.9\",\"462883\":\"19.9\",\"462913\":\"29\",\"462946\":\"35\",\"469873\":\"99.9\",\"473989\":\"59\",\"474492\":\"35\",\"474925\":\"25.9\",\"481714\":\"0.45\",\"482057\":\"285\",\"482849\":\"25\",\"495757\":\"25\",\"500223\":\"189\",\"500361\":\"349\",\"501305\":\"38\",\"514171\":\"3.5\",\"514806\":\"165\",\"515104\":\"99\",\"516458\":\"71.2\",\"529602\":\"29\",\"530182\":\"26\",\"533094\":\"48\",\"543150\":\"13\",\"543898\":\"4.8\",\"543899\":\"0.1\",\"543900\":\"0.1\",\"544575\":\"430\",\"544632\":\"2.4\",\"545661\":\"99\",\"550694\":\"98\",\"550695\":\"70\",\"550698\":\"65\",\"550699\":\"169\",\"550765\":\"68\",\"552491\":\"279\",\"552805\":\"399\",\"552921\":\"399\",\"554044\":\"155\",\"554285\":\"17.6\",\"556774\":\"3.88\",\"559291\":\"14.9\",\"562568\":\"67\",\"562573\":\"85\",\"562589\":\"49\",\"564877\":\"95\",\"565863\":\"160\",\"565864\":\"68\",\"567906\":\"600\",\"568385\":\"19.9\",\"571489\":\"7.9\",\"574294\":\"270\",\"576566\":\"14\",\"578437\":\"270\",\"583631\":\"165\",\"583635\":\"165\",\"584314\":\"199\",\"590159\":\"78\",\"592455\":\"790\",\"592462\":\"790\",\"601202\":\"6.5\",\"601214\":\"8.8\",\"619711\":\"50\",\"623636\":\"88\",\"623685\":\"119\",\"623836\":\"6.9\",\"623982\":\"2.9\",\"629002\":\"84\",\"634340\":\"58\",\"634424\":\"49.9\",\"635327\":\"138\",\"635342\":\"245\",\"635682\":\"49\",\"637639\":\"4.5\",\"638651\":\"1.9\",\"639864\":\"298\",\"639866\":\"129\",\"644354\":\"160\",\"644358\":\"160\",\"644724\":\"68\",\"644979\":\"19.9\",\"646353\":\"19.9\",\"647093\":\"248\",\"647098\":\"78\",\"647109\":\"128\",\"666559\":\"129\",\"672827\":\"79\",\"672829\":\"14.9\",\"674618\":\"9.9\",\"674691\":\"74\",\"679912\":\"6.8\",\"701035\":\"29.9\",\"704431\":\"255\",\"709769\":\"178\",\"712354\":\"198\",\"713983\":\"99\",\"714449\":\"85\",\"715525\":\"169\",\"716714\":\"58\",\"721897\":\"28\",\"722853\":\"6.8\",\"723008\":\"150\",\"732179\":\"9.9\",\"732185\":\"15\",\"732187\":\"5.8\",\"732415\":\"140\",\"734168\":\"129\",\"734352\":\"4.5\",\"736408\":\"4.9\",\"739629\":\"25.9\",\"739630\":\"29.9\",\"739633\":\"28.9\",\"739722\":\"26.9\",\"739734\":\"28.9\",\"739747\":\"25.9\",\"740355\":\"29.9\",\"743628\":\"35\",\"746375\":\"35\",\"746496\":\"19.9\",\"746987\":\"99\",\"747401\":\"29\",\"748929\":\"28\",\"748930\":\"9.9\",\"748931\":\"9.9\",\"759033\":\"29\",\"760439\":\"12.9\",\"760782\":\"145\",\"761117\":\"45\",\"761120\":\"15\",\"761175\":\"99\",\"763057\":\"4.99\",\"763424\":\"44\",\"763762\":\"172\",\"765056\":\"1.99\",\"765314\":\"24.5\",\"765316\":\"24.5\",\"768442\":\"19.9\",\"768764\":\"7.9\",\"769133\":\"39\",\"769682\":\"2099\",\"769717\":\"199\",\"769720\":\"219\",\"770043\":\"499\",\"770050\":\"159\",\"770310\":\"9.99\",\"770602\":\"44\",\"770973\":\"250\",\"773013\":\"128\",\"773895\":\"99\",\"774093\":\"24.9\",\"774590\":\"59.7\",\"774687\":\"99\",\"774704\":\"30\",\"774724\":\"88\",\"775968\":\"23.9\",\"777661\":\"29.9\",\"777668\":\"16\",\"778779\":\"6.99\",\"778798\":\"6.5\",\"781197\":\"288\",\"781198\":\"188\",\"781205\":\"178\",\"781927\":\"45\",\"783133\":\"78\",\"783139\":\"23\",\"783145\":\"55\",\"785003\":\"6.8\",\"785436\":\"6.8\",\"785466\":\"6.8\",\"786220\":\"165\",\"787671\":\"29\",\"787719\":\"64\",\"790061\":\"27\",\"790069\":\"30\",\"791337\":\"9.8\",\"791349\":\"9.8\",\"791400\":\"12.8\",\"792103\":\"69.9\",\"792109\":\"142\",\"792110\":\"69.9\",\"792111\":\"89\",\"794555\":\"230\",\"794910\":\"4.5\",\"794911\":\"4.5\",\"795752\":\"65\",\"795755\":\"185\",\"795764\":\"260\",\"798437\":\"15\",\"800127\":\"13\",\"814103\":\"9.9\",\"818739\":\"108\",\"822576\":\"36\",\"826138\":\"32\",\"826343\":\"298\",\"826455\":\"2.9\",\"830619\":\"298\",\"830686\":\"89\",\"831596\":\"39\",\"831599\":\"99\",\"833308\":\"4.9\",\"833503\":\"119\",\"833532\":\"9.6\",\"833550\":\"9.9\",\"833558\":\"8.8\",\"833572\":\"71\",\"833656\":\"28\",\"838818\":\"30\",\"838926\":\"111\",\"839293\":\"22.5\",\"839526\":\"78\",\"839606\":\"8.8\",\"840438\":\"42\",\"840439\":\"72\",\"840440\":\"103\",\"842355\":\"122\",\"842951\":\"13.5\",\"842959\":\"2.19\",\"842975\":\"188\",\"843754\":\"99\",\"844000\":\"29.9\",\"844259\":\"9.9\",\"844300\":\"35\",\"844305\":\"49.9\",\"844309\":\"1.8\",\"844318\":\"23.8\",\"844406\":\"6.85\",\"844619\":\"146\",\"844879\":\"98\",\"844928\":\"250\",\"846527\":\"8.8\",\"848097\":\"109\",\"848295\":\"22.4\",\"850985\":\"168\",\"851300\":\"70\",\"851716\":\"27.9\",\"851718\":\"68.8\",\"851842\":\"30\",\"852161\":\"28\",\"852275\":\"34\",\"852325\":\"65\",\"854403\":\"268\",\"854944\":\"649\",\"855890\":\"19.9\",\"859618\":\"19.9\",\"860658\":\"15.5\",\"860692\":\"58\",\"860868\":\"89.9\",\"860939\":\"128\",\"863233\":\"29.9\",\"863346\":\"158\",\"863787\":\"0.39\",\"863958\":\"328\",\"864094\":\"72\",\"864826\":\"199\",\"865699\":\"170\",\"865884\":\"180\",\"866263\":\"240\",\"866454\":\"240\",\"866460\":\"3\",\"866482\":\"255\",\"866484\":\"75\",\"866583\":\"2.9\",\"866717\":\"8.9\",\"867468\":\"15.8\",\"868313\":\"48\",\"869749\":\"6.8\",\"869771\":\"57\",\"869934\":\"29.9\",\"871642\":\"9.9\",\"871664\":\"4.8\",\"871744\":\"8.8\",\"872650\":\"104\",\"874277\":\"58\",\"876022\":\"38\",\"878375\":\"74\",\"879068\":\"50\",\"879583\":\"188\",\"881784\":\"6.8\",\"882038\":\"39.9\",\"882260\":\"98\",\"882311\":\"98\",\"882348\":\"88\",\"883023\":\"21.9\",\"883476\":\"3\",\"883551\":\"9.9\",\"883854\":\"9.9\",\"883858\":\"9.9\",\"884386\":\"5.5\",\"884532\":\"17.9\",\"884678\":\"39.9\",\"884729\":\"18\",\"885203\":\"18\",\"885207\":\"149\",\"885232\":\"83\",\"885254\":\"165\",\"885274\":\"186\",\"886653\":\"98\",\"886656\":\"13.9\",\"886682\":\"72\",\"886901\":\"6.99\",\"887048\":\"49.9\",\"887329\":\"41.9\",\"887472\":\"5.5\",\"887777\":\"58\",\"887909\":\"3.5\",\"889858\":\"104\",\"889921\":\"98\",\"889922\":\"88\",\"892585\":\"3\",\"892697\":\"6.3\",\"90485 \":\"4.5\"}";
    Map<String, String> b = new Gson().fromJson(a, new TypeToken<Map<String, String>>() {
    }.getType());
    System.out.println(new Gson().toJson(b));
  }

  public static void dd(BigDecimal b) {
    b = BigDecimal.ZERO;
  }
}
