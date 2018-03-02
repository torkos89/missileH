package version_K;

import java.util.LinkedList;

public class SpaceshipUpdate extends Update{

  SpaceshipUpdate(int worldX, int worldY) {
    super(worldX, worldY);
    
  }

  @Override
  public void update(Entity e, int id, LinkedList<Integer>less, LinkedList<Entity>more) {
    
    double vx = e.getVelX();
    double vy = e.getVelY();
    double maxSpeed = 20;
    
    // update velocity
    boolean engineOn = false;
    if(e.getInput().isUp()){
      engineOn = true;
      vy++;
    }
    if(e.getInput().isDown()){
      engineOn = true;
      vy--;
    }
    if(e.getInput().isLeft()){
      engineOn = true;
     vx++;
    }
    if(e.getInput().isRight()){
      engineOn = true;
      vx--;
    }
    SpaceshipData data = (SpaceshipData)e.getData();
    if(e.getInput().isBrake()){
        vx -= vx*.2; // 1 * Math.sin(-angle);
        vy -= vy*.2; //1 * Math.cos(angle);
    }
    if(Math.sqrt(vx*vx+vy*vy)>maxSpeed){
      double atan2 = Math.atan2(vy,vx);
      vx = maxSpeed*Math.cos(atan2);
      vy = maxSpeed*Math.sin(atan2);
    }
    e.setVelX(vx);
    e.setVelY(vy);
    //if(e.getInput().isMining()){
    //  more.add(createMine(e, data.getUpgrade())); 
    //}
    super.update(e,id,less,more);
    
    data.setEngineOn(engineOn);
    double cos = Math.cos(data.getShipFacing());
    double sin = Math.sin(data.getShipFacing());
    int x = e.getInput().getMouseX();
    int y = e.getInput().getMouseY();
    double rAng = 0;
    double lAng = 0;
    if(e.getInput().isRotate()){
      rAng = data.getrAng()+0.1;
      lAng = data.getlAng()-0.1;
    }else{
     rAng = Math.atan2(x-20*sin-10*cos,y-cos*20-sin*10);
     lAng = Math.atan2(x+20*sin-10*cos,y+cos*20-sin*10);
    }
    data.setrAng(rAng);
    data.setlAng(lAng);
    if(e.getInput()!=null){
      
      //SpaceshipData playerId = (SpaceshipData)e.getSession();
      if(e.getInput().isShooting()&&data.isReloaded()){
        //boolean offset = true;
       // for(int i = 0 ; i<data.getUpgrade(); i++){
         // more.add(createBullet(e,(offset?1:-1)*i/10.0)); 
        //  offset = !offset;
       // }
       
        more.add(createBullet(e, sin*20-10*cos+Math.sin(rAng)*12, 
            -cos*20-sin*10-Math.cos(rAng)*12, rAng , data.getUpgrade())); 
        more.add(createBullet(e,-sin*20-10*cos+Math.sin(lAng)*12, 
            cos*20-sin*10-Math.cos(lAng)*12 , lAng, data.getUpgrade())); 
        data.fire();
        
      }
      if(e.getInput().isMining()&&data.isMineReloaded()){
        more.add(createMine(e));
        data.mineFire();
      }
      //if(e.getInput().isShielding()&&data.isShieldReloaded()){
      //  more.add(createShield(e));
      //  data.shield();
        
     // }
      data.reload();
    }
    //TODO: make data smarter, better weapons 
    
    data.setShipFacing(Math.atan2(vy, vx));
    
    if(!e.getSession().isOpen()) less.add(id);
  }
  /*
  private Entity createBullet(Entity src, double angleOffset){
    double angleX = Math.sin(-src.getAngle()+angleOffset);   
    double angleY = Math.cos(src.getAngle()+angleOffset);
    
    return new Entity(new BulletUpdate(worldX,worldY),new BulletData().setOwner(src)).setType("bullet")
        .setX(src.getX()-angleX*35).setY(src.getY()-angleY*35)
        .setVelX(src.getVelX()+angleX*20).setVelY(src.getVelY()+angleY*20).setRadius(5).setColor("silver").setHealth(1);
  }
*/
  /*
  private Entity createBullet(Entity src, double xOffset, double yOffset , int health){
    double angleX = Math.sin(-src.getAngle());   
    double angleY = Math.cos(src.getAngle());
    
    return new Entity(new BulletUpdate(worldX,worldY),new BulletData().setOwner(src),new BulletReport()).setType("bullet")
        .setX(src.getX()+xOffset).setY(src.getY()+yOffset).setAngle(src.getAngle())
        .setVelX(src.getVelX()+angleX*20).setVelY(src.getVelY()+angleY*20).setRadius(5).setColor("LightGreen").setHealth(health);
  }
  */
  
  
  
  private Entity createMine(Entity src){ 
    
    return new Entity(new MineUpdate(worldX,worldY),new Data().setOwner(src),new MineReport()).setType("mine")
        .setX(src.getX()).setY(src.getY()).setRadius(20).setHealth(1).setFaction("green");
  }

}
