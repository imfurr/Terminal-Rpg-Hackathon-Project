public class Vector2
{
    public int x;
    public int y;
    public Vector2(int inX, int inY)
    {
        this.x = inX;
        this.y = inY;
    }
    public void Add(Vector2 inputVector)
    {
        this.x += inputVector.x;
        this.y += inputVector.y;
    }
    public void Sub(Vector2 inputVector)
    {
        this.x -= inputVector.x;
        this.y -= inputVector.y;
    }
}