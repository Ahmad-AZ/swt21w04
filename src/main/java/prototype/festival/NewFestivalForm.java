package prototype.festival;


import javax.validation.constraints.NotEmpty;


class NewFestivalForm {
	
	@NotEmpty   
	private final String name;
	
	public NewFestivalForm(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
}
