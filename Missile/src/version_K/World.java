package version_K;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Date;
import java.util.HashMap;

public class World implements Runnable{
  final Map<Integer, Entity> ENTITIES = new ConcurrentHashMap<>();
  final Map<Integer, Entity> PLAYERS = new ConcurrentHashMap<>();
  final int worldX = 10000;
  final int worldY = 10000;
  int asteroidCount = 0;
  int aiCount = 0;
  int entityMax = 3000;
  int aiMax = 100;
  Map<Integer,Entity> managed = new HashMap<>();
  LinkedList<Entity> more = new LinkedList<>();
  LinkedList<Integer> less = new LinkedList<>();
  //Map<Integer,Entity> green = new ConcurrentHashMap<>();
  //Map<Integer,Entity> red = new ConcurrentHashMap<>();
  //Map<Integer,Entity> neutrals = new LinkedHashMap<>();
  Map<String,HashMap<Integer, Entity>> factions = new HashMap<>();
  private int i = 0;
  World(){ 
    factions.put("green", new HashMap<>());
    factions.put("red", new HashMap<>());
    factions.put("neutral", new HashMap<>());
  }
  public int addEntity(Entity e){
    
    switch(e.getType()){
      case"ship": for(Entity ent : factions.get("neutral").values()){ 
          while(collide(e,ent)){
           e.setX(Math.random()*worldX);
           e.setY(Math.random()*worldY);
          }
        }
        PLAYERS.put(i,e);
        factions.get("green").put(i,e);
        ENTITIES.put(i++, e);
        Entity shield = new Entity(new ShieldUpdate(worldX,worldY),new ShieldData().setOwner(e),new ShieldReport()).setType("shield")
            .setX(e.getX()).setY(e.getY()).setRadius(50).setHealth(5).setFaction("green");
        factions.get("green").put(i,shield);
        ENTITIES.put(i++, shield);
        /*
        Entity pointDefense = new Entity(new AIUpdate(worldX,worldY), new AIData().setOwner(e), new Report())
            .setType("pointDefense").setX(e.getX()).setY(e.getY()).setHealth(10).setFaction("green");
        managed.add(pointDefense);
        
  //      green.put(i, pointDefense);
        ENTITIES.put(i++, pointDefense);
        return i-3;
        */
        return i-2;
      case"bullet": //green.put(i,e);break;
      case"mine": //green.put(i,e);break;
      case"shield": //green.put(i,e);break;
      case"pointDefense": //green.put(i,e);break;
      case"mineExplosion": factions.get(e.getFaction()).put(i,e);break;
      case"aiShip": factions.get("red").put(i,e);
        ENTITIES.put(i++,e);
        Entity driver = new Entity(new AIUpdate(worldX,worldY), new AIData().setOwner(e), new Report())
          .setType("aiDriver").setX(e.getX()).setY(e.getY()).setHealth(10).setFaction("red");
        managed.put(i,driver);
        
        e = driver;break;
      case"asteroid":for(Entity ent : PLAYERS.values()){ 
        while(collide(e,ent)){
          e.setX(Math.random()*worldX) ;
          e.setY(Math.random()*worldY) ;
         }
       } 
      factions.get("neutral").put(i,e);break;
    }
    ENTITIES.put(i++,e);
    return i-1;
  }
    
