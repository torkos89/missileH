package version_K;

import java.util.List;

public class AIData extends SpaceshipData{
  private volatile List<Entity> area = null;
  
  public List<Entity> getArea(){
    return area;
  }
  public void setArea(List<Entity> area) {
    this.area = area;
  }

}
