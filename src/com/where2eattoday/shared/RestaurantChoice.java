package com.where2eattoday.shared;


import java.io.Serializable;
import java.util.Date;

import javax.persistence.Id;



import com.googlecode.objectify.annotation.Entity;

@Entity
public class RestaurantChoice  implements Serializable{
	@Id Long id; // Google génèrera un ID automatiquement (car type Long)
	Long restaurantId;
	Date date;
	String stringDate;
	Long groupId;
	
	private RestaurantChoice(){}
	
	public RestaurantChoice (Long id, Date date, Long groupId){
		this.date = date;
		this.restaurantId = id;
		this.groupId = groupId;
	}
	public Date getDate(){
		return this.date;
	}
	public String getStringDate(){
		return this.stringDate;
	}
	public void setStringDate(String stringDate){
		this.stringDate = stringDate;
	}
	public Long getRestaurantId(){
		return this.restaurantId;
	}
	public Long getGroupId(){
		return this.groupId;
	}
}
