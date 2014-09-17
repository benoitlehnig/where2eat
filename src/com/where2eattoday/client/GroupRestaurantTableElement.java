package com.where2eattoday.client;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.Dropdown;
import com.github.gwtbootstrap.client.ui.Image;
import com.github.gwtbootstrap.client.ui.NavLink;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Widget;
import com.where2eattoday.shared.Group;
import com.where2eattoday.shared.LoginInfo;
import com.where2eattoday.shared.Restaurant;

public class GroupRestaurantTableElement extends Composite implements HasText {
	Logger logger = Logger.getLogger("loggerUI");
	private final GroupServiceAsync groupService = GWT.create(GroupService.class);
	private static GroupRestaurantTableElementUiBinder uiBinder = GWT
			.create(GroupRestaurantTableElementUiBinder.class);

	interface GroupRestaurantTableElementUiBinder extends
			UiBinder<Widget, GroupRestaurantTableElement> {
	}

	Group tabGroup;
	GroupRestaurantTable groupRestaurantTable;
	Restaurant restaurant;
	
	@UiField
	Image picture;
	
	@UiField
	Dropdown restaurantField;

	@UiField
	NavLink remove;
	
	public GroupRestaurantTableElement(Restaurant restaurantElement, Group group, GroupRestaurantTable groupRestaurantTable) {
		initWidget(uiBinder.createAndBindUi(this));
		this.tabGroup = group;
		this.groupRestaurantTable= groupRestaurantTable;
		this.restaurant = restaurantElement;
		restaurantField.setText(restaurantElement.getName());
	}
	@UiHandler("remove")
	void onClick(ClickEvent e) {
		AsyncCallback callback = new AsyncCallback<Group>() {
			public void onFailure(Throwable error) {
				
			}
 
			public void onSuccess(Group returnedGroup) {
				tabGroup = returnedGroup;
				groupRestaurantTable.setRestaurants(returnedGroup.getGroupRestaurant());
				logger.log(Level.INFO, "group returned contains:".concat(Integer.toString(returnedGroup.getGroupRestaurant().size())));
			}
		};
		groupService.deleteGroupRestaurant(tabGroup,restaurant,callback);
	}
	




	@Override
	public String getText() {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public void setText(String text) {
		// TODO Auto-generated method stub
		
	}

	
}
