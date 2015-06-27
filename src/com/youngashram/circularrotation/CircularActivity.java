package com.youngashram.circularrotation;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

/**
 * Circular view illusion. We have draw 8 view in clock direction.
 * On move of progress bar, we map event with available valid index view.
 * View remain on same position, just data part get reset among the views.
 *  
 * @author shubham
 *
 */
public class CircularActivity extends Activity {

	private TextView txtCount = null;
	private SeekBar seekBar = null;

	// complete set of child view available in clock direction
	private int[] viewArray = {R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4, R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8};
	
	// Valid index view, which will be available to rotate in clock direction.
	private int[] validViewArray = {R.id.btn1, R.id.btn2, R.id.btn8, R.id.btn5, R.id.btn3, -1, -1, -1};
	
	// Representation data part to show illusion.
	private int[] inputDataColor = {R.color.holo_green_light, R.color.holo_blue_light, R.color.holo_orange_light, R.color.holo_red_light,
			R.color.lemon_chiffon, R.color.articlecolor, R.color.cornsilk, R.color.cachecolor};

	private int lastPosition = 0; // To track direction while doing progress. You can use it from compass listener.
	private boolean isThisClockDirection = false; // Direction mapping flag to track clock direction.

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_circular);

		init();
		setListener();
	}

	/**
	 * Initialize view member.
	 */
	private void init() {
		seekBar = (SeekBar) findViewById(R.id.seekBar);
		txtCount = (TextView) findViewById(R.id.txtCount);

		for (int i = 0; i < validViewArray.length; i++) {
			
			if(validViewArray[i] == -1) {
				continue;
			}

			findViewById(validViewArray[i]).setBackgroundColor(getResources().getColor(inputDataColor[i]));
		}
	}

	/**
	 * Set event listener.
	 */
	private void setListener() {

		seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {

				// Clock direction mapping.
				if(lastPosition < progress) { 
					isThisClockDirection = true;
				} else {
					isThisClockDirection = false;
				}

				// Just to get event with every five move.
				if(progress % 5 == 0) {

					builder.setLength(0);

					builder.append(progress);
					builder.append(", " + isThisClockDirection+"\n");

					move(-1);
				}

				lastPosition = progress;
			}
		});
	}

	private Button view;
	private int inputIndex = 0;

	private StringBuilder builder = new StringBuilder();

	private int viewTag;

	/**
	 * Following allow to show clock illusion among views. 
	 * @param index fake index. Not using.
	 */
	private void move(int index) {

		// Reset all views.
		for (int i = 0; i < validViewArray.length; i++) {

			if(validViewArray[i] == -1)
				continue;
			
			findViewById(validViewArray[i]).setBackgroundColor(getResources().getColor(R.color.gray_light));
		}

		
		for (int i = 0; i < validViewArray.length; i++) {

			if(validViewArray[i] == -1)
				continue;

			view = (Button) findViewById(validViewArray[i]);

			// Get tag, set from xml view. It will be use to get next view to set data.
			viewTag = Integer.parseInt(findViewById(validViewArray[i]).getTag()+"");
			int tmpViewTag;

			if(isThisClockDirection) {
				tmpViewTag = viewTag <= (viewArray.length - 1) ? (viewTag + 1) : 1;
			} else {
				tmpViewTag = viewTag == 1 ? (viewArray.length) : viewTag - 1;
			}

			// Get next view to set data.
			view = (Button) findViewById(viewArray[tmpViewTag - 1]);
			view.setBackgroundColor(getResources().getColor(inputDataColor[i]));

			builder.append("[" + i +", " + viewTag + ", " + tmpViewTag + "]");
			txtCount.setText(builder.toString());

			validViewArray[i] = viewArray[tmpViewTag - 1]; // and finally assign valid view index i.e. this view now have data.
		}
	}

	
//	private int[] inputData = {1, 2, 3, 4};
//	private int[] tmpInputArray;
//	
//	/**
//	 * Move data, Look like view remain on same position. 
//	 */
//	private void move() {
//
//		tmpInputArray = new int[inputData.length];
//
//		for (int i = 0; i < validViewArray.length; i++) {
//			view = (Button) findViewById(validViewArray[i]);
//
//			if(isThisClockDirection)
//				inputIndex = i == 0 ? validViewArray.length - 1 : (i - 1);
//			else
//				inputIndex = i < (validViewArray.length - 1) ? (i + 1) : 0;
//
//				view.setText(inputData[inputIndex] + "");
//
//				builder.append(", [" + i +", " + inputIndex + ", " + inputData[inputIndex] + "]");
//				txtCount.setText(builder.toString());
//
//				tmpInputArray[i] = inputData[inputIndex];
//		}
//
//		inputData = tmpInputArray;
//	}
}
