package festivalmanager.ticketShop;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import festivalmanager.festival.Festival;

/**
 * the form used to buy {@link Ticket}s for a {@link Festival}
 *
 * @author Adrian Scholze
 */
public class BuyTicketForm { 
  
	@NotNull
	private final String type;
	
//	@NotNull
//	@Min(value = 0, message="Anzahl darf nicht negativ sein") 
	private final Long amount; 
	
	/**
	 * Creates a new {@link BuyTicketForm} with the given type and amount
	 *
	 * @param type must not be {@literal null}.
	 * @param amount must not be {@literal null}.
	 */
	public BuyTicketForm(String type, Long amount) {
		this.type = type;
		this.amount = amount;
	}

	/**
	 * Returns forms type.
	 * 
	 * @return name
	 */
	public String getType() {
		return type;
	}

	/**
	 * Returns forms amount
	 * 
	 * @return amount
	 */
	public long getAmount() {
		return amount;
	}		
}

