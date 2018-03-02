package version_K;

import java.text.DecimalFormat;

public class Report {
  private static DecimalFormat df = new DecimalFormat("0.0###");
  public String getData(Entity e) {
    return "["+Math.round(e.getX())+","+Math.round(e.getY())+","+e.getRadius()+","+df.format(e.getAngle())+"]";
    /*return "{"+getIntPair("px",e.getX())+","+getIntPair("py",e.getY())
       +","+getPair("r",e.getRadius())+","+getPair("a",e.getAngle())+"}";*/
  }
  protected String getIntPair(String key, double value){
    return "\""+key+"\":"+Math.round(value);
  }
  protected String getPair(String key, double value){
    return "\""+key+"\":"+df.format(value);
  }
  protected String getPair(String key, String value){
    return "\""+key+"\":\""+value+"\"";
  }
  protected String getPair(String key, Boolean value){
    return "\""+key+"\":"+(value?"true":"false")+"";
  }
 
}
