package com.where2eattoday.client;

import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.ButtonCell;
import com.github.gwtbootstrap.client.ui.CellTable;
import com.github.gwtbootstrap.client.ui.TextBox;
import com.github.gwtbootstrap.client.ui.TooltipCellDecorator;
import com.github.gwtbootstrap.client.ui.constants.IconType;
import com.google.gwt.cell.client.DateCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.where2eattoday.shared.DishOfTheDay;
import com.where2eattoday.shared.Restaurant;
import com.github.gwtbootstrap.client.ui.Row;


public class RestaurantList extends FlowPanel{

	Logger logger = Logger.getLogger("loggerUI");
	
	Row mainRow = new Row();
	com.github.gwtbootstrap.client.ui.Column restaurantListColumn = new com.github.gwtbootstrap.client.ui.Column(8);
	com.github.gwtbootstrap.client.ui.Column dishOfTheDayListColumn = new com.github.gwtbootstrap.client.ui.Column(4);
	
	private final GreetingServiceAsync greetingService = GWT.create(GreetingService.class);
	private final DishOfTheDayServiceAsync dishOfTheDayService = GWT.create(DishOfTheDayService.class);
	Restaurant selectedRestaurant= null;
	final CellTable<Restaurant> cellTableRestaurants = new CellTable<Restaurant>(5, GWT.<CellTable.SelectableResources>create(CellTable.SelectableResources.class));
	final CellTable<DishOfTheDay> cellTableDishOfTheDay = new CellTable<DishOfTheDay>(5, GWT.<CellTable.SelectableResources>create(CellTable.SelectableResources.class));

	final Button addRestaurant = new Button("Add");
	final TextBox addRestaurantName = new TextBox();
	final Button addDish = new Button("Add");
	final TextBox addDishName = new TextBox();
	final DateBox datePicker = new DateBox();
		
