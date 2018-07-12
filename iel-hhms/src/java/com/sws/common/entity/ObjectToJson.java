package com.sws.common.entity;

import java.io.IOException;
import java.io.StringWriter;
import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;

public class ObjectToJson
{
  public static String listToJson(List<?> json)
  {
    return objectToJson(json);
  }

  public static String objectToJson(Object obj)
  {
    StringWriter sw = new StringWriter();
    String result = null;
    try {
      ObjectMapper objectMapper = new ObjectMapper();
      objectMapper.writeValue(sw, obj);
      result = sw.toString();
    } catch (IOException e) {
      e.printStackTrace();
    /*  LogUtils.logException(e);*/

      if (sw != null)
        try {
          sw.flush();
          sw.close();
        } catch (IOException e1) {
         // LogUtils.logException(e);
        }
    }
    finally
    {
      if (sw != null) {
        try {
          sw.flush();
          sw.close();
        } catch (IOException e) {
          /*LogUtils.logException(e);*/
        }
      }
    }
    return result;
  }
}
