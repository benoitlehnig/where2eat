package com.where2eattoday.client;

import java.util.ArrayList;
import java.util.Date;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.where2eattoday.shared.DishOfTheDay;
import com.where2eattoday.shared.Group;
import com.where2eattoday.shared.LoginInfo;
import com.where2eattoday.shared.Restaurant;
import com.where2eattoday.shared.RestaurantChoice;

/**
 * The client-side stub for the RPC service.
 */
@RemoteServiceRelativePath("group")
public interface GroupService extends RemoteService {
	Group createGroup (String name, LoginInfo loginInfo);
	Group addGroupRestaurant(Long idToAdd, Group myGroup);
	Group deleteGroupRestaurant(Group myGroup, Restaurant restaurant);
	ArrayList<Group> listGroupsForUser(LoginInfo loginInfo);
	void deleteGroup(Group tabGroup);
	ArrayList<LoginInfo> addLoginInfoToGroup(String emailAddress, Group group);
	ArrayList<LoginInfo> removeLoginInfoFromGroup(LoginInfo loginInfo, Group group);
	ArrayList<LoginInfo> listUsersForGroup(Group group);
}
