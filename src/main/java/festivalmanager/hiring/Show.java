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
	public Show(@NotNull String name) {
		this.name = name;
	} 
 
	public Show() {
	}

	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

}
