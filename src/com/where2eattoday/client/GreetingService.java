package com.where2eattoday.client;

import java.util.ArrayList;
import java.util.Date;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.where2eattoday.shared.LoginInfo;
import com.where2eattoday.shared.DishOfTheDay;
import com.where2eattoday.shared.Group;
import com.where2eattoday.shared.Restaurant;
import com.where2eattoday.shared.RestaurantChoice;

/**
 * The client-side stub for the RPC service.
 */
@RemoteServiceRelativePath("greet")
public interface GreetingService extends RemoteService {
	String greetServer(String name) throws IllegalArgumentException;

	ArrayList<Restaurant> getListOfRestaurants();
	void  deleteRestaurants();
	Restaurant setTodayChoice(Group group);
	Restaurant getTodayChoice(Group group);
	Restaurant addRestaurant(String name);
	void deleteRestaurant(Restaurant restaurant);
	Restaurant addRestaurantFullDetails(String name, long latitude,
			long longitude, String address, String cuisine, String telephone);

	String getUserEmail(String token);

	LoginInfo login(String token, String requestUri);

	
}