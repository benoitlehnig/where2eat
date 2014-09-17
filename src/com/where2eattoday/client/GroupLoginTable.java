package com.where2eattoday.client;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.CellTable;
import com.github.gwtbootstrap.client.ui.Column;
import com.github.gwtbootstrap.client.ui.Row;
import com.github.gwtbootstrap.client.ui.TextBox;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Widget;
import com.where2eattoday.shared.LoginInfo;
import com.where2eattoday.shared.Group;

public class GroupLoginTable extends Composite implements HasText {
	private final GroupServiceAsync groupService = GWT.create(GroupService.class);
	Group tabGroup;
	Logger logger = Logger.getLogger("loggerUI");
	LoginInfo loginInfo;
	GroupTabPane groupTabPane;
	
	private static loginTableUiBinder uiBinder = GWT
			.create(loginTableUiBinder.class);

	interface loginTableUiBinder extends UiBinder<Widget, GroupLoginTable> {
	}

	public GroupLoginTable() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@UiField
	Button addLogin;

	@UiField
	TextBox addLoginEmail;
	
	@UiField
	HTMLPanel listOfLogins;
	
	
	
	public GroupLoginTable(Group tabGroup, LoginInfo loginInfo,GroupTabPane groupTabPane) {
		initWidget(uiBinder.createAndBindUi(this));
		this.tabGroup = tabGroup;
		this.loginInfo = loginInfo;
		this.groupTabPane = groupTabPane;
	}

	@UiHandler("addLogin")
	void onClick(ClickEvent e) {
		AsyncCallback callback = new AsyncCallback<ArrayList<LoginInfo>>() {
			public void onFailure(Throwable error) {
				 //
			}
 
			public void onSuccess(ArrayList<LoginInfo> loginInfos) {
				logger.log(Level.INFO, "size of the list of loginInfo: ".concat(Integer.toString(loginInfos.size())));
				setLoginInfos(loginInfos);
				addLoginEmail.setText("");
			}

			
		};
				
		groupService.addLoginInfoToGroup(addLoginEmail.getText(), tabGroup, callback);
	}

	public void setText(String text) {
		addLogin.setText(text);
	}

	public String getText() {
		return addLogin.getText();
	}

	public void setLoginInfos(ArrayList<LoginInfo> loginInfos) {
		listOfLogins.clear();
		groupTabPane.getGroupContent().getCircle().clear();
		int deg = 360/loginInfos.size();
		for(int i=0;i<loginInfos.size();i++){
			if(!loginInfos.get(i).getEmailAddress().equalsIgnoreCase(loginInfo.getEmailAddress())){
				GroupLoginTableElement loginElement = new GroupLoginTableElement (loginInfos.get(i), tabGroup,this);
				listOfLogins.add(loginElement);
			}
			CircleUserElement circleUserElement = new CircleUserElement(loginInfos.get(i));
			String rotate = Integer.toString(i*deg);
			String style =  "rotate("
					.concat(rotate).
					concat("deg) translate(14em) rotate(-")
					.concat(rotate)
					.concat("deg)");
			logger.log(Level.INFO, "style: ".concat(style)); 
			circleUserElement.getElement().getStyle().setProperty("transform",style);
			groupTabPane.getGroupContent().getCircle().add(circleUserElement);			
		}
	}

}