	public RestaurantList() {
		mainRow.add(restaurantListColumn);
		mainRow.add(dishOfTheDayListColumn);
		buildRestaurantTable();
		buildDishesOfTheDayTable();
		restaurantListColumn.add(cellTableRestaurants);
		restaurantListColumn.add(addRestaurantName);
		restaurantListColumn.add(addRestaurant);
		dishOfTheDayListColumn.add(cellTableDishOfTheDay);
		dishOfTheDayListColumn.add(datePicker);
		dishOfTheDayListColumn.add(addDishName);
		dishOfTheDayListColumn.add(addDish);
		addRestaurant.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) { 
				AsyncCallback callback = new AsyncCallback<Restaurant>() {

					public void onFailure(Throwable error) {
						 //
					}
		 
					public void onSuccess(Restaurant returnedRestaurant) {
						getListOfRestaurants();
					}
				};
						
				greetingService.addRestaurant(addRestaurantName.getText(), callback);
			}
		});
		
		addDish.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) { 
				AsyncCallback callback = new AsyncCallback<ArrayList<DishOfTheDay>>() {

					public void onFailure(Throwable error) {
						 //
					}
		 
					public void onSuccess(ArrayList<DishOfTheDay> results) {
						getListOfRestaurants();
					}
				};
				logger.log(Level.INFO, "adding new restaurant:".concat(Long.toString(selectedRestaurant.getId())));		
				dishOfTheDayService.addDishOfTheDay(selectedRestaurant, datePicker.getValue(),addDishName.getText(), callback);
			}
		});
		this.getListOfRestaurants();
		this.add(mainRow);
	}
	
	private void buildRestaurantTable() {
		TextColumn<Restaurant> nameColumn = 
				new TextColumn<Restaurant>() {
			@Override
			public String getValue(Restaurant object) {
				return object.getName();
			}
		};
		TextColumn<Restaurant> addressColumn = 
				new TextColumn<Restaurant>() {
			@Override
			public String getValue(Restaurant object) {
				return object.getAddress();
			}
		};
		TextColumn<Restaurant> cuisineColumn = 
				new TextColumn<Restaurant>() {
			@Override
			public String getValue(Restaurant object) {
				return object.getCuisine();
			}
		};

		ButtonCell buttonCell = new ButtonCell();
		buttonCell.setIcon(IconType.REMOVE);
		final TooltipCellDecorator<String> decorator = new TooltipCellDecorator<String>(buttonCell);
		decorator.setText("delete row, if click");
		Column<Restaurant, String> buttonColumn = new Column<Restaurant, String>(decorator) {
			@Override
			public String getValue(Restaurant object) {
				return "";
			}
		};
		
		
		buttonColumn.setFieldUpdater(new FieldUpdater<Restaurant, String>() {
			@Override
			public void update(final int index, Restaurant c,String value) 
			{
				deleteRestaurant(c);
			}
		});

		cellTableRestaurants.addColumn(nameColumn, "Name");
		cellTableRestaurants.addColumn(cuisineColumn, "Cuisine");
		cellTableRestaurants.addColumn(addressColumn, "Address");
		cellTableRestaurants.addColumn(buttonColumn, "Action");
		cellTableRestaurants.setBordered(true);
		cellTableRestaurants.setCondensed(true) ;
		cellTableRestaurants.setStriped(true);
		cellTableRestaurants.setAutoHeaderRefreshDisabled(true);
		final SingleSelectionModel<Restaurant> selectionModel = new SingleSelectionModel<Restaurant>();
	    cellTableRestaurants.setSelectionModel(selectionModel);
	    selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
	    	
	    public void onSelectionChange(SelectionChangeEvent event) {
	    	Restaurant selected = selectionModel.getSelectedObject();
	    	logger.log(Level.INFO, "selectedRestaurant".concat(selected.getName()).concat(Long.toString(selected.getId())));
	            if (selected != null) {
	            	logger.log(Level.INFO, "selectedRestaurant".concat(selected.getName()));
	            	selectedRestaurant= selected;
	            	AsyncCallback callback = new AsyncCallback<ArrayList<DishOfTheDay>>() {
	        			public void onFailure(Throwable error) {
	        				
	        			}
	         
	        			public void onSuccess(ArrayList<DishOfTheDay> list) {
	        				logger.log(Level.INFO, "list of dishes of the day: ".concat(Integer.toString(list.size())));
	        				cellTableDishOfTheDay.setRowData(list);
	        				cellTableDishOfTheDay.redraw();
	        			}
	        		};
	        		dishOfTheDayService.getListOfDishesOfTheDay(selected,callback);
	            }
	          }
	        });
	      
	    restaurantListColumn.add(cellTableRestaurants);

	}
	
	private void deleteRestaurant(Restaurant restaurant) {
		AsyncCallback callback = new AsyncCallback<Void>() {
			public void onFailure(Throwable error) {
				getListOfRestaurants();
			}
 
			public void onSuccess(Void ignore) {
				getListOfRestaurants();
			}
		};
		greetingService.deleteRestaurant(restaurant,callback);

	}
	
	private void getListOfRestaurants(){
		greetingService.getListOfRestaurants(
				new AsyncCallback<ArrayList<Restaurant>>() {
					public void onFailure(Throwable caught) {
					}

					public void onSuccess(ArrayList<Restaurant> result) {
						cellTableRestaurants.setRowData(result);
						cellTableRestaurants.getSelectionModel().setSelected(result.get(0), true);
						
					}
				});
	}
	
	
	private void buildDishesOfTheDayTable(){
		
	      TextColumn<DishOfTheDay> nameColumn = 
	      new TextColumn<DishOfTheDay>() {
	         @Override
	         public String getValue(DishOfTheDay object) {
	            return object.getDishOfTheDay();
	         }
	      };
	      DateCell dateCell = new DateCell();
	      Column<DishOfTheDay, Date> dateColumn = new Column<DishOfTheDay, Date>(dateCell) {
	        @Override
	        public Date getValue(DishOfTheDay object) {
	          return object.getDate();
	        }
	      };
	      
	    
	      cellTableDishOfTheDay.addColumn(nameColumn, "Name");   
	      cellTableDishOfTheDay.addColumn(dateColumn, "Date");   
	      
	}
}
