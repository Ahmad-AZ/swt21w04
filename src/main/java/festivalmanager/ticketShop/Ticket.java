package festivalmanager.ticketShop;
import org.springframework.lang.Nullable;

import javax.persistence.*;


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

	@Column
	private int soldCampingTicket;
	@Column
	private int soldDayTicket;


	public Ticket() {
	}

	public Ticket(@Nullable long festivalId, String festivalName, int dayTicketsCount, int campingTicketsCount, TicketType ticketType, float dayTicketPrice, float campingTicketPrice) {
		this.festivalId= festivalId;
		this.festivalName=festivalName;
		this.DayTicketsCount = dayTicketsCount;
		this.CampingTicketsCount = campingTicketsCount;
		this.ticketType = ticketType;
		this.DayTicketPrice = dayTicketPrice;
		this.CampingTicketPrice = campingTicketPrice;
		this.soldDayTicket=0;
		this.soldCampingTicket=0;

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

	public int getSoldCampingTicket() {
		return soldCampingTicket;
	}

	public void setSoldCampingTicket(int soldCampingTicket) {
		this.soldCampingTicket += soldCampingTicket ;
	}

	public int getSoldDayTicket() {
		return soldDayTicket;
	}

	public void setSoldDayTicket(int soldDayTicket) {
		this.soldDayTicket += soldDayTicket;
	}


	@Override
	public String toString() {
		return "camping ticket count "+getCampingTicketsCount()+
				" sold camping ticket  "+getSoldCampingTicket()+
				" day ticket count "+getDayTicketsCount()+
				" sold day ticket "+getSoldDayTicket();
	}
}
