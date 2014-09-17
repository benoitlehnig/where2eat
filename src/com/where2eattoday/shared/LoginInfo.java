package com.where2eattoday.shared;


import java.io.Serializable;
import java.util.ArrayList;

import javax.jdo.annotations.Index;
import javax.persistence.Embedded;
import javax.persistence.Id;

import com.googlecode.objectify.annotation.Entity;

@Entity
@Index // Active l'indexation par défaut
public class LoginInfo implements Serializable {

	@Id Long id; // Google génèrera un ID automatiquement (car type Long)

	ArrayList<Long> groupIds =new ArrayList<Long>();
	
	ArrayList<Group> groups =new ArrayList<Group>();
	
	private boolean loggedIn = false;

	private String loginUrl;

	private String logoutUrl;

	private String emailAddress;

	private String nickname;

	private String pictureUrl;

	
	public LoginInfo(){
		
	}
	public boolean isLoggedIn() {
		return loggedIn;
	}
	
	
	

	public void setLoggedIn(final boolean loggedIn) {
		this.loggedIn = loggedIn;
	}

	public String getLoginUrl() {
		return loginUrl;
	}

	public void setLoginUrl(final String loginUrl) {
		this.loginUrl = loginUrl;
	}

	public String getLogoutUrl() {
		return logoutUrl;
	}

	public void setLogoutUrl(final String logoutUrl) {
		this.logoutUrl = logoutUrl;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(final String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getName() {
		return nickname;
	}

	public void setName(final String nickname) {
		this.nickname = nickname;
	}

	public void setPictureUrl(final String pictureUrl) {
		this.pictureUrl = pictureUrl;

	}
	
	public long getId() {
		return id;
	}

	public String getPictureUrl() {
		return pictureUrl;
	}
	
	public ArrayList<Long> getGroupsId() {
		return  groupIds;
	}
	public ArrayList<Group> getGroups() {
		return  groups;
	}
	public void setGroups(ArrayList<Group> ArrayGroups) {
		 this.groups = ArrayGroups;
	}
	public void addGroupId(Long groupId) {
		groupIds.add(groupId);
	}
	public void deleteGroupId(Long id) {
		groupIds.remove(id);
		
	}

}
