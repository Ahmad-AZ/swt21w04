package festivalmanager.festival;


import java.time.LocalDate;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

/**
 * the form used to create the {@link Festival}
 *
 * @author Adrian Scholze
 */
class NewFestivalForm {
	
	@NotEmpty
	@NotNull
	@NotBlank
	private final String name; 
	
	@NotNull
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@FutureOrPresent(message="Das Startdatum darf nicht in der Vergangenheit liegen.")
	private final LocalDate startDate;
	
	@NotNull
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@FutureOrPresent(message="Das Enddatum darf nicht in der Vergangenheit liegen.")
	private final LocalDate endDate;
	
	/**
	 * Creates a new {@link NewFestivalForm} with the given name, start and end date.
	 *
	 * @param name must not be {@literal null}.
	 * @param startDate must not be {@literal null}.
	 * @param endDate must not be {@literal null}.
	 */
	public NewFestivalForm(String name, LocalDate startDate, LocalDate endDate) {
		this.name = name;
		this.startDate = startDate;
		this.endDate = endDate;
	}     
	
	/**
	 * Returns forms name.
	 * 
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns forms start date.
	 * 
	 * @return startDate
	 */
	public LocalDate getStartDate() {
		return startDate;
	}

	/**
	 * Returns forms end date.
	 * 
	 * @return endDate
	 */
	public LocalDate getEndDate() {
		return endDate;
	}
}
