package com.google.glassware;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.services.mirror.Mirror;
import com.google.api.services.mirror.model.Subscription;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HuntServlet extends HttpServlet {
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String userId = AuthUtil.getUserId(req);
		Credential credential = AuthUtil.newAuthorizationCodeFlow().loadCredential(userId);

		List<String> list = new ArrayList<String>();
		list.add("hunt");

		MirrorClient.insertSubscription(credential, WebUtil.buildUrl(req, "/notify"), userId, "timeline");
		//subscribeToNotifications(credential, WebUtil.buildUrl(req, "/notify"), userId, list);
	}


	/**
	 * Subscribe to notifications for the current user.
	 *
	 * @param service Authorized Mirror service.
	 * @param collection Collection to subscribe to (supported values are "timeline" and
	"locations").
	 * @param userToken Opaque token used by the Glassware to identify the user
	 *        the notification pings are sent for (recommended).
	 * @param verifyToken Opaque token used by the Glassware to verify that the
	 *        notification pings are sent by the API (optional).
	 * @param callbackUrl URL receiving notification pings (must be HTTPS).
	 * @param operation List of operations to subscribe to. Valid values are
	 *        "UPDATE", "INSERT" and "DELETE" or {@code null} to subscribe to all.
	 */
	public static void subscribeToNotifications(Mirror service, String collection, String userToken, String verifyToken, String callbackUrl, List<String> operation) {
		Subscription subscription = new Subscription();
		subscription.setCollection(collection).setUserToken(userToken).setVerifyToken(verifyToken).setCallbackUrl(callbackUrl).setOperation(operation);
		try {
			service.subscriptions().insert(subscription).execute();
		} catch (IOException e) {
			System.err.println("An error occurred: " + e);
		}
	}
}
