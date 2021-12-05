package festivalmanager.festival;


import java.time.LocalDate;
import java.util.Date;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;


class NewFestivalForm {
	
	@NotEmpty   
	private final String name; 
	
	@NotNull
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private final LocalDate startDate;
	
	@NotNull
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private final LocalDate endDate;
	
	
	public NewFestivalForm(String name, LocalDate startDate, LocalDate endDate) {
		this.name = name;
		this.startDate = startDate;
		this.endDate = endDate;
	}     
	
	public String getName() {
		return name;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}
}