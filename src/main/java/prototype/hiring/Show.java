package prototype.hiring;

import org.salespointframework.time.Interval;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.lang.reflect.Constructor;

@Entity
public class Show {

	@Id @GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	private String name;
	private static Interval duration;

	public Show(@NotNull String name,@NotNull Interval duration) {
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
