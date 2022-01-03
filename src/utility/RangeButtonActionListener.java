package utility;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RangeButtonActionListener implements ActionListener {

	Pair<Integer, Integer> coord = null;
	int x = -1, y = -1;
	
	public RangeButtonActionListener(Pair<Integer, Integer> coord, int x, int y) {
		// TODO Auto-generated constructor stub
		//System.out.println("X : " + coord.x + " Y : " + coord.y);
		this.coord = coord;
		this.x = x;
		this.y = y;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		//coord = new Pair<Integer, Integer>(x, y);
		coord.x = x;
		coord.y = y;
		return;
	}

}
