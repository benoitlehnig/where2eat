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
public interface DishOfTheDayServiceAsync {
	void greetServer(String input, AsyncCallback<String> callback)
			throws IllegalArgumentException;

	void getListOfDishesOfTheDay(Restaurant restaurant, AsyncCallback<ArrayList<DishOfTheDay>> callback);
	void addDishOfTheDay(Restaurant restaurant, Date date, String dishOfTheDay,AsyncCallback<ArrayList<DishOfTheDay>> callback);
	void updateDishOfTheDay(Restaurant restaurant, Date date, String dishOfTheDay,AsyncCallback<ArrayList<DishOfTheDay>> callback);
	
}
