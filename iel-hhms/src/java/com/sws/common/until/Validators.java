package com.sws.common.until;

import java.util.regex.Pattern;

public final class Validators
{
  public static boolean isAlphanumeric(String str)
  {
    if (isEmpty(str)) {
      return false;
    }

    String regex = "[a-zA-Z0-9]+";
    return Pattern.matches(regex, str);
  }

  public static boolean isChinaMobile(String str)
  {
    if (isEmpty(str)) {
      return false;
    }

    String regex = "1(3[4-9]|5[089])\\d{8}";
    return Pattern.matches(regex, str);
  }

  public static boolean isChinaPAS(String str)
  {
    if (isEmpty(str)) {
      return false;
    }

    if ((str.startsWith("013")) || (str.startsWith("015"))) {
      return false;
    }

    String regex = "0\\d{9,11}";
    return Pattern.matches(regex, str);
  }

  public static boolean isChinaUnicom(String str)
  {
    if (isEmpty(str)) {
      return false;
    }

    String regex = "1(3[0-3]|5[36])\\d{8}";
    return Pattern.matches(regex, str);
  }

  public static boolean isDate(String str)
  {
    if ((isEmpty(str)) || (str.length() > 10)) {
      return false;
    }

    String[] items = str.split("-");

    if (items.length != 3) {
      return false;
    }

    if ((!(isNumber(items[0], 1900, 9999))) || (!(isNumber(items[1], 1, 12)))) {
      return false;
    }

    int year = Integer.parseInt(items[0]);
    int month = Integer.parseInt(items[1]);

    return isNumber(items[2], 1, 
      DateUtils.getMaxDayOfMonth(year, month - 1));
  }

  public static boolean isDateTime(String str)
  {
    if ((isEmpty(str)) || (str.length() > 20)) {
      return false;
    }

    String[] items = str.split(" ");

    if (items.length != 2) {
      return false;
    }

    return ((isDate(items[0])) && (isTime(items[1])));
  }

  public static boolean isEmail(String str)
  {
    return ((!(isEmpty(str))) && (str.indexOf("@") > 0));
  }

  public static boolean isEmpty(Object[] args)
  {
    return ((args == null) || (args.length == 0) || (
      (args.length == 1) && (args[0] == null)));
  }

  public static boolean isEmpty(String str)
  {
    return ((str == null) || (str.trim().length() == 0));
  }

  public static boolean isIdCardNumber(String str)
  {
    if (isEmpty(str)) {
      return false;
    }

    String regex = "(\\d{14}|\\d{17})(\\d|x|X)";
    return Pattern.matches(regex, str);
  }

  public static boolean isMobile(String str)
  {
    if (isEmpty(str)) {
      return false;
    }

    String regex = "(13\\d{9})|(0\\d{9,11})|(15\\d{9})";

    return ((Pattern.matches(regex, str)) && (!(str.startsWith("013"))) && 
      (!(str.startsWith("015"))));
  }

  public static boolean isNumber(String str)
  {
    if (isEmpty(str)) {
      return false;
    }

    for (int i = 0; i < str.length(); ++i)
      if ((str.charAt(i) > '9') || (str.charAt(i) < '0'))
        return false;


    return true;
  }

  public static boolean isNumber(String str, int min, int max)
  {
    if (!(isNumber(str))) {
      return false;
    }

    int number = Integer.parseInt(str);
    return ((number >= min) && (number <= max));
  }

  public static boolean isNumeric(String str)
  {
    if (isEmpty(str)) {
      return false;
    }

    String regex = "(\\+|-){0,1}(\\d+)([.]?)(\\d*)";
    return Pattern.matches(regex, str);
  }

  public static boolean isNumeric(String str, int fractionNum)
  {
    if (isEmpty(str)) {
      return false;
    }

    String regex = "(\\+|-){0,1}(\\d+)([.]?)(\\d{0," + fractionNum + "})";
    return Pattern.matches(regex, str);
  }

  public static boolean isPhoneNumber(String str)
  {
    if (isEmpty(str)) {
      return false;
    }

    String regex = "(([\\(（]\\d+[\\)）])?|(\\d+[-－]?)*)\\d+";
    return Pattern.matches(regex, str);
  }

  public static boolean isPostcode(String str)
  {
    if (isEmpty(str)) {
      return false;
    }

    return ((str.length() == 6) && (isNumber(str)));
  }

  public static boolean isString(String str, int minLength, int maxLength)
  {
    if (str == null) {
      return false;
    }

    if (minLength < 0)
      return (str.length() <= maxLength);

    if (maxLength < 0) {
      return (str.length() >= minLength);
    }

    return ((str.length() >= minLength) && (str.length() <= maxLength));
  }

  public static boolean isTime(String str)
  {
    if ((isEmpty(str)) || (str.length() > 8)) {
      return false;
    }

    String[] items = str.split(":");

    if ((items.length != 2) && (items.length != 3)) {
      return false;
    }

    for (int i = 0; i < items.length; ++i) {
      if ((items[i].length() != 2) && (items[i].length() != 1))
        return false;

    }

    return ((isNumber(items[0], 0, 23)) && (isNumber(items[1], 0, 59)) && (((items.length != 3) || (isNumber(
      items[2], 0, 59)))));
  }

  public static void main(String[] args)
  {
  }
}