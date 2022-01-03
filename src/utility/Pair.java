package utility;

public class Pair<T1, T2> {
	
	public T1 x;
	public T2 y;
	
	public Pair()
	{
		x = null;
		y = null;
	}
	
	public Pair(T1 x, T2 y)
	{
		this.x = x;
		this.y = y;
	}
	
	public T1 first()
	{
		return x;
	}
	
	public T2 second()
	{
		return y;
	}
	
	public boolean equals (Pair<T1, T2> p2)
	{
		if(x == p2.x && y == p2.y) return true;
		else return false;
	}
}
