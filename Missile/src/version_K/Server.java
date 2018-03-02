
package version_K;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/serverK")
public class Server {
  private Entity player = null;
  private Input input = null;
  private int worldX = 10000;
  private int worldY = 10000;

  @OnOpen public void onOpen(Session ses){
    
    ses.getAsyncRemote().sendText("_"+ses.getId()+","+worldX+","+worldY);
 
    //ses.getUserProperties().put("engineOn", false);
    //ses.getUserProperties().put("brake", false);
    //ses.getUserProperties().put("turnLeft", false);
    //ses.getUserProperties().put("turnRight", false);
    //ses.getUserProperties().put("shooting", false);
    input = new Input();
    
    player = new Entity(ses,new SpaceshipData(),input,new SpaceshipUpdate(worldX,worldY),new SpaceshipReport())
        .setX(Math.random()*worldX).setY(Math.random()*worldY).setVelX(0).setVelY(0)
        .setAngle(0).setType("ship").setRadius(20).setHealth(6).setFaction("green");
    ses.getUserProperties().put("id", WorldFactory.getWorld().addEntity(player));
   
  }
  @OnMessage public void onMessage(String mes, Session ses){
    
    switch(mes.charAt(0)){
      case'ø': ((SpaceshipData)player.getData()).setName(mes.substring(1)); break;
      case'u': input.setUp(mes.charAt(1)=='1'); break;
      case'd': input.setDown(mes.charAt(1)=='1'); break;
      case'l': input.setLeft(mes.charAt(1)=='1'); break;
      case'r': input.setRight(mes.charAt(1)=='1'); break;
      case'b': input.setBrake(mes.charAt(1)=='1'); break;
      case's': input.setShooting(mes.charAt(1)=='1'); break;
      case't': input.setRotate(!input.isRotate()); break;
      case'f': input.setMining(mes.charAt(1)=='1'); break;
      //case'h': input.setShield(mes.charAt(1)=='1'); break;
      //case'w': input.setWeapon(mes.charAt(1)=='1');break;
      case'm': //player.setAngle(Double.parseDouble(mes.substring(1))); break;
       String[] pair = mes.substring(1).split(",");
       input.setMouse((int)Double.parseDouble(pair[0]),(int)Double.parseDouble(pair[1]));
//       System.out.println(pair[0]+","+pair[1]);//,mes.charAt(',')));
    }
    //ses.getUserProperties().put("engineOn", mes.charAt(0)=='1'? "true" : "false");
    //ses.getUserProperties().put("brake", mes.charAt(1)=='1'? "true" : "false");
    //ses.getUserProperties().put("turnLeft", mes.charAt(1)=='1'? "true" : "false");
    //ses.getUserProperties().put("turnRight", mes.charAt(2)=='1'? "true" : "false");
    //ses.getUserProperties().put("shooting", mes.charAt(2)=='1'? "true" : "false");
    /*
    input.setUp(mes.charAt(0)=='1');   
    input.setLeft(mes.charAt(1)=='1');   
    input.setDown(mes.charAt(2)=='1');      
    input.setRight(mes.charAt(3)=='1');
    input.setBrake(mes.charAt(4)=='1');
    input.setShooting(mes.charAt(5)=='1');
    input.setRotate(mes.charAt(6)=='1');
    player.setAngle(Double.parseDouble(mes.substring(7)));
    */
  //  ses.getAsyncRemote().sendText(mes);
    
  }
  
  @OnClose public void onClose(Session ses){
    int id = (int)ses.getUserProperties().get("id");
    WorldFactory.getWorld().removePlayer(id);
    WorldFactory.getWorld().removeEntity(id+1);
  }
}
