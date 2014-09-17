package com.where2eattoday.shared;


import java.io.Serializable;
import java.util.ArrayList;

import javax.jdo.annotations.Index;
import javax.persistence.Embedded;
import javax.persistence.Id;

import com.googlecode.objectify.annotation.Entity;

@Entity
@Index // Active l'indexation par défaut
public class LoginInfoGroup implements Serializable {

	@Id Long id; // Google génèrera un ID automatiquement (car type Long)

	String loginEmail;
	Long groupId;

	
	public LoginInfoGroup(){
		
	}

	public LoginInfoGroup(String loginEmail, Long groupId ){
		this.loginEmail = loginEmail;
		this.groupId = groupId;
	}
	
	public Long getGroupId( ){
		return groupId;
	}

	public String getLoginEmail( ){
		return loginEmail;
	}
	public void setGroupId(Long groupId ){
		this.groupId = groupId;
	}
	
}
