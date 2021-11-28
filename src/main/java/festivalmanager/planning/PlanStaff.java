package festivalmanager.planning;

import festivalmanager.festival.Festival;

public class PlanStaff extends Planning {

	PlanStaff(Festival festival) {
		super(festival);
		// TODO Auto-generated constructor stub
	}

	private int numberOfTechnicans, numberOfSecurity;

	public int getNumberOfTechnicans()
	{
		return numberOfTechnicans;
	}

	public int getNumberOfSecurity()
	{
		return numberOfSecurity;
	}
}
