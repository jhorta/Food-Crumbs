package com.hortashorchatas.foodcrumbs.test;

import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.widget.RadioButton;
import android.widget.TextView;

import com.hortashorchatas.foodcrumbs.Directions_Activity;
import com.hortashorchatas.foodcrumbs.R;

public class DirectionsActivityTest extends
ActivityInstrumentationTestCase2<Directions_Activity>{

	private Directions_Activity mActivity;
	private RadioButton time;
	private RadioButton distance;
	private TextView filterLabel;
	
	// Constructor
	public DirectionsActivityTest() {
		super(Directions_Activity.class);
	}
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();

		// Turn off touch mode in the device (for if we send commands to the device)
		setActivityInitialTouchMode(false);

		// Initialize all of the assets we are testing
		mActivity = getActivity();	
		time = (RadioButton) mActivity.findViewById(R.id.radio_button_filter_time);
		distance = (RadioButton) mActivity.findViewById(R.id.radio_button_filter_distance);
		filterLabel = (TextView) mActivity.findViewById(R.id.label_text_hours_or_miles);
	}


	/*
	 * Make sure all the elements are not null
	 */
	public void testPreconditions() throws Exception {
		// Make sure all of the assets we are testing are not null, otherwise tests will fail
		assertNotNull("mActivity is null", mActivity);
		assertNotNull("time is null", time);
		assertNotNull("distance is null", distance);
		assertNotNull("filterLabel is null", filterLabel);
	}
	
	
	/*
	 * Test clicking the time button. The filter label should be changed to "Hours", distance
	 * should be unchecked, and time should be checked.
	 */
	public void testTimeFilter() throws Exception
	{
		TouchUtils.clickView(this, time);
		assert(time.isChecked());
		assertFalse(distance.isChecked());
		assertEquals("Hours", filterLabel.getText());	
	}

	
	/*
	 * Test clicking the distance button. The filter label should be changed to "Miles", distance
	 * should be checked, and time should be unchecked.
	 */
	public void testDistanceFilter() throws Exception
	{
		TouchUtils.clickView(this, distance);
		assert(distance.isChecked());
		assertFalse(time.isChecked());
		assertEquals("Miles", filterLabel.getText());
	}

}
