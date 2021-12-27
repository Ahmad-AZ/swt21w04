package festivalmanager.festival;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class StringInputForm {
	
	@NotEmpty
	@NotNull
	@NotBlank
	private final String name; 
		
	public StringInputForm(String name) {
		this.name = name;
	}     
	
	public String getName() {
		return name;
	}
}
