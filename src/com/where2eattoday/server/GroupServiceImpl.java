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

import com.where2eattoday.client.GreetingService;
import com.where2eattoday.client.GroupService;
import com.where2eattoday.shared.DishOfTheDay;
import com.where2eattoday.shared.FieldVerifier;
import com.where2eattoday.shared.Group;
import com.where2eattoday.shared.LoginInfo;
import com.where2eattoday.shared.LoginInfoGroup;
import com.where2eattoday.shared.Restaurant;
import com.where2eattoday.shared.RestaurantChoice;
import com.where2eattoday.shared.RestaurantGroup;
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
public class GroupServiceImpl extends RemoteServiceServlet implements
		GroupService {
	
	Logger logger = Logger.getLogger("loggerServer");
	
	DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

	public int getCountForRestaurant(Restaurant restaurant, Group group) {
		logger.log(Level.INFO, ">>starting getCountForRestaurant for: "
				.concat(restaurant.getName())
				.concat(Long.toString(restaurant.getId()))
				.concat(group.getName())
				.concat(Long.toString(group.getId())));
		int count =0;
		Objectify ofy = ObjectifyService.begin();
		try {
			Query<RestaurantChoice> q = ofy.query(RestaurantChoice.class)
					.filter("restaurantId", restaurant.getId())
					.filter("groupId", group.getId());
				count = q.count();
		} catch (RuntimeException ex) {
			//todo
		}
		logger.log(Level.INFO, "  count: "
				.concat(Integer.toString(count)));
		return count;
	}
	

	@Override
	public Group createGroup(String name, LoginInfo loginInfo) {
		logger.log(Level.INFO, ">>createGroup : ".concat(name));
		Objectify ofy = ObjectifyService.begin();
		// Simple create
		Group group = new Group(name);
		ofy.put(group);
		assert group.id != null;
		logger.log(Level.INFO, ">>addGroupToUser : ".concat(Long.toString(group.getId())));
		addGroupToUser(group, loginInfo);
		return group;
	}
	
	public void addGroupToUser(Group group, LoginInfo loginInfo) {
		logger.log(Level.INFO, ">>addGroupToUser : ".concat(loginInfo.getEmailAddress()));
		Objectify ofy = ObjectifyService.begin();
		LoginInfoGroup loginInfoGroup = new LoginInfoGroup(loginInfo.getEmailAddress(),group.getId());
		ofy.put(loginInfoGroup);	
	}
	
	@Override
	public Group addGroupRestaurant(Long restaurantId, Group myGroup) {
		logger.log(Level.INFO, "Adding: ".concat(Long.toString(restaurantId)
				.concat(" to ")
				.concat(myGroup.getName())
				.concat("with the nb of restaurants: ")
				.concat(Integer.toString(myGroup.getGroupRestaurant().size()))));
		Objectify ofy = ObjectifyService.begin();
		RestaurantGroup restaurantGroup = new RestaurantGroup(restaurantId, myGroup.getId());
		ofy.put(restaurantGroup);
		
		return getGroupDetails(myGroup.getId());
	}
	
	public Group deleteGroupRestaurant(Group myGroup, Restaurant restaurant){
		logger.log(Level.INFO, "Deleting: ".concat(restaurant.getName())
				.concat(" from ")
				.concat(myGroup.getName())
				.concat("with the nb of restaurants: ")
				.concat(Integer.toString(myGroup.getGroupRestaurant().size())));
		Objectify ofy = ObjectifyService.begin();
		Query<RestaurantGroup> q  = ofy.query(RestaurantGroup.class).filter("groupId", myGroup.getId()).filter("restaurantId", restaurant.getId());
		Iterable<RestaurantGroup> restaurantGroupIt = q.fetch();
		for (RestaurantGroup r : restaurantGroupIt) {
			ofy.delete(r);	
		}
		
		return getGroupDetails(myGroup.getId());
	}

	@Override
	public ArrayList<Group> listGroupsForUser(LoginInfo loginInfo) {
		logger.log(Level.INFO, "listGroupsForUser for: ".concat(loginInfo.getEmailAddress()));
		ArrayList<Group> returnedGroups = new ArrayList<Group>();	
		Objectify ofy = ObjectifyService.begin();
		Query<LoginInfoGroup> q  = ofy.query(LoginInfoGroup.class).filter("loginEmail", loginInfo.getEmailAddress());
		Iterable<LoginInfoGroup> loginInfoGroupIt = q.fetch();
		logger.log(Level.INFO, "nb of groups found : ".concat(Integer.toString(q.count())));
		for (LoginInfoGroup r : loginInfoGroupIt) {
			Long currentGroupId = r.getGroupId();
			Group currentGroup = getGroupDetails(currentGroupId);
			returnedGroups.add(currentGroup);
		}		
		return returnedGroups;
	}

	@Override
	public void deleteGroup(Group group) {
		logger.log(Level.INFO, "deleteGroup : ".concat(group.getName()));
		Objectify ofy = ObjectifyService.begin();
		Query<LoginInfoGroup> q  = ofy.query(LoginInfoGroup.class).filter("groupId", group.getId());
		Iterable<LoginInfoGroup> loginInfoGroupIt = q.fetch();
		for (LoginInfoGroup r : loginInfoGroupIt) {
			ofy.delete(r);	
		}
		Query<RestaurantGroup> r  = ofy.query(RestaurantGroup.class).filter("groupId", group.getId());
		Iterable<RestaurantGroup> restaurantGroupIt = r.fetch();
		for (RestaurantGroup v : restaurantGroupIt) {
			ofy.delete(v);	
		}
		ofy.delete(group);
	}
	@Override
	public ArrayList<LoginInfo>  addLoginInfoToGroup(String emailAddress, Group group) {
		logger.log(Level.INFO, ">> starting addLoginInfoToGroup : ".concat(emailAddress));
		Objectify ofy = ObjectifyService.begin();
		//does loginInfo exit for this user.
		int countLoginInfo = ofy.query(LoginInfo.class).filter("emailAddress", emailAddress).count();
		//user does not exit in in the DB, we have to create him a login
		if(countLoginInfo==0){
			LoginInfo newLoginInfo = new LoginInfo ();
			newLoginInfo.setEmailAddress(emailAddress);	
			ofy.put(newLoginInfo);	
		}
		LoginInfoGroup loginInfoGroup = new LoginInfoGroup(emailAddress,group.getId());
		logger.log(Level.INFO, "addLoginInfoToGroup : ".concat(group.getName()));
		ofy.put(loginInfoGroup);	
		return listUsersForGroup(group);
	}
	@Override
	public ArrayList<LoginInfo> removeLoginInfoFromGroup(LoginInfo loginInfo, Group group) {
		logger.log(Level.INFO, "removeLoginInfoFromGroup : ".concat(group.getName()));
		Objectify ofy = ObjectifyService.begin();
		Query<LoginInfoGroup> q  = ofy.query(LoginInfoGroup.class).filter("groupId", group.getId()).filter("loginEmail", loginInfo.getEmailAddress());
		Iterable<LoginInfoGroup> loginInfoGroupIt = q.fetch();
		for (LoginInfoGroup r : loginInfoGroupIt) {
			ofy.delete(r);	
		}
		return listUsersForGroup(group);
	}
	
	@Override
	public ArrayList<LoginInfo> listUsersForGroup(Group group) {
		ArrayList<LoginInfo> returnedLoginInfo = new ArrayList<LoginInfo>();
		logger.log(Level.INFO, ">>Starting listUsersForGroup : ".concat(group.getName()));
		Objectify ofy = ObjectifyService.begin();
		Query<LoginInfoGroup> q  = ofy.query(LoginInfoGroup.class).filter("groupId", group.getId());
		Iterable<LoginInfoGroup> loginInfoGroupIt = q.fetch();
		logger.log(Level.INFO, "count of loginInfo : ".concat(Integer.toString(q.count())));
		for (LoginInfoGroup r : loginInfoGroupIt) {
			Query<LoginInfo> q2 = ofy.query(LoginInfo.class).filter("emailAddress",r.getLoginEmail());
			Iterable<LoginInfo> loginInfoIt = q2.fetch();
			for (LoginInfo r2 : loginInfoIt) {
				returnedLoginInfo.add(r2);
			}
		}
		return  returnedLoginInfo;
	}
	
	
	public ArrayList<Restaurant> listRestaurantsForGroup(Group group) {
		ArrayList<Restaurant> returnedRestaurants = new ArrayList<Restaurant>();
		Objectify ofy = ObjectifyService.begin();
		Query<RestaurantGroup> q  = ofy.query(RestaurantGroup.class).filter("groupId", group.getId());
		Iterable<RestaurantGroup> restaurantGroupIt = q.fetch();
		for (RestaurantGroup r : restaurantGroupIt) {
			Restaurant rest  = ofy.get(new Key<Restaurant>(Restaurant.class, r.getRestaurantId()));
			returnedRestaurants.add(rest);
		}
		return returnedRestaurants;
	}
	
	
	
	public Group getGroupDetails(Long groupId) {
		Objectify ofy = ObjectifyService.begin();
		Group group = ofy.get(new Key<Group>(Group.class, groupId));
		if(group!=null){
			group.setGroupLoginInfos(listUsersForGroup(group));
			group.setGroupRestaurants(listRestaurantsForGroup(group));
		}
		
		return  group;
	}
}
