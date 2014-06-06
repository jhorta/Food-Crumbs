package com.hortashorchatas.foodcrumbs.test;

import android.app.Instrumentation.ActivityMonitor;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.hortashorchatas.foodcrumbs.Favorites_View_Activity;
import com.hortashorchatas.foodcrumbs.Main_Menu_Activity;
import com.hortashorchatas.foodcrumbs.Map_View_Activity;
import com.hortashorchatas.foodcrumbs.R;
import com.hortashorchatas.foodcrumbs.Random_Generator_Activity;

public class MainMenuActivityTest extends
ActivityInstrumentationTestCase2<Main_Menu_Activity> {

	private Main_Menu_Activity mActivity;
	private ImageView profpic;
	private TextView name;
	private ImageButton fav;
	private ImageButton directions;
	private ImageButton random;

	// Constructor
	public MainMenuActivityTest() {
		super(Main_Menu_Activity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		// Turn off touch mode in the device (for if we send commands to the device)
		setActivityInitialTouchMode(false);

		// Initialize all of the assets we are testing
		mActivity = getActivity();
		profpic = (ImageView) mActivity.findViewById(com.hortashorchatas.foodcrumbs.R.id.profile_pic);
		name = (TextView) mActivity.findViewById(com.hortashorchatas.foodcrumbs.R.id.name_text);
		fav = (ImageButton) mActivity.findViewById(R.id.favorites_button);
		directions = (ImageButton) mActivity.findViewById(R.id.directions_button);
		random = (ImageButton) mActivity.findViewById(R.id.random_button);
	}


	/*
	 * Make sure all the elements are not null
	 */
	public void testPreconditions() throws Exception {
		// Make sure all of the assets we are testing are not null, otherwise tests will fail
		assertNotNull("mActivity is null", mActivity);
		assertNotNull("profpic is null", profpic);
		assertNotNull("name is null", name);
		assertNotNull("fav is null", fav);
		assertNotNull("directions is null", directions);
		assertNotNull("random is null", random);
	}


	/*
	 * Test the onCreateView - profile pic, favorites button, directions button, random button
	 */
	public void testOnCreateView() throws Exception {
		// Check the height and picture of the profile pic
		assertEquals(150, profpic.getHeight());
		assertNotNull(profpic.getDrawable());

		// Check the text
		assertEquals("Hello, Michael!", name.getText());

		// Check the size of the favorites button
		assertEquals(200, fav.getHeight());
		assertEquals(200, fav.getWidth());

		// Check the size of the directions button
		assertEquals(200, directions.getHeight());
		assertEquals(200, directions.getWidth());

		// Check the size of the random button
		assertEquals(200, random.getHeight());
		assertEquals(200, random.getWidth());
	}


	/*
	 * Test clicking on the favorites button - should start the Favorites View Activity
	 */
	public void testFavoritesView() throws Exception {
		// Set up an ActivityMonitor
		ActivityMonitor receiverActivityMonitor =
				getInstrumentation().addMonitor(Favorites_View_Activity.class.getName(),
						null, false);


		// Click the favorites button
		TouchUtils.clickView(this, fav);


		// Use the ActivityMonitor to make sure that the Favorites View Activity has started
		Favorites_View_Activity favActivity = (Favorites_View_Activity)
				receiverActivityMonitor.waitForActivityWithTimeout(1000);
		assertNotNull("Favorites View Activity is null", favActivity);
		assertEquals("Monitor for ReceiverActivity has not been called",
				1, receiverActivityMonitor.getHits());
		assertEquals("Activity is of wrong type",
				Favorites_View_Activity.class, favActivity.getClass());


		// Remove the ActivityMonitor
		getInstrumentation().removeMonitor(receiverActivityMonitor);

		// Close the Favorites View Activity
		favActivity.finish();
	}


	/*
	 * Test clicking on the directions button - should start the Map View Activity
	 * NOTE: ONLY WORKS ON AN ACTUAL DEVICE. IF YOU RUN ON AN EMULATOR YOU WILL GET A NULLPTR EXCEPTION
	 */
	public void testMapView() throws Exception {
		try {
		// Set up an ActivityMonitor
		ActivityMonitor receiverActivityMonitor =
				getInstrumentation().addMonitor(Map_View_Activity.class.getName(),
						null, false);


		// Click the directions button
		TouchUtils.clickView(this, directions);


		// Use the ActivityMonitor to make sure that the Map View Activity has started
		Map_View_Activity mapActivity = (Map_View_Activity)
				receiverActivityMonitor.waitForActivityWithTimeout(1000);
		assertNotNull("Map View Activity is null", mapActivity);
		assertEquals("Monitor for ReceiverActivity has not been called",
				1, receiverActivityMonitor.getHits());
		assertEquals("Activity is of wrong type",
				Map_View_Activity.class, mapActivity.getClass());


		// Remove the ActivityMonitor
		getInstrumentation().removeMonitor(receiverActivityMonitor);

		// Close the Map View Activity
		mapActivity.finish();
		}
		
		catch (Exception e) {
			e.printStackTrace();
		}
	}


	/*
	 * Test clicking on the random button - should start the Random Generator Activity
	 */
	public void testRandomView() throws Exception {
		// Set up an ActivityMonitor
		ActivityMonitor receiverActivityMonitor =
				getInstrumentation().addMonitor(Random_Generator_Activity.class.getName(),
						null, false);


		// Click the favorites button
		TouchUtils.clickView(this, random);


		// Use the ActivityMonitor to make sure that the Random View Activity has started
		Random_Generator_Activity randomActivity = (Random_Generator_Activity)
				receiverActivityMonitor.waitForActivityWithTimeout(1000);
		assertNotNull("Random Generator Activity is null", randomActivity);
		assertEquals("Monitor for ReceiverActivity has not been called",
				1, receiverActivityMonitor.getHits());
		assertEquals("Activity is of wrong type",
				Random_Generator_Activity.class, randomActivity.getClass());


		// Remove the ActivityMonitor
		getInstrumentation().removeMonitor(receiverActivityMonitor);

		// Close the Favorites View Activity
		randomActivity.finish();
	}


}
