//package prototype.hiring;
//
//import com.mysema.commons.lang.Assert;
//import org.javamoney.moneta.Money;
//import org.springframework.stereotype.Service;
//
//import javax.transaction.Transactional;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Service
//@Transactional
//public class OffersArtists {
//	private final OffersArtistRepository offersArtists;
//
//	public OffersArtists(OffersArtistRepository offersArtists) {
//		Assert.notNull(offersArtists, "OffersArtistRepository must not be null");
//		this.offersArtists = offersArtists;
//	}
//	public void getOffers(Money minFee, Money maxFee){
//		List<OfferArtist> offerArtists = offersArtists.findAll()
//				.filter(offerArtist -> offerArtist.getFeePerShow().isGreaterThanOrEqualTo(minFee) &
//										offerArtist.getFeePerShow().isLessThanOrEqualTo(maxFee))
//				.stream().collect(Collectors.toList());
//	}
//}