  public void removeEntity(int id){
    Entity e =  ENTITIES.remove(id);
    factions.get(e.getFaction()).remove(id);
    if(e.getType().equals("aiShip")){
      ENTITIES.remove(id+1);
      managed.remove(id+1);
    }
  }
  public void removePlayer(int id){
    PLAYERS.remove(id);
    //ENTITIES.remove(id+1);
    //TODO add pointDefense removal
  }
  private boolean collide(Entity entA , Entity entB){
   // if(entA.getColor().equals(entB.getColor())) return false;
   // if(entA.getColor().equals("explosion") || entB.getColor().equals("explosion")) return false;
   // if(entA.getType().equals("ship") && entB.getType().equals("bullet")) return false;
    double X = Math.abs( entA.getX() - entB.getX());
    double Y = Math.abs(entA.getY() - entB.getY());
    double R = entA.getRadius() + entB.getRadius();
    if(R < X || R < Y) return false;
    //if(entA.getType().equals("bullet") || entB.getType().equals("bullet"))
    return(Math.sqrt(X*X + Y*Y) < R);
  }
  @Override
  public void run() {
   try{
    Thread.sleep(50);
     while(true){
       
       long timer = new Date().getTime();
       manager(ENTITIES);
       updateLoop(ENTITIES);//, less, more);
       while(less.size()>0){
//         if(ENTITIES.get(less.size()-1).getType().equals("pointDefense")) System.out.println("killed");
//         else System.out.println(ENTITIES.get(less.size()-1).getType());
         removeEntity(less.pop());
       }
       for(Entry<Integer,Entity> g : factions.get("green").entrySet()){
         if(less.contains(g.getKey())) continue;
         Entity e1 = g.getValue();
         for(Entry<Integer,Entity> n : factions.get("neutral").entrySet()){
           if(less.contains(n.getKey())) continue;
           Entity e2 = n.getValue();
           if(collide(e1,e2)){
             int hp = e2.getHealth();
             if(collided(n.getKey(),e2,e1.getHealth(),more)){
                 e1.getData().addXp();
             }
             collided(g.getKey(),e1,hp,more);
             break;
           }
         }
         for(Entry<Integer,Entity> r : factions.get("red").entrySet()){
           if(less.contains(r.getKey())) continue;
           Entity e2 = r.getValue();
           if(collide(e1,e2)){
             int hp = e2.getHealth();
             if(collided(r.getKey(),e2,e1.getHealth(),more)){
                 e1.getData().addXp();
             }
             collided(g.getKey(),e1,hp,more);
             break;
           }
         }
       }
       for(Entry<Integer,Entity> r : factions.get("red").entrySet()){
         if(less.contains(r.getKey())) continue;
         Entity e1 = r.getValue();
         for(Entry<Integer,Entity> n : factions.get("neutral").entrySet()){
           if(less.contains(n.getKey())) continue;
           Entity e2 = n.getValue();
           if(collide(e1,e2)){
             int hp = e2.getHealth();
             if(collided(n.getKey(),e2,e1.getHealth(),more)){
             //    e1.getData().addXp();
             }
             collided(r.getKey(),e1,hp,more);
             break;
           }
         }
         
       }
       while(less.size()>0){
         removeEntity(less.pop());
         
       }
        //more.add(createAiShip(20,500,500,10));
       
       while(entityMax>factions.get("neutral").size()+more.size()){
         more.add(createAsteroid(((int)(Math.random()*3)+1)*10, 1));//(int)(Math.random()*3)+1));
       }
       while(aiMax>factions.get("red").size()+more.size()){
         more.add(createAiShip(20,Math.random()*worldX,Math.random()*worldY,10));
         
       }
       while(more.size()>0){
         addEntity(more.pop());
       }
       sendLoop(ENTITIES, PLAYERS,timer);
       timer = new Date().getTime()-timer;
       if(timer<50)Thread.sleep(50-timer);
     }
   }
   catch(InterruptedException|ConcurrentModificationException|IOException e){
     e.printStackTrace();
   }
  }
    private void manager(Map<Integer, Entity> entities){
      
      // pull entity, get location, set radius, loop neutrals list
      // check size, loop list, build reference, loop neutrals list, check radius, add entity to reference, run ai thing 
      if(managed.size()==0) return;
      List<Tracking> ais = new ArrayList<>(managed.size());
      for(Entity m: managed.values()){
        Tracking ai = new Tracking();
        ai.ai = m;
        ais.add(ai);
      }
      for(Entity e : ENTITIES.values()){
         for(Tracking t : ais){
           if(Math.abs(e.getX()-t.ai.getX())<1000 && Math.abs(e.getY()-t.ai.getY())<1000){
             t.area.add(e);
           }
         }
      }
      for(Tracking t : ais){
        ((AIData)t.ai.getData()).setArea(t.area);
      }
    }
    private void updateLoop(Map<Integer, Entity> entities){//,LinkedList<Integer> less, LinkedList<Entity> more){
      for(Integer id : entities.keySet()){
        entities.get(id).update(id, less, more);    
      }
    }
    
    private Entity createAiShip(int radius,double x,double y,int health){
      //aiCount++;
       return new Entity(new Update(worldX,worldY), new Data(), new Report())
           .setType("aiShip").setX(x).setY(y).setRadius(radius).setHealth(health).setFaction("red");
     }
     
     private Entity createAsteroid(int radius, int health){
       return createAsteroid(radius,Math.random()*worldX,Math.random()*worldY,health);
     }
     
     private Entity createAsteroid(int radius,double x,double y,int health){  
       asteroidCount++;
       return new Entity(new AsteroidUpdate(worldX,worldY),new AsteroidData(radius))
           .setType("asteroid").setX(x).setY(y).setRadius(radius).setHealth(radius/10)
           .setVelX(Math.random()*(Math.random()<0.5? 1 : -1)*(4-radius/10))
           .setVelY(Math.random()*(Math.random()<0.5? 1 : -1)*(4-radius/10))
           .setAngle(Math.random()*Math.PI*2).setFaction("neutral");
     }
     
