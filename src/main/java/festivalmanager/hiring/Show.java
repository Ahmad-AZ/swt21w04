package festivalmanager.hiring;

import java.io.Serializable;
import java.time.Duration;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity

public class Show implements Serializable{

	private static final long serialVersionUID = 1L;
		
	@Id @GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	private String name;
	private Duration performance;

	/**
	 * show constructor
	 * @param name must not be {@literal null}
	 * @param performance must not be {@literal null}
	 */
	public Show(@NotNull String name, Duration performance) {
		this.name = name;
		this.performance = performance;
	} 
 
	public Show() {
	}

	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public long getPerformance() {
		return performance.toMinutes();
	}
}
