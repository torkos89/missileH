package version_K;

import java.util.LinkedList;

public class ShieldUpdate extends Update {

  ShieldUpdate(int worldX, int worldY) {
    super(worldX, worldY);
  }
  @Override
  public void update(Entity e, int id, LinkedList<Integer> less, LinkedList<Entity> more){ 
    //e.setRadius(e.getRadius());
    int shieldHealth = e.getHealth();
    ShieldData data = ((ShieldData)e.getData());
    if(data.isShieldReloaded()){
      e.setHealth(shieldHealth+1);
      data.shield();
    }
    data.reload();
   
    Entity src = data.getOwner();
    if(src == null){
      less.add(id);
      return;
    }
    e.setX(src.getX());
    e.setY(src.getY());
    
    
    e.setRadius(e.getHealth()<=0? 0 : 50);
  }
}
