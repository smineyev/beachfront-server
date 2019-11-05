package mvw.server.data.service;

import java.util.Optional;

import mvw.server.data.entity.Buyer;

public interface AuctionSevice {
	public Optional<Buyer> findBuyerForDisplay(long creativeId);

	public Optional<Buyer> findBuyerForVideo(long videoid);
}
