package festivalmanager.utils;

public class LongOrNull {

	private Long value;

	public LongOrNull() {
		this.value = null;
	}

	public LongOrNull(long value) {
		this.value = value;
	}

	public Long getValue() {
		return this.value;
	}

}
