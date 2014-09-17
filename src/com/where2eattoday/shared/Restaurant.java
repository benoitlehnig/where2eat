package com.where2eattoday.shared;
import java.io.Serializable;
import java.util.Date;

import javax.jdo.annotations.Index;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.googlecode.objectify.annotation.*;

@Entity
@Index // Active l'indexation par défaut
public class Restaurant implements Serializable {
	@Id Long id; // Google génèrera un ID automatiquement (car type Long)
	String name;
	int numberOfOccurence;
	long latitude;
	long longitude;
	String address;
	String cuisine;
	String telephone;
	String website;
	boolean isGroupRestaurant = false;
	String groupRestaurant;
	String todaysDishName;
	
	private Restaurant (){}
	
	public Restaurant (String name){
		this.name = name;	
	}
	public Restaurant (String name, String groupRestaurant){
		this.name = name;	
		this.groupRestaurant= groupRestaurant;
		this.isGroupRestaurant = true;
	}
	public Restaurant (String name, long latitude, long longitude, String address, String cuisine, String telephone){
		this.name = name;	
		this.latitude=latitude;
		this.longitude=longitude;
		this.address = address;
		this.cuisine = cuisine;
		this.telephone = telephone;
		this.website= website;
		this.groupRestaurant = groupRestaurant;
	}
	public Restaurant (String name, long latitude, long longitude, String address, String cuisine, String telephone, String website, String groupRestaurant){
		this.name = name;	
		this.latitude=latitude;
		this.longitude=longitude;
		this.address = address;
		this.cuisine = cuisine;
		this.telephone = telephone;
		this.website= website;
		this.groupRestaurant = groupRestaurant;
		this.isGroupRestaurant = true;
	}
	public String getName(){
		return this.name;
	}
	public String getAddress(){
		return this.address;
	}
	
	public Long getId(){
		return id;
	}
	
	public int getNumberOfOccurence(){
		return numberOfOccurence;
	}
	public void setNumberOfOccurence(int nb){
		numberOfOccurence=nb ;
	}
	
	public long getLongitude(){
		return longitude;
	}
	public void setLongitude(long longitude){
		this.longitude= longitude ;
	}
	public long getLatitude(){
		return latitude;
	}
	public void setLatitude(long latitude){
		this.latitude= latitude ;
	}
	public String getTelephone(){
		return telephone;
	}
	public void setTelephone(String telephone){
		this.telephone= telephone ;
	}
	public String getWebsite(){
		return website;
	}
	public void setWebsite(String website){
		this.website= website ;
	}
	public String getCuisine(){
		return cuisine;
	}
	public void setCuisine(String cuisine){
		this.cuisine= cuisine ;
	}

	public void setTodaysDishName( String dishName) {
		this.todaysDishName = dishName;
	}
	public String getsetTodaysDishName() {
		return todaysDishName;
	}
}
