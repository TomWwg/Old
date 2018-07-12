package com.sws.common.entity;

import java.util.ArrayList;
import java.util.List;

public class OperateResult
{
  private boolean result;
  private String msg;
  private List<String> detailMsg = new ArrayList<String>();

  public OperateResult()
  {
  }

  public OperateResult(boolean result)
  {
    this.result = result;
  }

  public OperateResult(boolean result, String msg)
  {
    this.result = result;
    this.msg = msg;
  }

  public boolean isResult() {
    return this.result;
  }

  public void setResult(boolean result) {
    this.result = result;
  }

  public String getMsg() {
    return this.msg;
  }

  public void setMsg(String msg) {
    this.msg = msg;
  }

  public List<String> getDetailMsg() {
    return this.detailMsg;
  }

  public void setDetailMsg(List<String> detailMsg) {
    this.detailMsg = detailMsg;
  }

  public void addDetailMsg(String detailMsg) {
    this.detailMsg.add(detailMsg);
  }
}
