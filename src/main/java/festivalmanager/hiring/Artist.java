package festivalmanager.hiring;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
public class Artist {
	@Id@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	private String name;

	public Artist(@NotNull String name) {
		this.name = name;
	}
	public Artist() {}

	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}
}
