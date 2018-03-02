package version_K;

import java.util.LinkedList;

public class Update {
  protected int worldX;
  protected int worldY;
  Update(int worldX,int worldY){
    this.worldX = worldX;
    this.worldY = worldY;
  }
  public void update(Entity e,int id, LinkedList<Integer> less,LinkedList<Entity> more){
    double x = e.getX();
    double y = e.getY();
    x -= e.getVelX();
    y -= e.getVelY();
    if(x<0) x += worldX;
    else if(x>worldX) x -= worldX;
    if(y<0) y += worldY;
    else if(y>worldY) y -= worldY;
    e.setX(x);
    e.setY(y);
    
  }
  protected Entity createBullet(Entity src, double xOffset, double yOffset , double angle, int health){
    double angleX = Math.sin(-angle);   
    double angleY = Math.cos(angle);
    
    return new Entity(new BulletUpdate(worldX,worldY),new Data().setOwner(src),new BulletReport()).setType("bullet")
        .setX(src.getX()+xOffset).setY(src.getY()+yOffset).setAngle(angle)
        .setVelX(src.getVelX()+angleX*20).setVelY(src.getVelY()+angleY*20).setRadius(5).setColor("LightGreen").setHealth(health)
        .setFaction(src.getFaction());
  }
}
