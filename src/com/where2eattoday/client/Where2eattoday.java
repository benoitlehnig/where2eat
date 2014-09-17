package com.where2eattoday.client;

import java.util.ArrayList;


import java.util.logging.Level;
import java.util.logging.Logger;

import com.where2eattoday.shared.DishOfTheDay;
import com.where2eattoday.shared.Group;
import com.where2eattoday.shared.LoginInfo;
import com.where2eattoday.shared.Restaurant;
import com.github.gwtbootstrap.client.ui.ButtonCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.github.gwtbootstrap.client.ui.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.github.gwtbootstrap.client.ui.ListBox;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.RootPanel;
import com.github.gwtbootstrap.client.ui.TextBox;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.api.gwt.oauth2.client.Auth;
import com.google.api.gwt.oauth2.client.AuthRequest;
import com.google.api.gwt.oauth2.client.Callback;
import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.constants.Alignment;
import com.github.gwtbootstrap.client.ui.constants.BaseIconType;
import com.github.gwtbootstrap.client.ui.constants.ButtonType;
import com.github.gwtbootstrap.client.ui.constants.IconType;
import com.github.gwtbootstrap.client.ui.Dropdown;
import com.github.gwtbootstrap.client.ui.DropdownButton;
import com.github.gwtbootstrap.client.ui.Image;
import com.github.gwtbootstrap.client.ui.SplitDropdownButton;
import com.github.gwtbootstrap.client.ui.Navbar;
import com.github.gwtbootstrap.client.ui.NavLink;
import com.github.gwtbootstrap.client.ui.Nav;
import com.github.gwtbootstrap.client.ui.Brand;
import com.github.gwtbootstrap.client.ui.TooltipCellDecorator;
import com.github.gwtbootstrap.client.ui.TabPanel;
import com.github.gwtbootstrap.client.ui.TabPane;
import com.github.gwtbootstrap.client.ui.Tab;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Where2eattoday implements EntryPoint {
	/**
	 * The message displayed to the user when the server cannot be reached or
	 * returns an error.
	 */
	
	/**
	 * Create a remote service proxy to talk to the server-side Greeting service.
	 */
	private final GreetingServiceAsync greetingService = GWT
			.create(GreetingService.class);
	
	private final GroupServiceAsync groupService = GWT
			.create(GroupService.class);
	
	// Use the implementation of Auth intended to be used in the GWT client app.
	  private static final Auth AUTH = Auth.get();
	  private static final String GOOGLE_AUTH_URL = "https://accounts.google.com/o/oauth2/auth";
	  // The auth scope being requested. This scope will allow the application to
	  // identify who the authenticated user is.
	  private static final String PLUS_ME_SCOPE = "https://www.googleapis.com/auth/plus.me";
	  // This app's personal client ID assigned by the Google APIs Console
	  // (http://code.google.com/apis/console).
	  private static final String GOOGLE_CLIENT_ID = "319444413743-k2s3q50ts70kagn82ac2skjoog2poea6.apps.googleusercontent.com";
	  
	  
	private ArrayList<GroupTabPane> listOfGroupPanes = new ArrayList<GroupTabPane>();
	
	private Image profilePicture = new Image();
	private final Anchor signInLink = new Anchor("");
	private final NavLink signOutNavLink = new NavLink(""); 
	private final NavLink manageGroupsNavLink = new NavLink("");  
	private final NavLink manageRestaurantsNavLink = new NavLink("");   
	private final Nav displayUserOptionsNav = new Nav(); 
	private final NavLink displayRestaurantsNavLink = new NavLink(""); 
	private final TabPanel groupsTabPanel = new TabPanel(); 
	
	TabPane newGroupTabPane = new TabPane();
	//mock data
	final String myGroupName = "myGroup";
	Group myGroup;
	int selectedGroup =0;
	final Button addGroupRestaurant = new Button();
	final ListBox newRestaurant = new ListBox();
	
	final String[] listOfSuggestions = {"Armouriers", "Pizzas Carres"};
	final String[] listOfDishes = {"Roti de porc", "Pizza blanche"};
	
	final Brand applicationName = new Brand("Welcome to W2E");
	
	final Navbar navbar = new Navbar();
	//Group
	final TextBox groupName = new TextBox();
	final Button addGroup = new Button("Add");
	
	final Label todayChoiceRestaurantName = new Label();
	final FocusPanel displayChoice = new FocusPanel();
	final FocusPanel displayRestaurants = new FocusPanel();
	final Button displayGroups = new Button("Manage My groups");
	
	final Dropdown displayUserOptions= new Dropdown();
	final FocusPanel displaySignoutWrapper = new FocusPanel();
	final FlowPanel displaySignout = new FlowPanel();
	final Label userEmail = new Label("userEmail");
	final Button defineTodayChoice = new Button();
	MultiWordSuggestOracle autoCompleteRestaurants = new MultiWordSuggestOracle();

	
	
	
	LoginInfo loginInfo = null;
	
	final CellTable<Group> cellTableGroups = new CellTable<Group>();
	
	
	Logger logger = Logger.getLogger("loggerUI");
   
	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {

		final AuthRequest req = new AuthRequest(GOOGLE_AUTH_URL, GOOGLE_CLIENT_ID)
		.withScopes(PLUS_ME_SCOPE);

		// Calling login() will display a popup to the user the first time it is
		// called. Once the user has granted access to the application,
		// subsequent calls to login() will not display the popup, and will
		// immediately result in the callback being given the token to use.
		AUTH.login(req, new Callback<String, Throwable>() {
			@Override
			public void onSuccess(String token) {
				loginUser(token);
			}
			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Error:\n" + caught.getMessage());
			}
		});

	}

	private void loginUser(String token){
		final String tokenToPass = token;
		greetingService.login(tokenToPass, GWT.getHostPageBaseURL(), new AsyncCallback<LoginInfo>() {
			@Override	
			public void onFailure(final Throwable caught) {
				logger.log(Level.INFO, "FAILED: User not logged");
			}

			@Override
			public void onSuccess(final LoginInfo result) {
				if (result.getName() != null && !result.getName().isEmpty()) {
					logger.log(Level.INFO, "User logged ".concat(result.getEmailAddress()));
					loginInfo = result;
					if(result.getGroups().size()!=0){
						myGroup = result.getGroups().get(0);
						selectedGroup =0;	
					}
						loadUi();
				} else {
					logger.log(Level.INFO, "User not logged");
					loginInfo = result;
					loadLogin();					
				}
				
				
			}
		});
		
	}
	
	
	
	
	private void loadLogin() {
		signInLink.setHref(loginInfo.getLoginUrl());
		signInLink.setText("Please, sign in with your Google Account");
		signInLink.setTitle("Sign in");
		signInLink.setStyleName("signOutLink");
		RootPanel.get("loginContainer").add(signInLink);
	}
	
	private void loadLogout() {
		logger.log(Level.INFO,">>Starting loading log out panel");
		
		
		displayUserOptions.setText(loginInfo.getEmailAddress());
		signOutNavLink.setHref(loginInfo.getLogoutUrl());
		signOutNavLink.setText("Sign out");
		

		
		profilePicture.setUrl(loginInfo.getPictureUrl()); 
		profilePicture.setWidth("40px");
		manageRestaurantsNavLink.setText("Restaurants");
		manageRestaurantsNavLink.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				displayRestaurantsView();
			}
			});
		manageGroupsNavLink.setText("My Profile");
		manageGroupsNavLink.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				//displayGroupsView();
			}
			});
		
		displayUserOptionsNav.add(displayUserOptions);
		displayUserOptionsNav.insert(profilePicture,0);
		displayUserOptionsNav.setAlignment(Alignment.RIGHT);
		displayUserOptions.add(manageGroupsNavLink);
		displayUserOptions.add(signOutNavLink);
		displayUserOptions.add(manageRestaurantsNavLink);
		displayGroups.setType(ButtonType.PRIMARY);
		navbar.add(displayUserOptionsNav);
		navbar.add(displayUserOptionsNav);
		RootPanel.get("TopMenu").add(navbar);
		RootPanel.get("loginContainer").setVisible(false);
	}
	
	
	private void getListOfSuggestions() {
		displayListOfSuggestions();
		
	}
	private void displayListOfSuggestions() {
		for (int i=0; i<listOfSuggestions.length; i++){
			FlowPanel contentRestaurant = new FlowPanel();
			contentRestaurant.setStyleName("myRestaurantItem");
			Label restaurantName = new Label(); 
			Label restaurantDish = new Label(); 
			restaurantName.setStyleName("restaurantNameInList");
			restaurantDish.setStyleName("restaurantCount");
			restaurantName.setText(listOfSuggestions[i]);
			restaurantDish.setText(listOfDishes[i]);
			contentRestaurant.add(restaurantName);
			contentRestaurant.add(restaurantDish);
			RootPanel.get("listOfSuggestions").add(contentRestaurant);
		}
		
	}

	private void loadUi() {
		logger.log(Level.INFO, ">>Starting loadUi");
		RootPanel.get("Manager").setVisible(false);
		loadTopMenu();
			
		buildGroupsTab();
		RestaurantList restaurantList = new RestaurantList();
		RootPanel.get("Manager").add(restaurantList);
		RootPanel.get("GroupManager").setVisible(false);
	}

	private void buildGroupsTab() {
		for(int i=0; i<loginInfo.getGroups().size();i++){
			logger.log(Level.INFO, "new GroupTabPane".concat(loginInfo.getGroups().get(i).getName()));
			GroupTabPane groupTabPane = new GroupTabPane(loginInfo.getGroups().get(i),loginInfo.getGroups().get(i).getName(),groupsTabPanel, loginInfo);
			
			
			groupsTabPanel.add(groupTabPane);
			listOfGroupPanes.add(groupTabPane);
		}
		newGroupTabPane.setHeading("Add new Group");
		groupsTabPanel.add(newGroupTabPane);
		groupsTabPanel.selectTab(0);
		groupsTabPanel.setPullRight(true);
		groupsTabPanel.setTabPosition("Left");
		newGroupTabPane.add(groupName);
		newGroupTabPane.add(addGroup);
		
		addGroup.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) { 
				AsyncCallback callback = new AsyncCallback<Group>() {
					public void onFailure(Throwable error) {
						 //
					}
		 
					public void onSuccess(Group group) {
						GroupTabPane groupTabPane = new GroupTabPane(group,group.getName(),groupsTabPanel, loginInfo);
						groupTabPane.setCreateTabLink(false);
						groupsTabPanel.add(groupTabPane);
						listOfGroupPanes.add(groupTabPane);								
						groupsTabPanel.selectTab(listOfGroupPanes.indexOf(groupTabPane)+1);
						
					}
				};
						
				groupService.createGroup(groupName.getText(),loginInfo, callback);
			}
		});
		
		
		RootPanel.get("Choice").add(groupsTabPanel);
	}

	
	private void loadTopMenu() {
		displayRestaurantsNavLink.setText("Restaurants");

		displayRestaurantsNavLink.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				displayRestaurantsView();
			}
			});
		
		navbar.add(displayChoice);
		applicationName.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				displayChoiceView();
			}
			});
		applicationName.setHref("#");
		navbar.add(applicationName);
		
		navbar.add(displayRestaurants);
		loadLogout();
	}


	
	


	private void displayChoiceView() {
		RootPanel.get("Choice").setVisible(true);
		RootPanel.get("Manager").setVisible(false);
		RootPanel.get("GroupManager").setVisible(false);
		displayRestaurants.removeStyleName("selected");
	}
	private void displayRestaurantsView() {
		RootPanel.get("Choice").setVisible(false);
		RootPanel.get("Manager").setVisible(true);
		RootPanel.get("GroupManager").setVisible(false);
		displayRestaurants.addStyleName("selected");
		displayGroups.removeStyleName("selected");
	}
		
	
	

	

	
	
	
	
	
	
	
}
