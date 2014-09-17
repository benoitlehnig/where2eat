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
@RemoteServiceRelativePath("dishOfTheDay")
public interface DishOfTheDayService extends RemoteService {
	String greetServer(String name) throws IllegalArgumentException;

	ArrayList<DishOfTheDay> getListOfDishesOfTheDay(Restaurant restaurant);
	ArrayList<DishOfTheDay> addDishOfTheDay(Restaurant restaurant, Date date, String dishOfTheDay);
	ArrayList<DishOfTheDay> updateDishOfTheDay(Restaurant restaurant, Date date, String dishOfTheDay);
	
}