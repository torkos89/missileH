package version_K;

public class SpaceshipReport extends Report {
  
  public String getData(Entity e) {
    SpaceshipData spData = (SpaceshipData)e.getData();
    /*{\"id\":0,\"type\":\"ship\","
        +"\"data\":{\"color\":\"silver\",\"pos\":{\"x\":"+x+",\"y\":"+y+"},"
        +"\"angle\":"+angle+",\"engineOn\":"+(engineOn?"true":"false")
      +"}}*/
    return "{"+getPair("id",e.getSession().getId())+","
      +getPair("shipFacing",spData.getShipFacing())+","+getIntPair("score",spData.getScore())+","
      +getIntPair("health",e.getHealth())+","+getPair("engineOn",spData.isEngineOn())+","
      +getPair("name",spData.getName().isEmpty()? "failName": spData.getName())+","
      +"\"pos\":{"+getIntPair("x",e.getX())+","+getIntPair("y",e.getY())+"},"+getPair("lAng",spData.getlAng())+","
      +getPair("rAng",spData.getrAng())+"}";  //","
              //+getPair("engineOn",e.getInput().isEngineOn())+"}";
  }
}/*
    return "{"+getPair("type","ship")+","+getPair("color","silver")+","
     +"\"pos\":{"
       +getPair("x",Double.parseDouble(data.get("px").toString()))+","+getPair("y",Double.parseDouble(data.get("py").toString()))
     +"},"+getPair("angle",Double.parseDouble(data.get("angle").toString()))+","
           +getPair("engineOn",Boolean.parseBoolean(data.get("engineOn").toString()))+"}";*/