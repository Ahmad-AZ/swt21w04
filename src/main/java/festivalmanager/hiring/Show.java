package festivalmanager.hiring;

import org.salespointframework.time.Interval;

import java.time.LocalDateTime;


import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class Show {

	@Id @GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	private String name;
	//Example
	public static LocalDateTime startDate = LocalDateTime.of(2021, 11, 2, 3, 34);
	public static LocalDateTime endDate = LocalDateTime.of(2021, 11, 12, 13, 34);
	@Lob()
	private Interval duration = Interval.from(startDate).to(endDate);

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
