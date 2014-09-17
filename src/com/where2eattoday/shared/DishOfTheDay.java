package com.where2eattoday.shared;
import java.io.Serializable;
import java.util.Date;

import javax.persistence.Id;

public class DishOfTheDay implements Serializable {
	@Id Long id; // Google génèrera un ID automatiquement (car type Long)
	Long restaurantId;
	Date date;
	String stringDate;
	String dishOfTheDay;
	
	
	private DishOfTheDay(){}
	
	public DishOfTheDay (Date date,Long restaurantId,String dishOfTheDay ){
		this.date = date;
		this.restaurantId = restaurantId;
		this.dishOfTheDay = dishOfTheDay;
	}
	
	public String getDishOfTheDay(){
		return this.dishOfTheDay;
	}
	public Long getRestaurantId(){
		return this.restaurantId;
	}
	public Date getDate(){
		return this.date;
	}
	public String setDishOfTheDay(){
		return this.dishOfTheDay;
	}
	public String getStringDate(){
		return this.stringDate;
	}
	public void setStringDate(String stringDate){
		this.stringDate = stringDate;
	}

}
