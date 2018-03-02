package version_K;

import java.util.LinkedList;

public class AIUpdate extends Update {

  AIUpdate(int worldX, int worldY) {
    super(worldX, worldY);
    
  }
  @Override
  public void update(Entity e,int id, LinkedList<Integer> less,LinkedList<Entity> more){
    AIData data = (AIData)e.getData();
    Entity src = data.getOwner();
    
    if(src == null){
      less.add(id);
      return;
    }
    e.setX(src.getX());
    e.setY(src.getY());

//    double vx = e.getVelX();
//    double vy = e.getVelY();
    
    Close closestObj = new Close();
    double dist = 3000;
    //double maxSpeed = 20;
    for(Entity t : data.getArea()){
      if(t.getX()==e.getX()&&t.getY()==e.getY()//||t.getType().equals("bullet")
          ||t.getType().equals("explosion"))continue;
      double X = Math.abs( e.getX() - t.getX());
      double Y = Math.abs(e.getY() - t.getY());
      double sqr = Math.sqrt(X*X + Y*Y);
      if(sqr < dist){
        closestObj.e = t;
        dist = sqr;
        closestObj.d = dist;
      }
    }
    if(closestObj.e==null)return;
    if(closestObj != null && closestObj.d < 100){
      double x = closestObj.e.getX()-e.getX();
      double y = closestObj.e.getY()-e.getY();
      Entity parent = data.getOwner();
      parent.setVelX(parent.getVelX()+.015*x);
      parent.setVelY(parent.getVelY()+.015*y);
      return;
    }
    dist = 3000;
    Close closeObj = new Close(); 
    for(Entity t : data.getArea()){
      if(t.getX()==e.getX()&&t.getY()==e.getY()||t.getType().equals("bullet")||t.getType().equals("explosion")||
          (t.getX()==closestObj.e.getX()&&t.getY()==closestObj.e.getY()))continue;
      double X = Math.abs( e.getX() - t.getX());
      double Y = Math.abs(e.getY() - t.getY());
      double sqr = Math.sqrt(X*X + Y*Y);
      if(sqr < dist){
        closeObj.e = t;
        dist = sqr;
        closeObj.d = dist;
      }
    }

    Entity closest = closestObj.e;
    Entity close = closeObj.e;

    if(close != null&&closestObj.d>100){
      /*
      more.add(new Entity(new ExplosionUpdate(worldX,worldY)).setType("explosion")
          .setX(closest.getX()).setY(closest.getY()).setRadius(4));
      more.add(new Entity(new ExplosionUpdate(worldX,worldY)).setType("explosion")
          .setX(close.getX()).setY(close.getY()).setRadius(4));
    */

     double x = 0;
     if(closest.getX()<close.getX()){
       x = closest.getX()-(closest.getX()-close.getX())/2-e.getX();
     }
     else x = close.getX()-(close.getX()-closest.getX())/2-e.getX();
     double y = 0;
     if(closest.getY()<close.getY()){
       y = closest.getY()-(closest.getY()-close.getY())/2-e.getY();
     }
     else y = close.getY()-(close.getY()-closest.getY())/2-e.getY();
     //more.add(new Entity(new ExplosionUpdate(worldX,worldY)).setType("explosion")
     //    .setX(e.getX()+x).setY(e.getY()+y).setRadius(4));
     
    // closest.getY()-(closest.getY()-close.getY())/2-e.getY();
     Entity parent = data.getOwner();
     parent.setVelX(parent.getVelX()*.9+.01*-x);
     parent.setVelY(parent.getVelY()*.9+.01*-y);
    }
    else if(closest != null){
     // more.add(new Entity(new ExplosionUpdate(worldX,worldY)).setType("explosion")
      //     .setX(closest.getX()).setY(closest.getY()).setRadius(4));
      double x = closest.getX()-e.getX();
      double y = closest.getY()-e.getY();
      Entity parent = data.getOwner();
      parent.setVelX(parent.getVelX()+.015*x);
      parent.setVelY(parent.getVelY()+.015*y);
    }
    
    /*
    if(Math.sqrt(vx*vx+vy*vy)>maxSpeed){
      double atan2 = Math.atan2(vy,vx);
      vx = maxSpeed*Math.cos(atan2);
      vy = maxSpeed*Math.sin(atan2);
    }
    
    e.setVelX(vx);
    e.setVelY(vy);
    
    super.update(e, id, less, more);
     */ 
  }
  private class Close{
    Entity e = null;
    double d = 0;
  }
}
