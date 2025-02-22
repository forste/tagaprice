package org.tagaprice.client.gwt.client.features.shopmanagement.createShop;

import org.tagaprice.client.gwt.client.ClientFactory;
import org.tagaprice.client.gwt.shared.entities.RevisionId;
import org.tagaprice.client.gwt.shared.entities.receiptManagement.IReceiptEntry;
import org.tagaprice.client.gwt.shared.entities.shopmanagement.*;
import org.tagaprice.client.gwt.shared.logging.LoggerFactory;
import org.tagaprice.client.gwt.shared.logging.MyLogger;
import org.tagaprice.client.gwt.shared.rpc.shopmanagement.ShopDTO;
import org.tagaprice.core.api.UserNotLoggedInException;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.NotificationMole;
import com.google.gwt.user.client.ui.PopupPanel;

public class CreateShopActivity implements ICreateShopView.Presenter, Activity {
	private static final MyLogger _logger = LoggerFactory.getLogger(CreateShopActivity.class);

	private IShop _shop;
	private ICreateShopView<IReceiptEntry> _createShopView;
	private CreateShopPlace _place;
	private ClientFactory _clientFactory;

	public CreateShopActivity(CreateShopPlace place, ClientFactory clientFactory) {
		CreateShopActivity._logger.log("CreateProductActivity created");
		_place = place;
		_clientFactory = clientFactory;
	}


	@Override
	public String mayStop() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCancel() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub

	}

	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		CreateShopActivity._logger.log("activity startet");

		_createShopView = _clientFactory.getCreateShopView();
		_createShopView.setPresenter(this);

		if (_place.getRevisionId() != null && _place.getRevisionId().getId() != 0L) {
			// Existing product... trying to load
			_clientFactory.getShopService().getShop(new RevisionId(_place.getRevisionId().getId()),
					new AsyncCallback<ShopDTO>() {

				@Override
				public void onSuccess(ShopDTO result) {
					CreateShopActivity._logger.log("got shop: " + result);
					setShop(result.getShop());
					_createShopView.setReceiptEntries(result.getReceiptEntries());

				}

				@Override
				public void onFailure(Throwable caught) {

					CreateShopActivity._logger.log(caught.getMessage());
				}
			});

		} else {
			// new product... reseting view
		}

		panel.setWidget(_createShopView);


	}

	@Override
	public void goTo(Place place) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSaveEvent() {
		_clientFactory.getShopService().save(getShop(), new AsyncCallback<IShop>() {

			@Override
			public void onSuccess(IShop result) {
				CreateShopActivity._logger.log("got updated shop: " + result);
				setShop(result);
			}

			@Override
			public void onFailure(Throwable caught) {

				try{
					throw caught;
				}catch (UserNotLoggedInException e){
					//TODO This stuff must be implementet at an global place
					final PopupPanel pop = new PopupPanel();
					final NotificationMole mole = new NotificationMole();
					pop.show();
					pop.setPopupPosition(Window.getClientWidth() / 2, Window.getClientHeight() / 2);
					pop.add(mole);
					mole.setMessage("user not logged in "+e.getMessage());
					mole.setAnimationDuration(500);
					mole.show();

					Timer t = new Timer() {
						@Override
						public void run() {
							mole.hide();
							Timer t2 = new Timer() {

								@Override
								public void run() {
									pop.hide();
								}
							};
							t2.schedule(500);
						}
					};

					t.schedule(2000);
					CreateShopActivity._logger.log(e.getMessage());
				}catch (Throwable e){
					// last resort -- a very unexpected exception
					CreateShopActivity._logger.log(e.getMessage());
					e.printStackTrace();
				}

				CreateShopActivity._logger.log("got exception");
				CreateShopActivity._logger.log(caught.getMessage());
			}
		});
	}

	private void setShop(IShop shop) {
		_shop = shop;
		_createShopView.setRevisionId(shop.getRevisionId());
		_createShopView.setShopTitle(shop.getTitle());
		_createShopView.setCountry(shop.getCountry());
		_createShopView.setCity(shop.getCity());
		_createShopView.setZip(shop.getZip());
		_createShopView.setStreet(shop.getStreet());
		_createShopView.setLatLng(LatLng.newInstance(shop.getLat(), shop.getLng()));
	}

	private IShop getShop() {
		IShop shop;
		if (_shop != null) {
			shop = _shop;
		} else {
			shop = new Shop();
		}
		shop.setTitle(_createShopView.getShopTitle());
		shop.setCountry(_createShopView.getCountry());
		shop.setCity(_createShopView.getCity());
		shop.setZip(_createShopView.getZip());
		shop.setStreet(_createShopView.getStreet());
		shop.setLat((_createShopView.getLatLng().getLatitude()));
		shop.setLng((_createShopView.getLatLng().getLongitude()));
		return shop;
	}

}
