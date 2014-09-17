package com.where2eattoday.client;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.where2eattoday.shared.Group;
import com.where2eattoday.shared.LoginInfo;
import com.where2eattoday.shared.Restaurant;

public interface GroupServiceAsync {
	void createGroup(String name, LoginInfo loginInfo, AsyncCallback callback);
	void addGroupRestaurant(Long idToAdd, Group myGroup, AsyncCallback callback);
	void listGroupsForUser(LoginInfo loginInfo,  AsyncCallback callback);
	void deleteGroupRestaurant(Group myGroup, Restaurant restaurant,
			AsyncCallback callback);
	void deleteGroup(Group tabGroup, AsyncCallback callback);
	void addLoginInfoToGroup(String emailAddress, Group group,AsyncCallback<ArrayList<LoginInfo>> callback);
	void removeLoginInfoFromGroup(LoginInfo loginInfo, Group group,AsyncCallback<ArrayList<LoginInfo>> callback);
	void listUsersForGroup(Group group,AsyncCallback<ArrayList<LoginInfo>> callback);
}
