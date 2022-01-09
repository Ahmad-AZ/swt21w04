package festivalmanager.ticketShop;

import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.util.UUID;


@Entity
public class Ticket {


	private @Id @GeneratedValue(generator = "UUID")  @GenericGenerator(
			name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
	@Column( updatable = false, nullable = false)
	UUID id;

	@Column
	private long festivalId;
	private String festivalName;

	@Column
	private int dayTicketsCount;

	@Column
	private int campingTicketsCount;
	@Column
	private TicketType ticketType;
	@Column
	private float dayTicketPrice;

	@Column
	private float campingTicketPrice;

	@Column
	private int soldCampingTicket;
	@Column
	private int soldDayTicket;


	@Column
	private String qr;


	public Ticket() {
	}

	public Ticket( int dayTicketsCount, int campingTicketsCount, TicketType ticketType, float dayTicketPrice, float campingTicketPrice) {

		this.dayTicketsCount = dayTicketsCount;
		this.campingTicketsCount = campingTicketsCount;
		this.ticketType = ticketType;
		this.dayTicketPrice = dayTicketPrice;
		this.campingTicketPrice = campingTicketPrice;
		this.soldDayTicket=0;
		this.soldCampingTicket=0;

	}


	public Ticket(long festivalId, String festivalName, int dayTicketsCount, int campingTicketsCount, TicketType ticketType, float dayTicketPrice, float campingTicketPrice) {
		this.festivalId = festivalId;
		this.festivalName = festivalName;
		this.dayTicketsCount = dayTicketsCount;
		this.campingTicketsCount = campingTicketsCount;
		this.ticketType = ticketType;
		this.dayTicketPrice = dayTicketPrice;
		this.campingTicketPrice = campingTicketPrice;
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
		return dayTicketsCount;
	}

	public void setDayTicketsCount(int dayTicketsCount) {
		this.dayTicketsCount = dayTicketsCount;
	}

	public int getCampingTicketsCount() {
		return campingTicketsCount;
	}

	public void setCampingTicketsCount(int campingTicketsCount) {
		this.campingTicketsCount = campingTicketsCount;
	}

	public TicketType getTicketType() {
		return ticketType;
	}

	public void setTicketType(TicketType ticketType) {
		this.ticketType = ticketType;
	}

	public float getDayTicketPrice() {
		return dayTicketPrice;
	}

	public void setDayTicketPrice(float dayTicketPrice) {
		this.dayTicketPrice = dayTicketPrice;
	}

	public float getCampingTicketPrice() {
		return campingTicketPrice;
	}

	public void setCampingTicketPrice(float campingTicketPrice) {
		this.campingTicketPrice = campingTicketPrice;
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

	public UUID getId() {
		return id;
	}

	public String getQr() {
		return qr;
	}

	public void setQr(String qr) {
		this.qr = qr;
	}

	@Override
	public String toString() {
		return "camping ticket count "+getCampingTicketsCount()+
				" sold camping ticket  "+getSoldCampingTicket()+
				" day ticket count "+getDayTicketsCount()+
				" sold day ticket "+getSoldDayTicket();
	}
}
