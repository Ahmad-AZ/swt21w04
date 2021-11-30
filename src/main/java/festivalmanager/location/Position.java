package festivalmanager.location;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Position {
	@Id @GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	private int x1, y1, x2, y2;
	
	public Position(int x1, int y1, int x2, int y2) {
		
	}

	public int getX1() {
		return x1;
	}

	public void setX1(int x1) {
		this.x1 = x1;
	}
	
	
	
}
