package festivalmanager.ticketShop;

public enum TicketType {

	DAY_TICKET("Day Ticket"),
	CAMPING("Camping"),
	;

	private final String displayValue;

	public String getDisplayValue() {
		return displayValue;
	}

	TicketType(String displayValue) {
		this.displayValue= displayValue;
	}
}
