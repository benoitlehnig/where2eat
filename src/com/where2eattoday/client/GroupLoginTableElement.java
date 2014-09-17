package com.where2eattoday.client;

import java.util.ArrayList;

import com.github.gwtbootstrap.client.ui.Dropdown;
import com.github.gwtbootstrap.client.ui.Heading;
import com.github.gwtbootstrap.client.ui.Icon;
import com.github.gwtbootstrap.client.ui.Image;
import com.github.gwtbootstrap.client.ui.NavLink;
import com.github.gwtbootstrap.client.ui.Paragraph;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.github.gwtbootstrap.client.ui.Button;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Widget;
import com.where2eattoday.shared.Group;
import com.where2eattoday.shared.LoginInfo;

public class GroupLoginTableElement extends Composite implements HasText {
	private final GroupServiceAsync groupService = GWT.create(GroupService.class);
	
	private static GroupLoginTableElementUiBinder uiBinder = GWT
			.create(GroupLoginTableElementUiBinder.class);

	interface GroupLoginTableElementUiBinder extends
			UiBinder<Widget, GroupLoginTableElement> {
	}

	Group tabGroup;
	LoginInfo loginInfo;
	GroupLoginTable groupLoginTable;
	public GroupLoginTableElement() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@UiField
	Image picture;
	
	@UiField
	Dropdown login;

	@UiField
	NavLink remove;
	
	public GroupLoginTableElement(LoginInfo loginInfo, Group group, GroupLoginTable groupLoginTable) {
		initWidget(uiBinder.createAndBindUi(this));
		this.loginInfo = loginInfo;
		this.tabGroup = group;
		this.groupLoginTable = groupLoginTable;
		
		login.setText(loginInfo.getEmailAddress());
		if(loginInfo.getPictureUrl()!=null){
			picture.setUrl(loginInfo.getPictureUrl());
		}
		
	}

	@UiHandler("remove")
	void onClick(ClickEvent e) {
		AsyncCallback callback = new AsyncCallback<ArrayList<LoginInfo>>() {
			public void onFailure(Throwable error) {
				
			}
 
			public void onSuccess(ArrayList<LoginInfo> loginInfos) {
				groupLoginTable.setLoginInfos(loginInfos);
			}
		};
		groupService.removeLoginInfoFromGroup(loginInfo,tabGroup, callback);
	}

	public void setText(String text) {
		remove.setText(text);
	}

	public String getText() {
		return remove.getText();
	}

}
