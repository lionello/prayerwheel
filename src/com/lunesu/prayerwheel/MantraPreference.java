package com.lunesu.prayerwheel;

import android.content.Context;
import android.preference.ListPreference;
import android.util.AttributeSet;

public class MantraPreference extends ListPreference {

	public MantraPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onSetInitialValue(boolean restore, Object defaultValue) {
		super.onSetInitialValue(restore, defaultValue);
		setSummary(this.getEntry());
	}
	
	@Override
	public boolean callChangeListener(Object newValue) {
		// Find the title of the newly selected value
		int index = findIndexOfValue((String)newValue);
		if (index < 0)
			return false;
		setSummary(getEntries()[index]);
		return true;
	}
}
