package com.where2eattoday.client;

import java.util.Date;

import com.github.gwtbootstrap.client.ui.Heading;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Widget;
import com.where2eattoday.shared.Restaurant;

public class GroupContent extends Composite   {

	private static GroupContentUiBinder uiBinder = GWT
			.create(GroupContentUiBinder.class);

	interface GroupContentUiBinder extends UiBinder<Widget, GroupContent> {
	}

	public GroupContent() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@UiField
	Heading restaurantName;

	@UiField
	Heading dishName;
	
	@UiField
	HTMLPanel circle;
	
	
	public GroupContent(String restName, String dishOfTheDayName) {
		initWidget(uiBinder.createAndBindUi(this));
		restaurantName.setText(restName);
		dishName.setText(dishOfTheDayName);
	}


	public void setText(String restName, String dishOfTheDayName) {
		restaurantName.setText(restName);
		dishName.setText(dishOfTheDayName);
	}
	public void setText(Restaurant restaurant) {
		if(restaurant !=null){
			restaurantName.setText(restaurant.getName());
			if(dishName !=null){
				dishName.setText(restaurant.getsetTodaysDishName());
			}
			else{
				dishName.setText("No dish of the day defined");
			}
			//RootPanel.get("nameFieldContainer").remove(defineTodayChoice);
		}
		else{
			restaurantName.setText("No restaurant defined yet for today");
			dishName.setText("");
		}
	}
	
	public HTMLPanel getCircle() {
		return circle;
	}
}
