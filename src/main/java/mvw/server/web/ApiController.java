package mvw.server.web;

import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicReferenceArray;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import mvw.server.data.entity.Buyer;
import mvw.server.data.entity.Cookie;
import mvw.server.data.entity.User;
import mvw.server.data.service.AuctionSevice;
import mvw.server.data.service.CookieService;
import mvw.server.data.service.UserService;

@RestController
public class ApiController {
	private static final Logger logger = LoggerFactory.getLogger(ApiController.class);
	
	@Autowired
	private ExecutorService taskExecutor;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private AuctionSevice auctionService;
	
	@Autowired
	private CookieService cookieService;
	
	
	@GetMapping("/getDisplay")
	public @ResponseBody DeferredResult<ResponseEntity<String>> getDisplay(
			@RequestParam long userid, 
			@RequestParam long timeout, 
			@RequestParam long displayid) {
		var deferredRes = new DeferredResult<ResponseEntity<String>>(timeout);
		var threadResults = new AtomicReferenceArray<Object>(3);
		
		var futureUser = submitTask(0, deferredRes, threadResults, ()-> userService.findUser(userid));
		var futureAuction = submitTask(1, deferredRes, threadResults, ()-> auctionService.findBuyerForDisplay(displayid));
		var futureCookies = submitTask(2, deferredRes, threadResults, ()-> cookieService.findCookies(userid));
		
		deferredRes.onTimeout(() -> { 
			logger.warn("req timeout");
			
			deferredRes.setErrorResult(
			    ResponseEntity
			    	.status(HttpStatus.NO_CONTENT).build());
			
			futureUser.cancel(true);
			futureAuction.cancel(true);
			futureCookies.cancel(true);
		} );
		
		return deferredRes;
	}

	@GetMapping("/getVideo")
	public @ResponseBody DeferredResult<ResponseEntity<String>> getVideo(
			@RequestParam long userid, 
			@RequestParam long timeout, 
			@RequestParam long videoid) {
		var deferredRes = new DeferredResult<ResponseEntity<String>>(timeout);
		var threadResults = new AtomicReferenceArray<Object>(3);
		
		var futureUser = submitTask(0, deferredRes, threadResults, ()-> userService.findUser(userid));
		var futureAuction = submitTask(1, deferredRes, threadResults, ()-> auctionService.findBuyerForVideo(videoid));
		var futureCookies = submitTask(2, deferredRes, threadResults, ()-> cookieService.findCookies(userid));
		
		deferredRes.onTimeout(() -> { 
			logger.warn("req timeout");
			
			deferredRes.setErrorResult(
			    ResponseEntity
			    	.status(HttpStatus.NO_CONTENT).build());
			
			futureUser.cancel(true);
			futureAuction.cancel(true);
			futureCookies.cancel(true);
		} );
		
		return deferredRes;		
	}	

	private Future<?> submitTask(int localTaskNum,
			DeferredResult<ResponseEntity<String>> httpRes,
			AtomicReferenceArray<Object> threadResults,
			Supplier<?> taskCallable) {
		return taskExecutor.submit(() -> {
			var taskRes = taskCallable.get();
			if (!Thread.interrupted()) {
				threadResults.set(localTaskNum, taskRes);
				aplpyResultIfAllFinished(httpRes, threadResults);
			}
		});
	}


	@SuppressWarnings("unchecked")
	private void aplpyResultIfAllFinished(
			DeferredResult<ResponseEntity<String>> httpRes, 
			AtomicReferenceArray<Object> threadResults) {
		for (int i=0; i<3; i++) {
			if (threadResults.get(i) == null) {
				return;
			}
		}
		
		Optional<User> user = (Optional<User>) threadResults.get(0);
		Optional<Buyer> buyer = (Optional<Buyer>) threadResults.get(1);
		Collection<Cookie> cookies = (Collection<Cookie>) threadResults.get(2);
		
		String body = "{ user_id: " + user.get().getId() + 
						", buyer_id: "+buyer.get().getId() + 
						", num_of_cookies: "+cookies.size() + " }";
		httpRes.setResult(ResponseEntity.ok(body));
		
		logger.info("req ok");
	}
}
