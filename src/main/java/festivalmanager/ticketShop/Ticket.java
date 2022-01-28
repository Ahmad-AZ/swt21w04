package festivalmanager.ticketShop;

import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.util.UUID;


/**
 * class of {@link Ticket} Entity
 * @author Ahmad Abu Zahra
 */
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



	public Ticket() {
	}

	public Ticket( int dayTicketsCount, int campingTicketsCount, TicketType ticketType,
				   float dayTicketPrice, float campingTicketPrice) {

		this.dayTicketsCount = dayTicketsCount;
		this.campingTicketsCount = campingTicketsCount;
		this.ticketType = ticketType;
		this.dayTicketPrice = dayTicketPrice;
		this.campingTicketPrice = campingTicketPrice;
		this.soldDayTicket=0;
		this.soldCampingTicket=0;

	}


	/**
	 *
	 * @param festivalId
	 * @param festivalName
	 * @param dayTicketsCount
	 * @param campingTicketsCount
	 * @param ticketType
	 * @param dayTicketPrice
	 * @param campingTicketPrice
	 */
	public Ticket(long festivalId, String festivalName, int dayTicketsCount,
				  int campingTicketsCount, TicketType ticketType, float dayTicketPrice, float campingTicketPrice) {
		this.festivalId = festivalId;
		this.festivalName = festivalName;
		this.dayTicketsCount = dayTicketsCount;
		this.campingTicketsCount = campingTicketsCount;
		this.ticketType = ticketType;
		this.dayTicketPrice = dayTicketPrice;
		this.campingTicketPrice = campingTicketPrice;
	}

	/**
	 * @return Festival ID
	 */
	public long getFestivalId() {
		return festivalId;
	}

	/**
	 * sets Festival ID
	 * @param festivalId
	 */
	public void setFestivalId(long festivalId) {
		this.festivalId = festivalId;
	}

	/**
	 *
	 * @return Festival Name
	 */

	public String getFestivalName() {
		return festivalName;
	}

	/**
	 * sets Festival Name
	 * @param festivalName
	 */
	public void setFestivalName(String festivalName) {
		this.festivalName = festivalName;
	}

	/**
	 *
	 * @return Day Tickets Count
	 */
	public int getDayTicketsCount() {
		return dayTicketsCount;
	}

	/**
	 * set Day Ticket count
	 * @param dayTicketsCount
	 */
	public void setDayTicketsCount(int dayTicketsCount) {
		this.dayTicketsCount = dayTicketsCount;
	}

	/**
	 *
	 * @return Camping Ticket Count
	 */
	public int getCampingTicketsCount() {
		return campingTicketsCount;
	}

	/**
	 * sets Camping Ticket Count
	 * @param campingTicketsCount
	 */
	public void setCampingTicketsCount(int campingTicketsCount) {
		this.campingTicketsCount = campingTicketsCount;
	}

	/**
	 *
	 * @return Ticket Type , camping ticket or day ticket
	 */
	public TicketType getTicketType() {
		return ticketType;
	}

	/**
	 * sets Ticket Type, camping ticket or day ticket
	 * @param ticketType
	 */
	public void setTicketType(TicketType ticketType) {
		this.ticketType = ticketType;
	}

	/**
	 *
	 * @return day ticket price
	 */
	public float getDayTicketPrice() {
		return dayTicketPrice;
	}

	/**
	 * sets day ticket price
	 * @param dayTicketPrice
	 */
	public void setDayTicketPrice(float dayTicketPrice) {
		this.dayTicketPrice = dayTicketPrice;
	}

	/**
	 *
	 * @return camping ticket price
	 */
	public float getCampingTicketPrice() {
		return campingTicketPrice;
	}

	/**
	 * sets camping ticket price
	 * @param campingTicketPrice
	 */
	public void setCampingTicketPrice(float campingTicketPrice) {
		this.campingTicketPrice = campingTicketPrice;
	}


	/**
	 *
	 * @return sold camping ticket count
	 */
	public int getSoldCampingTicket() {
		return soldCampingTicket;
	}

	/**
	 * sets sold camping  ticket
	 * @param soldCampingTicket
	 */
	public void setSoldCampingTicket(int soldCampingTicket) {
		this.soldCampingTicket += soldCampingTicket ;
	}

	/**
	 *
	 * @return sold day ticket
	 */
	public int getSoldDayTicket() {
		return soldDayTicket;
	}

	/**
	 * sets sold day ticket
	 * @param soldDayTicket
	 */
	public void setSoldDayTicket(int soldDayTicket) {
		this.soldDayTicket += soldDayTicket;
	}

	/**
	 *
	 * @return Ticket ID
	 */
	public UUID getId() {
		return id;
	}


	/**
	 *
	 * @return ticket properties as String
	 */
	@Override
	public String toString() {
		return "camping ticket count "+getCampingTicketsCount()+
				" sold camping ticket  "+getSoldCampingTicket()+
				" day ticket count "+getDayTicketsCount()+
				" sold day ticket "+getSoldDayTicket();
	}
}
