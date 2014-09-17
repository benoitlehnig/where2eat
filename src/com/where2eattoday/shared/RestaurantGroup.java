package com.where2eattoday.shared;


import java.io.Serializable;
import java.util.ArrayList;

import javax.jdo.annotations.Index;
import javax.persistence.Embedded;
import javax.persistence.Id;

import com.googlecode.objectify.annotation.Entity;

@Entity
@Index // Active l'indexation par défaut
public class RestaurantGroup implements Serializable {

	@Id Long id; // Google génèrera un ID automatiquement (car type Long)

	Long restaurantId;
	Long groupId;

	
	public RestaurantGroup(){
		
	}

	public RestaurantGroup(Long restaurantId, Long groupId ){
		this.restaurantId = restaurantId;
		this.groupId = groupId;
	}
	
	public Long getGroupId( ){
		return groupId;
	}

	public Long getRestaurantId( ){
		return restaurantId;
	}
	public void setGroupId(Long groupId ){
		this.groupId = groupId;
	}
	public void setRestaurantId(Long restaurantId ){
		this.restaurantId = restaurantId;
	}
}
