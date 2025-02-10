public class Enemy
{
    public double health;
    public double damage;
    public Enemy(double inX, double inY)
    {
        this.health = inX;
        this.damage = inY;
    }
    public void Oww(double damage)
    {
        this.health -= damage;
    }
}