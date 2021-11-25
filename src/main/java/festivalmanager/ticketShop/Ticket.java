package festivalmanager.ticketShop;


import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;

import java.time.LocalDate;
import java.util.Date;


@Entity
public class Ticket {




	private @Id @GeneratedValue long id;

	@Column
	private String festivalName;

	@Column
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private String festivalDate;
	@Column
	private int ticketsCount;
	@Column
	private TicketType ticketType;
	@Column
	private float ticketPrice;



	public Ticket(String festivalName, String date, int ticketsCount, TicketType ticketType, float ticketPrice) {
		this.festivalName = festivalName.toLowerCase();
		this.festivalDate = date;
		this.ticketsCount = ticketsCount;
		this.ticketType = ticketType;
		this.ticketPrice = ticketPrice;
	}

	public Ticket() {

	}

	public long getId() {
		return id;
	}

	public String getFestivalDate() {
		return festivalDate;
	}

	public TicketType getTicketType() {
		return ticketType;
	}

	public float getTicketPrice() {
		return ticketPrice;
	}

	public int getTicketsCount() {
		return ticketsCount;
	}

	public void setFestivalDate(String festivalDate) {
		this.festivalDate = festivalDate;
	}

	public void setTicketsCount(int ticketsCount) {
		this.ticketsCount = ticketsCount;
	}

	public void setTicketType(TicketType ticketType) {
		this.ticketType = ticketType;
	}

	public void setTicketPrice(float ticketPrice) {
		this.ticketPrice = ticketPrice;
	}

	public String getFestivalName() {
		return festivalName;
	}

	public void setFestivalName(String festivalName) {
		this.festivalName = festivalName;
	}


}
