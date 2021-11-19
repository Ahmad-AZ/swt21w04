package prototype.location;



import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

import org.springframework.format.annotation.DateTimeFormat;

class NewLocationForm {
	@NotEmpty   
	private final String name; 
	
	@NotEmpty  
	private final String adress;
	
	@Min(value = 0) 
	private final Double pricePerDay;

	@Min(value = 0)  
	private final Long visitorCapacity;
	
	@Min(value = 0) 
	private final Long stageCapacity;
		
	public NewLocationForm(String name, String adress, Double pricePerDay, Long visitorCapacity, Long stageCapacity) {
		this.name = name;
		this.adress = adress;
		this.pricePerDay = pricePerDay;
		this.visitorCapacity = visitorCapacity;
		this.stageCapacity = stageCapacity;
	}     
	
	public String getName() {
		return name;
	}

	public String getAdress() {
		return adress;
	}

	public double getPricePerDay() {
		return pricePerDay;
	}

	public Long getVisitorCapacity() {
		return visitorCapacity;
	}

	public Long getStageCapacity() {
		return stageCapacity;
	}


}
