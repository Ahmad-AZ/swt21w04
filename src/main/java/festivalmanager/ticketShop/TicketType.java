package festivalmanager.ticketShop;

public enum TicketType {

	DAY_TICKET("Tagesticket"),
	CAMPING("Camping Ticket"),
	;

	private final String displayValue;

	public String getDisplayValue() {
		return displayValue;
	}

	TicketType(String displayValue) {
		this.displayValue= displayValue;
	}
}
