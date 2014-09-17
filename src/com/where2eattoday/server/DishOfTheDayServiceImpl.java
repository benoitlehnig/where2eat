package com.where2eattoday.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.where2eattoday.client.DishOfTheDayService;
import com.where2eattoday.client.GreetingService;
import com.where2eattoday.client.GroupService;
import com.where2eattoday.shared.DishOfTheDay;
import com.where2eattoday.shared.FieldVerifier;
import com.where2eattoday.shared.Group;
import com.where2eattoday.shared.LoginInfo;
import com.where2eattoday.shared.LoginInfoGroup;
import com.where2eattoday.shared.Restaurant;
import com.where2eattoday.shared.RestaurantChoice;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.googlecode.objectify.*;

/**
 * The server-side implementation of the RPC service.
 * 
 */
@SuppressWarnings("serial")
public class DishOfTheDayServiceImpl extends RemoteServiceServlet implements
		DishOfTheDayService {
	
	Logger logger = Logger.getLogger("loggerServer");
	DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
	DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

	@Override
	public String greetServer(String name) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<DishOfTheDay> getListOfDishesOfTheDay(Restaurant restaurant) {
		ArrayList<DishOfTheDay> returnedList = new ArrayList<DishOfTheDay>();
		logger.log(Level.INFO, "getListOfDishesOfTheDay : ".concat(restaurant.getName()));
		Objectify ofy = ObjectifyService.begin();
		Query<DishOfTheDay> q  = ofy.query(DishOfTheDay.class).filter("restaurantId", restaurant.getId());
		Iterable<DishOfTheDay> dishOfTheDayIt = q.fetch();
		for (DishOfTheDay r : dishOfTheDayIt) {
			returnedList.add(r);
		}
		return returnedList;
	}

	@Override
	public ArrayList<DishOfTheDay> addDishOfTheDay(Restaurant restaurant,
			Date date, String dishOfTheDay) {
		logger.log(Level.INFO, "addDishOfTheDay".concat(restaurant.getName()));
		DishOfTheDay dishOfTheDaytoAdd= new DishOfTheDay(date,restaurant.getId(),dishOfTheDay);
		dishOfTheDaytoAdd.setStringDate(dateFormat.format(date));
		Objectify ofy = ObjectifyService.begin();
		ofy.put(dishOfTheDaytoAdd);
		
		return getListOfDishesOfTheDay(restaurant);
	}

	@Override
	public ArrayList<DishOfTheDay> updateDishOfTheDay(Restaurant restaurant,
			Date date, String dishOfTheDay) {
		// TODO Auto-generated method stub
		return null;
	}

}
