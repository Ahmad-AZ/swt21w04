package festivalmanager.festival;


import java.util.Date;

import javax.validation.constraints.NotEmpty;

import org.springframework.format.annotation.DateTimeFormat;


class NewFestivalForm {
	
	@NotEmpty   
	private final String name; 
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private final Date startDate;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private final Date endDate;
	
	
	public NewFestivalForm(String name, Date startDate, Date endDate) {
		this.name = name;
		this.startDate = startDate;
		this.endDate = endDate;
	}     
	
	public String getName() {
		return name;
	}

	public Date getStartDate() {
		return startDate;
	}

	public Date getEndDate() {
		return endDate;
	}
}
