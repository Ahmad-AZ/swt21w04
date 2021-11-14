package prototype.planning;

import prototype.festival.Festival;

import java.util.Date;

public class PlanDate extends Planning {

	private Date startDate, endDate;

	PlanDate(Festival festival) {

		super(festival);
	}

	public void setDate(Date startDate, Date endDate){
		this.startDate=startDate;
		this.endDate=endDate;
	}

	public Date getStartDate() {
		return startDate;
	}

	public Date getEndDate() {
		return endDate;
	}
}
