package festivalmanager.festival;

public class LongOrNull {

	private Long value;

	LongOrNull() {
		this.value = null;
	}

	LongOrNull(long value) {
		this.value = value;
	}

	public Long getValue() {
		return this.value;
	}

}
