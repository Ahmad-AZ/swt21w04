package festivalmanager.location;


import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;

@Entity
public class Area {
	public static enum AreaType {
		STAGE, CATERING, TOILET, CLOSED, PARKING, CAMPING
	}
	
	@Id @GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@Lob
	private Position position;
	private AreaType type;
	
	private int visitorCapacity;
	
	public Area() {}
	
	public Area(Position position, AreaType type, int visitorCapacity){
		this.setPosition(position);
		this.setType(type);
		this.setVisitorCapacity(visitorCapacity);
	}
	
	public long getId() {
		return id;
	}

	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}

	public AreaType getType() {
		return type;
	}

	public void setType(AreaType type) {
		this.type = type;
	}

	public int getVisitorCapacity() {
		return visitorCapacity;
	}

	public void setVisitorCapacity(int visitorCapacity) {
		this.visitorCapacity = visitorCapacity;
	}

}
