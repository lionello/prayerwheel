package com.lunesu.prayerwheel;

import android.content.Context;
import android.content.res.TypedArray;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.View;

public class ColorPreference extends Preference implements ColorPickerDialog.OnColorChangedListener {

	static final int DEFAULT_COLOR = -1;//white
	
	public ColorPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
		
        setWidgetLayoutResource(R.layout.preference_widget_colorpreference);        
	}

	private int mColor;// = DEFAULT_COLOR;
	
    @Override
    protected void onBindView(View view) {
        super.onBindView(view);
        
        // Set our custom views inside the layout
        final ColorView myTextView = (ColorView) view.findViewById(R.id.mypreference_widget);
        if (myTextView != null) {
            myTextView.setColor(mColor);
        }
    }

	@Override
    protected void onClick() {
		ColorPickerDialog cpd = new ColorPickerDialog(getContext(), this, DEFAULT_COLOR);
		cpd.show();
	}

	@Override
	public void colorChanged(int color) {
		if (callChangeListener(color))
		{
			mColor = color;
	        // Save to persistent storage (this method will make sure this
	        // preference should be persistent, along with other useful checks)
			persistInt(color);
			
			notifyChanged();
		}
	}	
	
    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        // This preference type's value type is Integer, so we read the default
        // value from the attributes as an Integer.
        return a.getInteger(index, DEFAULT_COLOR);
    }
	
    @Override
    protected void onSetInitialValue(boolean restoreValue, Object defaultValue) {
        if (restoreValue) {
            // Restore state
        	mColor = getPersistedInt(mColor);
        } else {
            // Set state
            int value = (Integer) defaultValue;
            mColor = value;
            persistInt(value);
        }
    }
}
