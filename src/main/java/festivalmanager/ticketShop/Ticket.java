package festivalmanager.ticketShop;


import org.springframework.beans.factory.annotation.Required;
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
	private int DayTicketsCount;
	@Column
	private int CampingTicketsCount;
	@Column
	private TicketType ticketType;
	@Column
	private float DayTicketPrice;

	@Column
	private float CampingTicketPrice;


	public Ticket() {
	}

	public Ticket(String festivalName, String festivalDate, int dayTicketsCount, int campingTicketsCount, TicketType ticketType, float dayTicketPrice, float campingTicketPrice) {
		this.festivalName = festivalName;
		this.festivalDate = festivalDate;
		DayTicketsCount = dayTicketsCount;
		CampingTicketsCount = campingTicketsCount;
		this.ticketType = ticketType;
		DayTicketPrice = dayTicketPrice;
		CampingTicketPrice = campingTicketPrice;
	}

	public String getFestivalName() {
		return festivalName;
	}

	@Required
	public void setFestivalName(String festivalName) {
		this.festivalName = festivalName;
	}

	public String getFestivalDate() {
		return festivalDate;
	}

	public void setFestivalDate(String festivalDate) {
		this.festivalDate = festivalDate;
	}

	public int getDayTicketsCount() {
		return DayTicketsCount;
	}

	public void setDayTicketsCount(int dayTicketsCount) {
		DayTicketsCount = dayTicketsCount;
	}

	public int getCampingTicketsCount() {
		return CampingTicketsCount;
	}

	public void setCampingTicketsCount(int campingTicketsCount) {
		CampingTicketsCount = campingTicketsCount;
	}

	public TicketType getTicketType() {
		return ticketType;
	}

	public void setTicketType(TicketType ticketType) {
		this.ticketType = ticketType;
	}

	public float getDayTicketPrice() {
		return DayTicketPrice;
	}

	public void setDayTicketPrice(float dayTicketPrice) {
		DayTicketPrice = dayTicketPrice;
	}

	public float getCampingTicketPrice() {
		return CampingTicketPrice;
	}

	public void setCampingTicketPrice(float campingTicketPrice) {
		CampingTicketPrice = campingTicketPrice;
	}




}
