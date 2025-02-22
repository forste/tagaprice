package org.tagaprice.client.gwt.client.features.accountmanagement.login.devView;

import org.tagaprice.client.gwt.client.features.accountmanagement.login.ILoginView;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class LoginViewImpl extends Composite implements ILoginView {
	interface LoginViewImplUiBinder extends UiBinder<Widget, LoginViewImpl>{}

	private static LoginViewImplUiBinder uiBinder = GWT.create(LoginViewImplUiBinder.class);

	private Presenter _presenter;

	@UiField
	TextBox email;

	@UiField
	PasswordTextBox password;

	@UiField
	Button loginButton;

	public LoginViewImpl() {
		initWidget(LoginViewImpl.uiBinder.createAndBindUi(this));
	}

	@Override
	public String getEmail(){
		return email.getText();
	}

	@Override
	public String getPassword(){
		return password.getText();
	}

	@UiHandler("loginButton")
	public void onLoginButtonCicked(ClickEvent event){
		_presenter.onLoginEvent();
	}

	@Override
	public void setPresenter(Presenter presenter) {
		_presenter=presenter;
	}

}
