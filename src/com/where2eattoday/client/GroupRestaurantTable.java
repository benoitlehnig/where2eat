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
import com.github.gwtbootstrap.client.ui.ListBox;
import com.github.gwtbootstrap.client.ui.Typeahead;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.Widget;
import com.where2eattoday.shared.Group;
import com.where2eattoday.shared.LoginInfo;
import com.where2eattoday.shared.Restaurant;

public class GroupRestaurantTable extends Composite implements HasText {
	Logger logger = Logger.getLogger("loggerUI");
	private final GreetingServiceAsync greetingService = GWT.create(GreetingService.class);
	private final GroupServiceAsync groupService = GWT.create(GroupService.class);
	private static GroupRestaurantTableUiBinder uiBinder = GWT
			.create(GroupRestaurantTableUiBinder.class);

	interface GroupRestaurantTableUiBinder extends
			UiBinder<Widget, GroupRestaurantTable> {
	}

	public GroupRestaurantTable() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@UiField
	Button addRestaurantButton;

	@UiField
	HTMLPanel listOfRestaurants;
	
	@UiField
	ListBox restaurantListBox;
	 
	MultiWordSuggestOracle oracle; 
	
	Group tabGroup;
	
	public GroupRestaurantTable(Group tabGroup) {
		initWidget(uiBinder.createAndBindUi(this));
		this.tabGroup = tabGroup;
		getListOfRestaurantsForGroup();
		
	}

	private void populateSelectBox(ArrayList<Restaurant> result) {
		restaurantListBox.clear();
		//populate it
		for(int i = 0; i < result.size(); i++){
			final Restaurant currentRestaurant = result.get(i);
			if(!tabGroup.getGroupRestaurant().contains(currentRestaurant)){
				restaurantListBox.addItem(currentRestaurant.getName(), Long.toString(currentRestaurant.getId()));
			}
		}		
	}
	

	private void getListOfRestaurantsForGroup() {
		greetingService.getListOfRestaurants(
				new AsyncCallback<ArrayList<Restaurant>>() {
					public void onFailure(Throwable caught) {
					}
					public void onSuccess(ArrayList<Restaurant> result) {
						populateSelectBox(result);
					}
				});
	}

	
	@UiHandler("addRestaurantButton")
	void onClick(ClickEvent e) {
		
		Long idToAdd = Long.parseLong(restaurantListBox.getValue(restaurantListBox.getSelectedIndex())); 
		AsyncCallback callback = new AsyncCallback<Group>() {

			public void onFailure(Throwable error) {
				 //
			}
 
			public void onSuccess(Group returnedGroup) {
				tabGroup = returnedGroup;
				logger.log(Level.INFO, "group returned contains:".concat(Integer.toString(returnedGroup.getGroupRestaurant().size())));
				setRestaurants(returnedGroup.getGroupRestaurant());
				getListOfRestaurantsForGroup();
				
			}
			
		};
		groupService.addGroupRestaurant(idToAdd, tabGroup, callback);
	}

	public void setText(String text) {
		addRestaurantButton.setText(text);
	}

	public String getText() {
		return addRestaurantButton.getText();
	}

	public void setRestaurants(ArrayList<Restaurant> restaurants) {
	listOfRestaurants.clear();
	for(int i=0;i<restaurants.size();i++){
		GroupRestaurantTableElement restaurantElement = new GroupRestaurantTableElement (restaurants.get(i), tabGroup,this);
		listOfRestaurants.add(restaurantElement);
		}
	}
}

