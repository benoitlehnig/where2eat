package com.where2eattoday.client;

import java.util.ArrayList;
import java.util.Date;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.where2eattoday.shared.DishOfTheDay;
import com.where2eattoday.shared.Group;
import com.where2eattoday.shared.LoginInfo;
import com.where2eattoday.shared.Restaurant;
import com.where2eattoday.shared.RestaurantChoice;
/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface GreetingServiceAsync {
	void greetServer(String input, AsyncCallback<String> callback)
			throws IllegalArgumentException;

		
	void getListOfRestaurants  (AsyncCallback callback);
	
	void deleteRestaurants (AsyncCallback callback);
	
	void addRestaurant(String name, AsyncCallback<Restaurant> callback);
	void addRestaurantFullDetails(String name, long latitude, long longitude, String address, String cuisine, String telephone, AsyncCallback<Restaurant> callback);
	
	void setTodayChoice (Group group, AsyncCallback<Restaurant> callback);
	void getTodayChoice (Group group, AsyncCallback<Restaurant> callback);

	void deleteRestaurant(Restaurant restaurant, AsyncCallback callback);

	void getUserEmail(String token, AsyncCallback callback);
	void login(String token, String requestUri, AsyncCallback callback);

	
}
