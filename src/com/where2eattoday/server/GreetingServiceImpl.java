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

import com.google.appengine.labs.repackaged.org.json.JSONObject;
import com.where2eattoday.client.GreetingService;
import com.where2eattoday.shared.DishOfTheDay;
import com.where2eattoday.shared.FieldVerifier;
import com.where2eattoday.shared.Group;
import com.where2eattoday.shared.LoginInfo;
import com.where2eattoday.shared.LoginInfoGroup;
import com.where2eattoday.shared.Restaurant;
import com.where2eattoday.shared.RestaurantChoice;
import com.where2eattoday.shared.RestaurantGroup;
import com.where2eattoday.shared.UserGoogle;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.googlecode.objectify.*;
import com.google.gson.*;


/**
 * The server-side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class GreetingServiceImpl extends RemoteServiceServlet implements
		GreetingService {
	
	static {
		ObjectifyService.register(Restaurant.class);
		ObjectifyService.register(RestaurantChoice.class);
		ObjectifyService.register(Group.class);
		ObjectifyService.register(DishOfTheDay.class);
		ObjectifyService.register(LoginInfo.class);
		ObjectifyService.register(LoginInfoGroup.class);
		ObjectifyService.register(RestaurantGroup.class);
	}
	Logger logger = Logger.getLogger("loggerServer");
	private Random randomGenerator;
	private int lastTimeLimit = 11;
	
	DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	
	public void setRestaurantChoice (Group group, Restaurant restaurant){
		logger.log(Level.INFO, ">>starting setRestaurantChoice for: "
				.concat(group.getName())
				.concat(" and for restaurant : ")
				.concat(restaurant.getName())
				.concat(Long.toString(restaurant.getId()))
				);
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
		Date date = new Date();
		Objectify ofy = ObjectifyService.begin();
		// Simple create
		RestaurantChoice restaurantChoiceForToday= new RestaurantChoice(restaurant.getId(), date, group.getId());
		restaurantChoiceForToday.setStringDate(dateFormat.format(date));
		ofy.put(restaurantChoiceForToday);
		group.addGroupChoice(restaurantChoiceForToday);
		ofy.put(group);
		logger.log(Level.INFO, ">>end setRestaurantChoice");
	}

	private Restaurant getRandomRestaurant(Group group) {
		logger.log(Level.INFO, ">>Starting randomRestaurant for group : ".
				concat(group.getName()));
		Restaurant randomRestaurant = new Restaurant("");
		ArrayList<Restaurant> restaurants = group.getGroupRestaurant();
		if (restaurants!=null){
			Random ran = new Random();
			// Assumes max and min are non-negative.
			int randomInt = ran.nextInt(restaurants.size());
			randomRestaurant= restaurants.get(randomInt);
			logger.log(Level.INFO, ">>random getName : ".
					concat(group.getName())
					.concat(Long.toString(randomRestaurant.getId())));
		}
		return randomRestaurant;
	}
	
	
	public String greetServer(String input) throws IllegalArgumentException {
		// Verify that the input is valid. 
		if (!FieldVerifier.isValidName(input)) {
			// If the input is not valid, throw an IllegalArgumentException back to
			// the client.
			throw new IllegalArgumentException(
					"Name must be at least 4 characters long");
		}

		String serverInfo = getServletContext().getServerInfo();
		String userAgent = getThreadLocalRequest().getHeader("User-Agent");

		// Escape data from the client to avoid cross-site script vulnerabilities.
		input = escapeHtml(input);
		userAgent = escapeHtml(userAgent);

		return "Hello, " + input + "!<br><br>I am running " + serverInfo
				+ ".<br><br>It looks like you are using:<br>" + userAgent;
	}

	/**
	 * Escape an html string. Escaping data received from the client helps to
	 * prevent cross-site script vulnerabilities.
	 * 
	 * @param html the html string to escape
	 * @return the escaped string
	 */
	private String escapeHtml(String html) {
		if (html == null) {
			return null;
		}
		return html.replaceAll("&", "&amp;").replaceAll("<", "&lt;")
				.replaceAll(">", "&gt;");
	}

	@Override
	public ArrayList<Restaurant> getListOfRestaurants() {
		ArrayList<Restaurant> restaurants = new ArrayList<Restaurant> ( );
		Objectify ofy = ObjectifyService.begin();
		Query<Restaurant> q = 	ofy.query(Restaurant.class);
		Iterable<Restaurant> restaurantIt = q.fetch();
		for (Restaurant r : restaurantIt) {
			restaurants.add(r);
		}
		
		return restaurants;
	}

	@Override
	public void deleteRestaurants() {
		Objectify ofy = ObjectifyService.begin();
		Query<Restaurant> q = 	ofy.query(Restaurant.class);
		Iterable<Restaurant> restaurantIt = q.fetch();
		for (Restaurant r : restaurantIt) {
			ofy.delete(r);
		}
	}

	@Override
	public Restaurant setTodayChoice(Group group) {
		Date date = new Date();
		
		if (getTodayChoice(group)!= null) {
			throw new IllegalArgumentException(
					"The choice for today is already defined !");
		}
		Restaurant restaurant = new Restaurant("");
		restaurant = getRandomRestaurant(group);
		setRestaurantChoice(group, restaurant);	
		logger.log(Level.INFO, "RestaurantChoice: ".concat(restaurant.getName())
				.concat(" to ")
				.concat(group.getName()));
		
		return restaurant;
	}
	
	@Override
	public Restaurant getTodayChoice(Group group) {
		logger.log(Level.INFO, ">>starting getTodayChoice for: "
				.concat(group.getName()));
		Restaurant todayChoice = null;
		Long todayChoiceId = new Long(0);
		Objectify ofy = ObjectifyService.begin();
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
		Date date = new Date();
		
		Query<RestaurantChoice> q = ofy.query(RestaurantChoice.class)
				.filter("stringDate", dateFormat.format(date))
				.filter("groupId", group.getId());
		Iterable<RestaurantChoice> restaurantIt = q.fetch();
		for (RestaurantChoice r : restaurantIt) {
			todayChoiceId= r.getRestaurantId();
			logger.log(Level.INFO, "one restaurantChoice found");
			todayChoice = ofy.get(Restaurant.class, todayChoiceId);
			logger.log(Level.INFO, "todayChoice ".concat(todayChoice.getName()));
			
			//let's have the dish of the day.
			Query<DishOfTheDay> q2 = ofy.query(DishOfTheDay.class)
					.filter("restaurantId", r.getRestaurantId() )
					.filter("stringDate", dateFormat.format(date));
			
			
			if(q2.count()!=0){ 	
				Iterable<DishOfTheDay> dishOfTheDayIt = q2.fetch();
				for (DishOfTheDay r2 : dishOfTheDayIt) {
					todayChoice.setTodaysDishName(r2.getDishOfTheDay());
					
				}
			}
			else{
				todayChoice.setTodaysDishName("No dish defined");
			}
			
		}
		logger.log(Level.INFO, ">>end getTodayChoice for: "
				.concat(group.getName())
				.concat(": ")
				);
		return todayChoice;
	}

	@Override
	public Restaurant addRestaurant(String name) {
		Objectify ofy = ObjectifyService.begin();
		// Simple create
		Restaurant restaurant = new Restaurant(name);
		ofy.put(restaurant);
		return restaurant;
	}
	@Override
	public Restaurant addRestaurantFullDetails(String name, long latitude, long longitude, String address, String cuisine, String telephone) {
		Objectify ofy = ObjectifyService.begin();
		// Simple create
		Restaurant restaurant = new Restaurant(name, latitude, longitude, address, cuisine, telephone);
		ofy.put(restaurant);
		return restaurant;
	}

	@Override
	public void deleteRestaurant(Restaurant restaurant) {
		Objectify ofy = ObjectifyService.begin();
		Restaurant fetchedRestaurant = ofy.get(Restaurant.class, restaurant.getId());
		ofy.delete(fetchedRestaurant);
	}
	
	
	
	//user management
	@Override
	public String getUserEmail(final String token) {
		final UserService userService = UserServiceFactory.getUserService();
		final User user = userService.getCurrentUser();
		if (null != user) {
			return user.getEmail();
		} else {
			return "noreply@sample.com";
		}
	}

	@Override
	public LoginInfo login(final String token, final String requestUri) {
		logger.log(Level.INFO, ">>Starting login");
		final UserService userService = UserServiceFactory.getUserService();
		final User user = userService.getCurrentUser();
		final LoginInfo loginInfo = new LoginInfo();
		if (user != null) {
			logger.log(Level.INFO, "user email".concat(user.getEmail()));
			loginInfo.setLoggedIn(true);
			loginInfo.setName(user.getEmail());
			loginInfo.setEmailAddress(user.getEmail());
			loginInfo.setLogoutUrl(userService.createLogoutURL(requestUri));
			loginInfo.setPictureUrl(getPictureUrl(token, loginInfo));
			logger.log(Level.INFO, "login info defined");
			Objectify ofy = ObjectifyService.begin();
			if(isFirstlogin(loginInfo)){
				logger.log(Level.INFO, "Adding the user");
				ofy.put(loginInfo);
			}
			else{
				logger.log(Level.INFO, "Not a new the user");
				updatePicture(loginInfo, getPictureUrl(token, loginInfo));

				GroupServiceImpl groupServiceImpl = new GroupServiceImpl();
				logger.log(Level.INFO, "Nb of groups:".concat(Integer.toString(groupServiceImpl.listGroupsForUser(loginInfo).size())));
				loginInfo.setGroups(groupServiceImpl.listGroupsForUser(loginInfo));
			}
		} else {
			logger.log(Level.INFO, "user not logged in");
			loginInfo.setLoggedIn(false);
			loginInfo.setLoginUrl(userService.createLoginURL(requestUri));
		}
		
		
		logger.log(Level.INFO, "Ending Login");
		return loginInfo;
	}

	
	public ArrayList<LoginInfo> getGroupLoginInfos(Group group, LoginInfo loginInfo) {
		logger.log(Level.INFO, "starting getGroupLoginInfos for ".concat(Long.toString(group.getId())));
		Objectify ofy = ObjectifyService.begin();
		ArrayList<LoginInfo> groupLoginInfos = new ArrayList<LoginInfo>();	
		Query<LoginInfoGroup> q = ofy.query(LoginInfoGroup.class).filter("groupId",group.getId());
		Iterable<LoginInfoGroup> LoginInfoGroupIt = q.fetch();
		for (LoginInfoGroup r : LoginInfoGroupIt) {
			Query<LoginInfo> q2 = ofy.query(LoginInfo.class).filter("emailAddress",r.getLoginEmail());
			Iterable<LoginInfo> loginInfoIt = q2.fetch();
			for (LoginInfo r2 : loginInfoIt) {
				logger.log(Level.INFO, "found loginInfo:".concat(r2.getEmailAddress()));
				logger.log(Level.INFO, (Boolean.toString(loginInfo.getEmailAddress().equalsIgnoreCase(r2.getEmailAddress()))));
				if (!loginInfo.getEmailAddress().equalsIgnoreCase(r2.getEmailAddress())){
					groupLoginInfos.add(r2);
				}
			}
			
		}
		return  groupLoginInfos;
	}
	private boolean isFirstlogin(LoginInfo loginInfo) {
		boolean returnedCheck = false;
		Objectify ofy = ObjectifyService.begin();
		if (loginInfo.getEmailAddress() !=null){
			int count = ofy.query(LoginInfo.class).filter("emailAddress",loginInfo.getEmailAddress()).count();
			
			if(count ==0){
				returnedCheck = true;
			}
		}
		return returnedCheck;
	}

	private void updatePicture(LoginInfo loginInfo, String urlPicture) {
		Objectify ofy = ObjectifyService.begin();
		if (loginInfo.getEmailAddress() !=null){
			Query<LoginInfo> q2 = ofy.query(LoginInfo.class).filter("emailAddress",loginInfo.getEmailAddress());
			Iterable<LoginInfo> loginInfoIt = q2.fetch();
			for (LoginInfo r2 : loginInfoIt) {
				r2.setPictureUrl(urlPicture);
				ofy.put(r2);
				}
		}
	}
	
	public String getPictureUrl(final String token, LoginInfo loginInfo) {
		String url = "https://www.googleapis.com/oauth2/v1/userinfo?alt=json&access_token=" + token;
		logger.log(Level.INFO, url);
		final StringBuffer r = new StringBuffer();
		try {
			final URL u = new URL(url);
			final URLConnection uc = u.openConnection();
			final int end = 1000;
			InputStreamReader isr = null;
			BufferedReader br = null;
			try {
				isr = new InputStreamReader(uc.getInputStream());
				br = new BufferedReader(isr);
				final int chk = 0;
				while ((url = br.readLine()) != null) {
					if ((chk >= 0) && ((chk < end))) {
						r.append(url).append('\n');
					}
				}
			} catch (final java.net.ConnectException cex) {
				r.append(cex.getMessage());
			} catch (final Exception ex) {
				logger.log(Level.SEVERE, ex.getMessage());
			} finally {
				try {
					br.close();
				} catch (final Exception ex) {
					logger.log(Level.SEVERE, ex.getMessage());
				}
			}
		} catch (final Exception e) {
			logger.log(Level.SEVERE, e.getMessage());
		}
		System.out.println(r);
		
		Gson gson = new GsonBuilder().create();
		UserGoogle u= gson.fromJson(r.toString(), UserGoogle.class);

		return u.getPicture();
	}

}
