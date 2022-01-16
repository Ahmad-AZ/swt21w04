package festivalmanager.ticketShop;

/**
 * Ticket Typs that can be chosen
 */
public enum TicketType {

	DAY_TICKET("Tagesticket"),
	CAMPING("Camping Ticket"),
	;

	private final String displayValue;

	/**
	 *
	 * @return the Ticket Type displayValue as it saved in DAY_TICKET and CAMPING types
	 * */
	public String getDisplayValue() {
		return displayValue;
	}

	/**
	 *
	 * @param displayValue
	 */
	TicketType(String displayValue) {
		this.displayValue= displayValue;
	}
}
