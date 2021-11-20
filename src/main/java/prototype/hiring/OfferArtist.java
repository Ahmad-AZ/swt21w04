package prototype.hiring;

import org.javamoney.moneta.Money;
import prototype.Equipment.Catering;

import javax.persistence.CascadeType;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.List;

public class OfferArtist extends Offer {
	private Money feePerShow;

	@OneToOne
	private Artist artist;

	@OneToMany(cascade = CascadeType.ALL)
	private List<Show> shows;

	public OfferArtist( Artist artist, Money feePerShow, List<Show> shows, long id) {
		super(id);
		this.feePerShow = feePerShow;
		this.artist = artist;
		this.shows = shows;
	}

	public Money getFeePerShow() {
		return feePerShow;
	}

	public Artist getArtist() {
		return artist;
	}

	public List<Show> getShows() {
		return shows;
	}
}
