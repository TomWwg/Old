package com.sws.mq.comsumer;


public class QueueDataDto {

    int type;
    String dataXml;


    QueueDataDto(int type, String dataXml)
    {
      this.type = type;
      this.dataXml = dataXml;
    }

    public int getType() {
      return this.type;
    }
    public void setType(int type) {
      this.type = type;
    }
    public String getDataXml() {
      return this.dataXml;
    }
    public void setDataXml(String dataXml) {
      this.dataXml = dataXml;
    }

}
