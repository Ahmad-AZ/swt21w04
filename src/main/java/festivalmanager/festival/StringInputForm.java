package festivalmanager.festival;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * the form used to get the new {@link Festival} name
 *
 * @author Adrian Scholze
 */
public class StringInputForm {
	
	@NotEmpty
	@NotNull
	@NotBlank
	private final String name; 
	
	/**
	 * Creates a new {@link NewFestivalForm} with the given name.
	 *
	 * @param name must not be {@literal null}.
	 */
	public StringInputForm(String name) {
		this.name = name;
	}     
	
	/**
	 * Returns forms name.
	 * 
	 * @return name
	 */
	public String getName() {
		return name;
	}
}
