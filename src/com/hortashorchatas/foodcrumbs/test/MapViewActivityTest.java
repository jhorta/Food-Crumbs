package com.hortashorchatas.foodcrumbs.test;

import android.app.Instrumentation.ActivityMonitor;
import android.support.v7.widget.SearchView;
import android.test.ActivityInstrumentationTestCase2;
import android.view.KeyEvent;
import android.view.View;

import com.hortashorchatas.foodcrumbs.Directions_Activity;
import com.hortashorchatas.foodcrumbs.Map_View_Activity;
import com.hortashorchatas.foodcrumbs.R;

public class MapViewActivityTest extends
ActivityInstrumentationTestCase2<Map_View_Activity>{

	
	private Map_View_Activity mActivity;
	private SearchView sView;


	// Constructor
	public MapViewActivityTest() {
		super(Map_View_Activity.class);
	}
	
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();

		// Turn off touch mode in the device (for if we send commands to the device)
		setActivityInitialTouchMode(false);

		// Initialize all of the assets we are testing
		mActivity = getActivity();	
		sView = (SearchView) mActivity.findViewById(R.id.action_search);
	}


	/*
	 * Make sure all the elements are not null
	 */
	public void testPreconditions() throws Exception {
		// Make sure all of the assets we are testing are not null, otherwise tests will fail
		assertNotNull("mActivity is null", mActivity);
		assertNotNull("sView is null", sView);
	}
	

	
	
	/*
	 * Test clicking the search icon - should open a SearchView prompting the user to 
	 * enter a location
	 */
	public void testSearch() throws Exception {		
		// Click the search icon (use instrumentation because it's a menu item)
		getInstrumentation().invokeMenuActionSync(mActivity, R.id.action_search, 0);
		
		
		// SearchView should be visible with the hint text "Enter a Location"
		assertTrue(View.VISIBLE == sView.getVisibility());
		assertEquals("Enter a Location", sView.getQueryHint());
	}
	
	
	/*
	 * Test clicking the directions icon - should start the Directions Activity
	 */
	public void testDirections() throws Exception {
		// Create ActivityMonitor
		ActivityMonitor receiverActivityMonitor = getInstrumentation().addMonitor(Directions_Activity.class.getName(),
				null, false);
		
		// Click the directions icon
		getInstrumentation().sendKeyDownUpSync(KeyEvent.KEYCODE_MENU);
		getInstrumentation().invokeMenuActionSync(mActivity, R.id.action_directions, 0);
		
		// Make sure the Directions Activity is started
		Directions_Activity dirActivity = (Directions_Activity)
				receiverActivityMonitor.waitForActivityWithTimeout(1000);
		assertNotNull("Directions Activity is null", dirActivity);
		assertEquals("Monitor for ReceiverActivity has not been called",
				1, receiverActivityMonitor.getHits());
		assertEquals("Activity is of wrong type",
				Directions_Activity.class, dirActivity.getClass());


		// Remove the ActivityMonitor
		getInstrumentation().removeMonitor(receiverActivityMonitor);

		// Close the Directions Activity
		dirActivity.finish();
		
	}
	
	
}
