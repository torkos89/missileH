package version_K;

import java.util.LinkedList;

public class MineExplosionUpdate extends Update {
  MineExplosionUpdate(int worldX, int worldY) {
    super(worldX, worldY);
  }

  @Override
  public void update(Entity e, int id, LinkedList<Integer> less, LinkedList<Entity> more){ 
    e.setRadius(e.getRadius()+3);
    if(e.getRadius()>50) less.add(id);  //kill me
  }
}

