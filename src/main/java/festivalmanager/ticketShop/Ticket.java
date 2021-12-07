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
	private long festivalId;
	private String festivalName;

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

	public Ticket(long festivalId,String festivalName,  int dayTicketsCount, int campingTicketsCount, TicketType ticketType, float dayTicketPrice, float campingTicketPrice) {
		this.festivalId= festivalId;
		this.festivalName=festivalName;
		DayTicketsCount = dayTicketsCount;
		CampingTicketsCount = campingTicketsCount;
		this.ticketType = ticketType;
		DayTicketPrice = dayTicketPrice;
		CampingTicketPrice = campingTicketPrice;
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

	public long getFestivalId() {
		return festivalId;
	}

	public void setFestivalId(long festivalId) {
		this.festivalId = festivalId;
	}

	public String getFestivalName() {
		return festivalName;
	}

	public void setFestivalName(String festivalName) {
		this.festivalName = festivalName;
	}


}
