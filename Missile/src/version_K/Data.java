package version_K;

public class Data {
  private Entity owner;
  public Entity getOwner() {
    return owner;
  }

  public Data setOwner(Entity owner) {
    this.owner = owner;
    return this;
  }
  public void addXp() {
    if(owner!=null) owner.getData().addXp();
  }
}
