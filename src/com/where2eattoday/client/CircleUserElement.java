package com.where2eattoday.client;

import com.github.gwtbootstrap.client.ui.Image;
import com.github.gwtbootstrap.client.ui.Tooltip;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Widget;
import com.where2eattoday.shared.LoginInfo;

public class CircleUserElement extends Composite {

	private static CircleUserElementUiBinder uiBinder = GWT
			.create(CircleUserElementUiBinder.class);

	interface CircleUserElementUiBinder extends
			UiBinder<Widget, CircleUserElement> {
	}

	public CircleUserElement() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@UiField
	Image picture;

	public CircleUserElement(LoginInfo loginInfo) {
		initWidget(uiBinder.createAndBindUi(this));
		if(loginInfo.getPictureUrl()!=null){
			picture.setUrl(loginInfo.getPictureUrl());
		}
		else{
			picture.setUrl("icons/photo.jpg");
		}
		Tooltip tooltip = new Tooltip();
		tooltip.setWidget(this);
		tooltip.setText(loginInfo.getEmailAddress());
	    tooltip.reconfigure();
	}


}
