package com.where2eattoday.shared;

public class UserGoogle {
	String id;
	String name;
	String given_name;
	String family_name;
	String link;
	String picture;
	String gender;
	
	@Override
	    public String toString() {
	        return name + " - " + given_name;
	    }

	public String getPicture() {
		// TODO Auto-generated method stub
		return picture;
	}
}
