package prototype.hiring;

import org.javamoney.moneta.Money;

import javax.persistence.CascadeType;
import javax.persistence.OneToMany;
import java.util.List;

public class OffersArtists {
	@OneToMany(cascade = CascadeType.ALL)
	private List <Offer> offers;

	public OffersArtists(List<Offer> offers) {
		this.offers = offers;
	}

	public void getOffers(Money minFee, Money maxFee){

	}
	public void getOffer(Artist artist){

	}


}
