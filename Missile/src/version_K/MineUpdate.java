package version_K;

import java.util.LinkedList;


public class MineUpdate extends Update {
  MineUpdate(int worldX, int worldY) {
    super(worldX, worldY);
  }
  private Entity createMineExplosion(Entity src){
    return new Entity(new MineExplosionUpdate(worldX,worldY),src.getData()).setType("mineExplosion")
        .setX(src.getX()).setY(src.getY()).setRadius(40).setHealth(10).setFaction("green");
  }
  @Override
  public void update(Entity e, int id, LinkedList<Integer>less, LinkedList<Entity>more){
    double x = e.getX();
    double y = e.getY();
    int age = e.getAge();
   // x -= e.getVelX();
   // y -= e.getVelY();
   // if(x<0) x += worldX;
   // else if(x>worldX) x -= worldX;
   // if(y<0) y += worldY;
   // else if(y>worldY) y -= worldY;
    e.setX(x);
    e.setY(y);
    e.setAge(age+1);
    switch(age%10){
    case 0: //e.setColor();
    case 1: 
    case 2: //e.setColor();
    case 3:  
    case 4: e.setColor("#ff0000"); break;
    //case 5:
    default: e.setColor("#800000");
    }
    if(e.getAge()>50*e.getHealth()){
      less.add(id) ;
      more.add(createMineExplosion(e));
    }
  }
}
