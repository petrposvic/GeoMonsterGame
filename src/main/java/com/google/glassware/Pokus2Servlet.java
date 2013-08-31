package com.google.glassware;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.services.mirror.Mirror;
import com.google.api.services.mirror.model.Location;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.google.api.services.mirror.model.*;
import com.google.glassware.model.Monster;
import com.google.glassware.model.MonsterDefinition;

import java.util.Random;
import java.util.logging.Logger;

/**
 * 1) Generate monster
 * 2) Send position to the server
 */
public class Pokus2Servlet extends HttpServlet {

	private static final Logger LOG = Logger.getLogger(Pokus2Servlet.class.getSimpleName());

	// Maximal distance between player and new monster
	private static final double MAX_DISTANCE = 5000;

	// TODO
	private static MonsterDefinition[] MONSTER_DEFS = {
				new MonsterDefinition("Poring", 12f, 10.1f, "https://geomonstergame.appspot.com/static/images/zviratko1.png"),
				new MonsterDefinition("Dirty Wolf", 50f, 50.1f, "https://geomonstergame.appspot.com/static/images/zviratko2.png")
	};

	// TODO
	public static Monster mockGenerateMonster(double actualLatitude, double actualLongitude) {

		// TODO Generate random point in MAX_DISTANCE distance
		// ...

		Monster monster = new Monster();
		monster.latitude = actualLatitude;
		monster.longitude = actualLongitude;
		monster.duration = new Date().getTime() + new Random().nextLong() * 1000 * 60 * 60 + 1000 * 60 * 30;
		monster.definition = MONSTER_DEFS[
				new Random().nextInt(MONSTER_DEFS.length)
		];

		return monster;
	}

	/**
	 * Generate new card.
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

		String userId = AuthUtil.getUserId(req);
		Credential credential = AuthUtil.newAuthorizationCodeFlow().loadCredential(userId);

		// TODO
		Monster monster = mockGenerateMonster(50.0, 14.0);

		Location location = new Location();
		location.setLatitude(monster.latitude);
		location.setLongitude(monster.longitude);

		List<MenuItem> menuItemList = new ArrayList<MenuItem>();
		menuItemList.add(new MenuItem().setAction("TOGGLE_PINNED"));
		menuItemList.add(new MenuItem().setAction("NAVIGATE"));

		List<MenuValue> menuValues = new ArrayList<MenuValue>();
		menuValues.add(new MenuValue().setIconUrl(WebUtil.buildUrl(req, "/static/images/drill.png")).setDisplayName("Hunt"));
		menuItemList.add(new MenuItem().setValues(menuValues).setId("timeline").setAction("CUSTOM"));

		// TODO View website with monster's profile

		TimelineItem timelineItem = new TimelineItem();
		timelineItem.setNotification(new NotificationConfig().setLevel("DEFAULT"));
		timelineItem.setLocation(location);
		timelineItem.setMenuItems(menuItemList);
		timelineItem.setHtml(
				"<article>" +
				"<figure><img src=\"" + monster.definition.image + "\"></figure>" +
				"<section>" +
						"<table class=\"text-small align-justify\"><tbody>" +
								"<tr><td>Name</td><td>" + monster.definition.name + "</td></tr>" +
								"<tr><td>Difficulty</td><td>" + monster.definition.difficulty + "</td></tr>" +
								"<tr><td>Duration</td><td>" + new Date(monster.duration).toString() + "</td></tr>" +
						"</tbody></table></section>" +
				"</article>"
		);

		// TODO Save monster in database
		// ...

		MirrorClient.insertTimelineItem(credential, timelineItem);

		// TODO Return response
		PrintWriter out = res.getWriter();
		out.println("New card has been created: " + monster.toString());
		out.close();
	}

	/**
	 * Print information about the latest known location for the current user.
	 *
	 * @param service Authorized Mirror service.
	 */
	public static void checkRadius(Mirror service) {


		try {
			Location location = service.locations().get("latest").execute();

			System.out.println("Location recorded on: " + location.getTimestamp());
			System.out.println("  > Lat: " + location.getLatitude());
			System.out.println("  > Long: " + location.getLongitude());
			System.out.println("  > Accuracy: " + location.getAccuracy() + " meters");
		} catch (IOException e) {
			System.err.println("An error occurred: " + e);
		}
	}




















}