     private Entity createExplosion(Entity src){
       return new Entity(new ExplosionUpdate(worldX,worldY)).setType("explosion")
           .setX(src.getX()).setY(src.getY()).setRadius(4);
     }
     private Entity createMineExplosion(Entity src){
       return new Entity(new MineExplosionUpdate(worldX,worldY),src.getData()).setType("mineExplosion")
           .setX(src.getX()).setY(src.getY()).setRadius(40).setHealth(10).setFaction("green");
     }
     
     private boolean collided(int id,Entity e,int damage,LinkedList<Entity> more){
       int health = e.getHealth()-damage;
       boolean killed = false;
       switch(e.getType()){
         case"asteroid": 
           if(health<=0){
             asteroidCount--;
             less.addLast(id);
             int radius = e.getRadius();
             killed = true;
             if(radius>10){
               more.add(createAsteroid(radius-10,e.getX(),e.getY(), radius-10/10));
               more.add(createAsteroid(radius-10,e.getX(),e.getY(), radius-10/10));
             }
           }break;
         case"ship":
           if(health<=0){
             less.addLast(id);
             less.addLast(id+1);
             //more.add();
           }break;
         case"aiShip":
           if(health<=0){
             aiCount--;
             less.addLast(id);
             killed = true;
             more.add(createAiShip(20,Math.random()*worldX,Math.random()*worldY,health));
           }break;
         case"mine":
           if(health<=0){
             less.addLast(id);
             more.add(createMineExplosion(e));
             //more.add();
           }break;
         case"mineExplosion":
           if(health<=0){
             less.addLast(id);
             //more.add(createMineExplosion(e));
             //more.add();
           }break;
         case"bullet":
           if(health<=0){
             less.addLast(id);
             //((SpaceshipData)((BulletData)e.getData()).getOwner().getData()).addXp();
           }break;
       }
       e.setHealth(health);
       more.add(createExplosion(e));
       if(e.getType().equals("shield")){
         if(e.getHealth()<0) e.setHealth(0);
       }
       return killed;
     } 
     private void sendLoop(Map<Integer, Entity> entities, Map<Integer, Entity> players,long start) throws IOException{
       
       List<Communication> comms = new ArrayList<>(players.size());
       for(Entity p: players.values()){
         Communication c = new Communication();
         c.player = p;
         c.x = p.getX();
         c.y = p.getY();
         comms.add(c);
       }
       
       for(Entity e : entities.values()){
         String report = "";
         
         for(Communication c : comms){
           
           if(e.getType().equals("ship")||(Math.abs(e.getX()-c.x)<1020&&Math.abs(e.getY()-c.y)<920)){
             if(report.isEmpty()) report = "," + e.getReport();   
             switch(e.getType()){
               case"ship": c.ship+=report; break;
               case"aiShip": c.aiShip+=report; break;
               case"asteroid": c.asteroid+=report; break;
               case"bullet": c.bullet+=report; break;
               case"mine": c.mine +=report; break;
               case"shield": c.shield +=report;break;
               case"explosion": c.explosion+=report; break;
               case"mineExplosion": c.mineExplosion+=report;break;
               //case"pointDefense": c.ship+=report; break;
             }
           }
         }
       }
       for(Communication c : comms){
         
         c.player.send("{\"time\":"+(new Date().getTime()-start)+",\"ships\":["+(c.ship.isEmpty()?"":c.ship.substring(1))+"]"
             +",\"aiShips\":["+(c.aiShip.isEmpty()?"":c.aiShip.substring(1))+"]"
             +",\"asteroids\":["+(c.asteroid.isEmpty()?"":c.asteroid.substring(1))+"]"
             +",\"bullets\":["+(c.bullet.isEmpty()?"":c.bullet.substring(1))+"]"
             +",\"mines\":["+(c.mine.isEmpty()?"":c.mine.substring(1))+"]"
             +",\"shields\":["+(c.shield.isEmpty()?"":c.shield.substring(1))+"]"
             +",\"explosions\":["+(c.explosion.isEmpty()?"":c.explosion.substring(1))+"]"
             +",\"mineExplosions\":["+(c.mineExplosion.isEmpty()?"":c.mineExplosion.substring(1))+"]"
             +"}");
         
       }
       //if(x>0||y>0){
       
       
       /*
         for(Integer id : players.keySet()){
           players.get(id).send(state);  
           //if(entities.size()>0)entities.get(0).getSession().getBasicRemote().sendText(state);
         }
       }
       */
     }
    private class Communication{
      
      public Entity player = null;
      public double x = 0;
      public double y = 0;
      public String ship = "";
      public String aiShip = "";
      public String asteroid = "";
      public String bullet = "";
      public String mine = "";
      public String shield = "";
      public String explosion = "";
      public String mineExplosion = "";
    }
    private class Tracking{
      public Entity ai = null;
      public List<Entity> area = new LinkedList<>();
    }
}     