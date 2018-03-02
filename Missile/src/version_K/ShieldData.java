package version_K;

public class ShieldData extends Data {
  private int shieldTimer = 1;
  private int shieldRate = 200;
  
  public void reload(){
    if(shieldTimer >0){
      shieldTimer--;
      
    }
  }
  public void shield(){
    shieldTimer = shieldRate;
  }
  public boolean isShieldReloaded() {
    return(shieldTimer==0);
  }
}
