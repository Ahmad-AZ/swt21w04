package festivalmanager.ticketShop;


import javax.persistence.*;

import java.time.LocalDate;


@Entity
public class Ticket {




	private @Id @GeneratedValue long id;

	@Column
	private String festivalName;

	public String getFestivalName() {
		return festivalName;
	}

	public void setFestivalName(String festivalName) {
		this.festivalName = festivalName;
	}

	private LocalDate date;
	@Column
	private int ticketsCount;
	@Column
	private TicketType ticketType;
	@Column
	private float ticketPrice;



	public Ticket(String festivalName, LocalDate date, int ticketsCount, TicketType ticketType, float ticketPrice) {
		this.festivalName = festivalName.toLowerCase();
		this.date = date;
		this.ticketsCount = ticketsCount;
		this.ticketType = ticketType;
		this.ticketPrice = ticketPrice;
	}

	public Ticket() {

	}

	public long getId() {
		return id;
	}

	public LocalDate getDate() {
		return date;
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

	public void setDate(LocalDate date) {
		this.date = date;
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




}
