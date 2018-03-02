package version_K;

public class ShieldReport extends Report {
  public String getData(Entity e) {
    return "["+Math.round(e.getX())+","+Math.round(e.getY())+","+e.getRadius()+","+e.getHealth()+"]";
  }
}
