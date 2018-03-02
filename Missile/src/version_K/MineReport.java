package version_K;

public class MineReport extends Report{
 
  public String getData(Entity e) {
    return "["+e.getX()+","+e.getY() +","+ e.getRadius()+",\""+ e.getColor()+"\"]";
  }
}
