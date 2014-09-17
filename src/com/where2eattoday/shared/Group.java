package com.where2eattoday.shared;

import java.io.Serializable;
import java.util.ArrayList;

import javax.jdo.annotations.Index;
import javax.persistence.Embedded;
import javax.persistence.Id;

import com.googlecode.objectify.annotation.Entity;


@Entity
@Index // Active l'indexation par défaut
public class Group implements Serializable {
	@Id
	public Long id; // Google génèrera un ID automatiquement (car type Long)
	String name;
	@Embedded ArrayList<Restaurant> groupRestaurants = new ArrayList<Restaurant>();
	@Embedded ArrayList<RestaurantChoice> groupChoices = new ArrayList<RestaurantChoice>();
	ArrayList<LoginInfo> groupLoginInfos = new ArrayList<LoginInfo>();
	
	private Group(){}
	
	public Group(String name){
		this.name = name;
	}
	public long getGroupId (String name){
		return this.id;
	}
	public void addGroupRestaurant (Restaurant restaurant){
		groupRestaurants.add(restaurant);
	}
	public void setGroupRestaurants(ArrayList<Restaurant> groupRestaurants ) {
		this.groupRestaurants = groupRestaurants;
	}
	public void removeGroupRestaurant (Restaurant restaurant){
		for(int i=0;i<groupRestaurants.size();i++){
			if(groupRestaurants.get(i).getId()==restaurant.getId()){
				groupRestaurants.remove(restaurant);
			}
		}
		
	}
	public ArrayList<Restaurant> getGroupRestaurant(){
		return groupRestaurants;
	}
	public void addGroupChoice (RestaurantChoice restaurantChoice){
		groupChoices.add(restaurantChoice);
	}
	
	public ArrayList<RestaurantChoice> getGroupChoices(){
		return groupChoices;
	}

	public String getName() {
		return this.name;
	}

	public Long getId() {
		return this.id;
	}
	
	public ArrayList<LoginInfo> getGroupLoginInfos() {
		return this.groupLoginInfos;
	}
	public void setGroupLoginInfos(ArrayList<LoginInfo> groupLoginInfos ) {
		this.groupLoginInfos = groupLoginInfos;
	}
	public void addGroupLoginInfos(LoginInfo loginInfo ) {
		groupLoginInfos.add(loginInfo);
	}
	public void removeGroupLoginInfos(LoginInfo loginInfo ) {
		groupLoginInfos.remove(loginInfo);
	}
}
