package version_K;

public class SpaceshipData extends Data{
  private int reloadTimer = 1;
  private int reloadRate = 10;
  private int mineTimer = 1;
  private int mineRate = 30;
  private int shieldTimer = 1;
  private int shieldRate = 30;
  private int hits = 0;
  private int upgrade = 1;
  private int score = 0;
  private int age = 1;
  private double shipFacing;
  private boolean engineOn = false;
  private double lAng = 0;
  private double rAng = 0;
  private String name = "";
 
  public void reload(){
    if(reloadTimer > 0){
      reloadTimer--;  
    }
    if(mineTimer > 0){
      mineTimer--;
    }
    if(shieldTimer >0){
      shieldTimer--;
    }
  }
  public void fire(){
    reloadTimer = reloadRate;
  }
  public void mineFire(){
    mineTimer = mineRate;
  }
  public void shield(){
    shieldTimer = shieldRate;
  }
  public boolean isShieldReloaded() {
    return(shieldTimer==0);
  }
  public boolean isMineReloaded(){
    return(mineTimer==0);
  }
  public boolean isReloaded(){
    return(reloadTimer==0);
  }
  public void addXp() {
    setScore(getScore() + 1); 
    
    hits++;
    if(reloadRate>1){
      if(hits>5){
        reloadRate--;
        hits=0;
      }
    }
    else{
      upgrade++;
      reloadRate = 10;
    }
 
  }
  public int getScore() {
    return score;
  }
  public void setScore(int score) {
    this.score = score;
  }
  public int getUpgrade() {
    return upgrade;
  }
  public double getShipFacing() {
    return shipFacing;
  }
  public void setShipFacing(double shipFacing) {
    this.shipFacing = shipFacing;
  }
  public boolean isEngineOn(){
    return engineOn;
  }
  public void setEngineOn(boolean engineOn){
    this.engineOn = engineOn;
  }
  public double getrAng() {
    return rAng;
  }
  public void setrAng(double rAng) {
    this.rAng = rAng;
  }
  public double getlAng() {
    return lAng;
  }
  public void setlAng(double lAng) {
    this.lAng = lAng;
  }
  public int getAge() {
    return age;
  }
  public void setAge(int age) {
    this.age = age;
  }
  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }

}
