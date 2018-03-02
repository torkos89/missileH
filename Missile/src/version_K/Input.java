package version_K;

public class Input {
  private boolean up = false;
  private boolean left = false;
  private boolean down = false;
  private boolean right = false;
  private boolean brake = false;
  private boolean shooting = false;
  private boolean mining = false;
  private boolean shielding = false;
  private boolean rotate = false;
  private int mouseX;
  private int mouseY;
  
  
  public boolean isUp(){
    return up;
  }
  public void setUp(boolean up){
    this.up = up;
  }
  public boolean isLeft(){
    return left;
  }
  public void setLeft(boolean left){
    this.left = left;
  }
  public boolean isDown(){
    return down;
  }
  public void setDown(boolean down){
    this.down = down;
  }
  public boolean isRight(){
    return right;
  }
  public void setRight(boolean right){
    this.right = right;
  }
  public boolean isBrake() {
    return brake;
  }
  public void setBrake(boolean brake) {
    this.brake = brake;
  }
  public boolean isShooting() {
    return shooting;
  }
  public void setShooting(boolean shooting) {
    this.shooting = shooting;
  }
  public boolean isRotate(){
    return rotate;
  }
  public void setRotate(boolean rotate){
    this.rotate = rotate;
  }
  public void setMouse(int x, int y) {
    this.mouseX = x;
    this.mouseY = y;
  }
  public int getMouseX(){
    return mouseX;
  }
  public int getMouseY(){
    return mouseY;
  }
  public boolean isMining() {
    return mining;
  }
  public void setMining(boolean mining) {
    this.mining = mining;
  }
  public boolean isShielding() {
    return shielding;
  }
  public void setShield(boolean shielding) { 
    this.shielding = shielding;
  }
}
