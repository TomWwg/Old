package com.sws.common;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.sws.common.entity.ObjectToJson;
import com.sws.common.entity.OperateResult;


public class AjaxUtil
{
  public static void ajaxWrite(String s)
  {
    PrintWriter pw = null;
    try {
      HttpServletResponse response = ServletActionContext.getResponse();
      response.setContentType("text/html; charset=utf-8");

      pw = response.getWriter();
      pw.write(s);
    } catch (IOException localIOException) {
    } finally {
      if (pw != null)
        pw.close();
    }
  }

  public static void ajaxWrite(boolean b)
  {
    ajaxWrite(b);
  }

  public static void ajaxWrite(boolean result, String msg)
  {
    if (msg == null) {
      msg = "";
    }
    if ((!result) && (msg.trim().length() == 0)) {
      msg = "Ajax operateing faild!";
    }
    ajaxWrite("{\"success\":" + result + ",\"msg\":\"" + msg + "\"}");
  }

  public static void ajaxWrite(OperateResult ajaxResult)
  {
    ajaxWrite(ajaxResult.isResult(), ajaxResult.getMsg());
  }

  public static void ajaxWriteObject(Object o)
  {
    ajaxWrite(ObjectToJson.objectToJson(o));
  }

  public static void ajaxWriteObject(boolean result, Object o)
  {
    ajaxWrite("{\"success\":" + result + ",\"object\":" + ObjectToJson.objectToJson(o) + "}");
  }

  public static void ajaxWriteObject(OperateResult operateResult, Object o)
  {
    boolean success = false;
    String msg = "";
    if (operateResult != null) {
      success = operateResult.isResult();
      msg = operateResult.getMsg();
    }
    ajaxWrite("{\"success\":" + success + ",\"msg\":\"" + msg + "\"" + ",\"object\":" + ObjectToJson.objectToJson(o) + "}");
  }

  public static void ajaxWriteXml(String s) {
    PrintWriter pw = null;
    try {
      HttpServletResponse response = ServletActionContext.getResponse();
      response.setContentType("text/xml; charset=utf-8");

      pw = response.getWriter();
      pw.write(s);
    } catch (IOException localIOException) {
    } finally {
      if (pw != null)
        pw.close();
    }
  }
}
