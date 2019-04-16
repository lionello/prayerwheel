package com.lunesu.prayerwheel;

import java.util.UUID;

import org.apache.http.util.EncodingUtils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;

/**
 * Wrapper activity demonstrating the use of {@link GLSurfaceView}, a view
 * that uses OpenGL drawing into a dedicated surface.
 *
 * Shows:
 * + How to redraw in response to user input.
 */
public class PrayerWheelActivity extends Activity {

    private static final String PREFS_SILENTMODE = "silentMode";
    private static final String PREFS_PRAYERS = "prayers";
    private static final String PREFS_MANTRA = "mantra";
    private static final String PREFS_UUID = "uuid";
    private static final String PREFS_COLOR = "color";
    private static final String PREFS_HASH = "hash";

    private static final boolean DEF_SILENTMODE = false;
    private static final int DEF_COLOR = -1;
    private static final String DEF_MANTRA = "tibetan";
    private static final int DEF_PRAYERS = 0;
    
	private static final int MENU_EDIT = 1;
	private static final int MENU_INFO = 2;
	//private static final int MENU_SILENT = 3;
	//private static final int MENU_HELP = 4;
	//private static final int MENU_MANTRA = 5;
	
    private TouchSurfaceView mGLSurfaceView;
    private GestureDetector mGestureDetector;

