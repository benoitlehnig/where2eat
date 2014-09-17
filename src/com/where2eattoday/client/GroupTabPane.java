package com.where2eattoday.client;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.Heading;
import com.github.gwtbootstrap.client.ui.Row;
import com.github.gwtbootstrap.client.ui.TabLink;
import com.github.gwtbootstrap.client.ui.TabPanel;
import com.github.gwtbootstrap.client.ui.TabPane;
import com.github.gwtbootstrap.client.ui.constants.ButtonType;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;
import com.where2eattoday.shared.Group;
import com.where2eattoday.shared.LoginInfo;
import com.where2eattoday.shared.Restaurant;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.core.client.GWT;

public class GroupTabPane extends TabPane {
	Row mainRow = new Row();
	com.github.gwtbootstrap.client.ui.Column restaurantChoiceColumn = new com.github.gwtbootstrap.client.ui.Column(10);
	com.github.gwtbootstrap.client.ui.Column optionsColumn = new com.github.gwtbootstrap.client.ui.Column(4);
	Row restaurantsRow = new Row();
	Row usersRow = new Row();
	GroupContent groupContent;
	final FlowPanel RHS = new FlowPanel();
	LoginInfo loginInfo;
	TabLink tabLink = new TabLink();
	TabPanel tabPanel;
	Group tabGroup = null;
	final Button deleteGroup = new Button();
	final Button removeMySelfFromGroup = new Button();

	private final GroupServiceAsync groupService = GWT.create(GroupService.class);
	private final GreetingServiceAsync greetingService = GWT.create(GreetingService.class);
	
	Logger logger = Logger.getLogger("loggerUI");


	public GroupTabPane(final Group group, String name, TabPanel tabPanel, final LoginInfo loginInfo){
		this.setCreateTabLink(false);
		tabLink.setText(name);
		tabPanel.add(tabLink);
		tabLink.setTabPane(this);
		logger.log(Level.INFO, "Starting GroupTabPane".concat(group.getName()));
		this.tabGroup = group;
		this.tabPanel = tabPanel;
		this.loginInfo= loginInfo;
		this.setHeading(name);

		
		deleteGroup.setText("Delete Group");
		deleteGroup.setType(ButtonType.DANGER);

		setTodayChoice();
		getTodaysChoice();
		
		deleteGroup.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				AsyncCallback callback = new AsyncCallback<Void>() {

					public void onFailure(Throwable error) {
						 //
					}
					public void onSuccess(Void ignore) {
						removeTab();
					
					}
				};
						
				groupService.deleteGroup(tabGroup, callback);
			}
		});
		
	
		removeMySelfFromGroup.setText("Remove me from this group");
		removeMySelfFromGroup.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) { 
				AsyncCallback callback = new AsyncCallback<Void>() {
					public void onFailure(Throwable error) {
						 //
					}
		 
					public void onSuccess(Void ignore) {
						removeTab();
					}
				};
						
				groupService.removeLoginInfoFromGroup(loginInfo, tabGroup, callback);
			}
		});
		
		groupContent = new GroupContent();
		restaurantChoiceColumn.add(groupContent);
		
		
		GroupLoginTable groupLoginTable = new GroupLoginTable(tabGroup, loginInfo,this);
		usersRow.add(groupLoginTable);
		groupLoginTable.setLoginInfos(tabGroup.getGroupLoginInfos());
		
		
		GroupRestaurantTable groupRestaurantTable = new GroupRestaurantTable(tabGroup);
		groupRestaurantTable.setRestaurants(tabGroup.getGroupRestaurant());
		restaurantsRow.add(groupRestaurantTable);
		
		
		usersRow.add(removeMySelfFromGroup);
		
		optionsColumn.add(restaurantsRow);
		optionsColumn.add(usersRow);
		optionsColumn.add(deleteGroup);
		
		this.add(restaurantChoiceColumn);
		this.add(optionsColumn);
		
		
	}
	
	private void removeTab() {
		tabPanel.remove(tabLink);
		tabPanel.remove(this);
		tabPanel.selectTab(tabPanel.getSelectedTab()+1);
	}
	
	
	private void getTodaysChoice() {
		greetingService.getTodayChoice(tabGroup,
				new AsyncCallback<Restaurant>() {
					public void onFailure(Throwable caught) {
						//
					}

					public void onSuccess(Restaurant restaurant) {
						groupContent.setText(restaurant);
					}
				});
	}
	private void setTodayChoice() {
		greetingService.setTodayChoice(tabGroup,
				new AsyncCallback<Restaurant>() {
					public void onFailure(Throwable caught) {
						//
					}
					public void onSuccess(Restaurant restaurant) {
						groupContent.setText(restaurant);
					}
				});
	}
	
	
	
	
	public GroupContent getGroupContent(){
		return this.groupContent;
	}
}
