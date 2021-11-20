package prototype.hiring;

import org.salespointframework.time.Interval;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
public class Show {

	@Id @GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	private String name;
	private Interval duration;

	public Show(@NotNull String name, @NotNull Interval duration) {
		this.name = name;
		this.duration = duration;
	}

	public Show() {

	}

	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public Interval getDuration() {
		return duration;
	}
}
