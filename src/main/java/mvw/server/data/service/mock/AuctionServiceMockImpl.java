package mvw.server.data.service.mock;

import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import mvw.server.data.entity.Buyer;
import mvw.server.data.service.AuctionSevice;

@Configuration
public class AuctionServiceMockImpl implements AuctionSevice {

	@Value(value = "${spring.maxSleepTime}")
	private int maxSleepTime;
	
	public AuctionSevice auctionSevice() {
		return this;
	}
	
	@Override
	public Optional<Buyer> findBuyerForDisplay(long creativeId) {
		Random rnd = new Random();
		try {
			Thread.sleep(rnd.nextInt(maxSleepTime));
			return Optional.of(new Buyer(rnd.nextLong()));
		} catch (InterruptedException e) {
			System.out.println("interrupted");
			return Optional.empty();
		}
	}

	@Override
	public Optional<Buyer> findBuyerForVideo(long videoid) {
		Random rnd = new Random();
		try {
			Thread.sleep(rnd.nextInt(maxSleepTime));
			return Optional.of(new Buyer(rnd.nextLong()));
		} catch (InterruptedException e) {
			System.out.println("interrupted");
			return Optional.empty();
		}
	}

}