    private String uuid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Listen for long-press to show options menu for devices without HW menu button
        mGestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public void onLongPress(MotionEvent e) {
                openOptionsMenu();
            }
        });
        mGestureDetector.setIsLongpressEnabled(true);

        // Create our Preview view and set it as the content of our Activity
        mGLSurfaceView = new TouchSurfaceView(this);
        mGLSurfaceView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return mGestureDetector.onTouchEvent(motionEvent);
            }
        });

        applyPreferences();
        
        setContentView(mGLSurfaceView);
        mGLSurfaceView.requestFocus();
        mGLSurfaceView.setFocusableInTouchMode(true);

        // Retrieve prayers; show help on first launch
        SharedPreferences settings = getSharedPreferences();
        uuid = settings.getString(PREFS_UUID, UUID.randomUUID().toString());
        int prayers = settings.getInt(PREFS_PRAYERS, DEF_PRAYERS);
        String hash = settings.getString(PREFS_HASH, "");

        // Ensure the UUID is valid 
        UUID.fromString(uuid);
        
        // Check whether the settings got tampered with
        if (hash.length() == 0 || hash.equals(ha(prayers)))
        	mGLSurfaceView.setPrayers(prayers);
   	
        // Show help on first launch
        if (prayers == 0) {
            onMenuHelpClicked(this);
        }


    }

	/*private void updateSilentIcon(MenuItem item) {
		boolean silent = mGLSurfaceView.getSilent();
		item.setTitle(getString(silent ? R.string.menu_silent_off : R.string.menu_silent));
    	item.setIcon(silent ? android.R.drawable.ic_lock_silent_mode_off : android.R.drawable.ic_lock_silent_mode);
	}
	
	private void onMuteUnmuteClicked() {
		mGLSurfaceView.setSilent(!mGLSurfaceView.getSilent());
	}*/
	
	
	/* Creates the menu items */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    menu.add(0, MENU_INFO, 0, getString(R.string.menu_info))
			.setIcon(android.R.drawable.ic_menu_info_details);
	    /*menu.add(0, MENU_HELP, 0, getString(R.string.menu_help))
			.setIcon(android.R.drawable.ic_menu_help);*/
	    menu.add(0, MENU_EDIT, 0, getString(R.string.menu_edit))
			.setIcon(android.R.drawable.ic_menu_edit);
    	/*MenuItem mi = menu.add(0, MENU_SILENT, 0, getString(R.string.menu_silent));
    	updateSilentIcon(mi);*/
	    return true;
	}

	final String UploadUrl = "http://prayerwheel.net/up.php";

	private void onStatsClicked() {
		String title = getResources().getString(R.string.info_title);
		title = title.replace("%s", ""+mGLSurfaceView.getPrayers());
		Dialog dlg = new Dialog(this);
		dlg.setTitle(title);      
		//TextView tv = new TextView(this);
		//tv.setPadding(5, 0, 5, 5);
	    //tv.setText(R.string.info);
		//dlg.setContentView(tv);
		dlg.setContentView(R.layout.main);
		Button button = (Button)dlg.findViewById(R.id.Button01);
		button.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				int prayers = mGLSurfaceView.getPrayers();
				// Create a hash (to prevent prayer fraud)
				String hash = ha(prayers);
				// Build the GET URL
				Uri uri = Uri.parse(UploadUrl + 
						"?uuid=" + uuid + 
						"&count="+ prayers + 
						"&hash=" + hash); 
				Intent browserIntent = new Intent("android.intent.action.VIEW", uri);
				startActivity(browserIntent);
			}
		});
		dlg.show();
	}
	
	// Calculate hash
	private String ha(int prayers) {
		// Create a hash (to prevent prayer fraud)
		byte[] blah = EncodingUtils.getAsciiBytes(uuid + "saltyballs" + prayers);
		// Create UUID from string, using MD5
		UUID hash = UUID.nameUUIDFromBytes(blah);
		return hash.toString();
	}
	
	public static void onMenuHelpClicked(Context context) {
		Dialog dlg = new Dialog(context);
		dlg.setTitle(R.string.help_title);
		WebView wv = new WebView(context);
		wv.loadUrl(context.getString(R.string.help_url));
		wv.setBackgroundColor(0);
		ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		dlg.setContentView(wv, lp);
		//AlertDialog.Builder dlgb = new AlertDialog.Builder(this);
		//AlertDialog dlg = dlgb.create();
		//dlg.setView(wv);	
	    dlg.show();
	}
	
    private static final int REQUEST_CODE_PREFERENCES = 1;
	
	private void onEditClicked() {
		
        // When the button is clicked, launch an activity through this intent
        Intent launchPreferencesIntent = new Intent().setClass(this, CustomizeActivity.class);
        
        // Make it a subactivity so we know when it returns
        startActivityForResult(launchPreferencesIntent, REQUEST_CODE_PREFERENCES);
	/*
		ColorPickerDialog.OnColorChangedListener click = new ColorPickerDialog.OnColorChangedListener() {
			@Override
			public void colorChanged(int c) {
				mGLSurfaceView.setColor(c);
			}
		};
		
		ColorPickerDialog cpd = new ColorPickerDialog(this, click, 0xffffffff);
		cpd.show();*/
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    super.onActivityResult(requestCode, resultCode, data);
	    
	    // The preferences returned if the request code is what we had given
	    // earlier in startSubActivity
	    if (requestCode == REQUEST_CODE_PREFERENCES) {
	        // Apply the new preferences
	    	applyPreferences();
	    }
	}
	
	/* Handles item selections */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    case MENU_EDIT:
	    	onEditClicked();
	        return true;
	    case MENU_INFO:
	    	onStatsClicked();
	        return true;
	    /*case MENU_SILENT:
	    	onMuteUnmuteClicked();
	    	updateSilentIcon(item);
	        return true;
	    case MENU_HELP:
	    	onMenuHelpClicked();
	    	return true;*/
	    }
	    return false;
	}
    
    @Override
    protected void onResume() {
        // Ideally a game should implement onResume() and onPause()
        // to take appropriate action when the activity looses focus
        super.onResume();
        mGLSurfaceView.onResume();
    }

    @Override
    protected void onPause() {
        // Ideally a game should implement onResume() and onPause()
        // to take appropriate action when the activity looses focus
        super.onPause();
        mGLSurfaceView.onPause();
        
        savePreferences();
    }

    @Override 
    protected void onStop() {
    	super.onStop();
    	//mGLSurfaceView.onActivityStops();
    }

    private void applyPreferences() {
        // Restore preferences
        SharedPreferences settings = getSharedPreferences();
        boolean silent = settings.getBoolean(PREFS_SILENTMODE, DEF_SILENTMODE);
        int color = settings.getInt(PREFS_COLOR, DEF_COLOR);		// defaults to white
        String mantra = settings.getString(PREFS_MANTRA, DEF_MANTRA);

        // apply preferences
        mGLSurfaceView.setTexture(mantra);
        mGLSurfaceView.setSilent(silent);
        mGLSurfaceView.setColor(color);
    }

    private SharedPreferences getSharedPreferences() {
    	return getSharedPreferences(CustomizeActivity.PREFS_NAME, MODE_PRIVATE);
    }
    
    private void savePreferences() {
        // We need an Editor object to make preference changes.
        // All objects are from android.context.Context
        SharedPreferences settings = getSharedPreferences();
        SharedPreferences.Editor editor = settings.edit();
        int prayers = mGLSurfaceView.getPrayers(); 
        editor.putInt(PREFS_PRAYERS, prayers);
        editor.putString(PREFS_UUID, uuid);
        editor.putString(PREFS_HASH, ha(prayers));
        // Commit the edits!
        editor.commit();
    }   
}

