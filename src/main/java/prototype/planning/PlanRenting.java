package prototype.planning;

import prototype.festival.Festival;

public class PlanRenting extends Planning {

	private int numberOfStage;

	/**
	 *
	 * @param festival
	 */
	PlanRenting(Festival festival) {
		super(festival);
	}

/*	I wrote those function as comments because there is still no Equipment class
	public void rent(Equipment equipment){
		equipments.add(equipment);
	}
	*/
	public boolean unrent(Equipment equipment){
		if (!equipments.contain(equipment)){
			System.out.println('there is no such equipment to remove');
			return false;
		}
		equipments.remove(equipment);
		return true;
	}


}
