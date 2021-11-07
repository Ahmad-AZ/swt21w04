package prototype.festival;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class Festival {

	// das Id braucht man, wenn man Festival in der Zukunft aktulisiert
	private  @Id @GeneratedValue long id;

	private Date startDate;
	private Date endDate;


	public Festival(Date startDate, Date endDate) {
		this.startDate = startDate;
		this.endDate = endDate;
	}

	public Festival() {

	}


	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}


}
