package to.networld.android.divedroid;

import java.util.ArrayList;
import java.util.HashMap;

import to.networld.android.divedroid.model.MediaHandler;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;

/**
 * Main activity of DiveDroid.
 * DiveDroid is a logbook that uses the scuba dive ontology specified on http://scubadive.networld.to
 * 
 * @author Alex Oberhauser
 *
 */
public class DiveDroid extends Activity {
    
    private static final int MENU_SETTINGS = 0x0010;
    private static final int MENU_ABOUT = 0x0020;
	
	private OnItemClickListener listClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> list, View view, int position, long id) {
			switch (position) {
				case 0:
					showDiveCollection();
					break;
				case 1:
					showDiverInformation();
					break;
			}
		}
	};
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);
        
		ListView list = (ListView) findViewById(R.id.MAIN);
		list.setOnItemClickListener(this.listClickListener);
		ArrayList<HashMap<String, String>> buttonList = new ArrayList<HashMap<String, String>>();
		
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("top", "Show Logbook");
		buttonList.add(map);

		map = new HashMap<String, String>();
		map.put("icon", R.drawable.scuba_diver + "");
		map.put("top", "Diver Infos");
		buttonList.add(map);
		
		SimpleAdapter adapterMainList = new SimpleAdapter(this, buttonList, 
				R.layout.main_list_entry, new String[]{ "icon", "top", "bottom" },
				new int[] { R.id.icon, R.id.topText, R.id.bottomText });
		list.setAdapter(adapterMainList);
    }
    
    private void showDiveCollection() {
		Intent mapIntent = new Intent(DiveDroid.this, DiveCollectionList.class);
		this.startActivity(mapIntent);
    }
    
    private void showDiverInformation() {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this.getBaseContext());                                                                           
        String diverFilename = settings.getString("Diver", "diver.rdf");
        String diverNodeID = settings.getString("NodeID", "profile");
    	MediaHandler mediaHandler = new MediaHandler();
    	String diver = mediaHandler.getBuddyPath() + "/" + diverFilename;
    	
    	Intent intent = new Intent(DiveDroid.this, DiverProfile.class);
		intent.putExtra("filename", diver);
		intent.putExtra("nodeid", diverNodeID);
		intent.putExtra("isbuddy", false);
    	this.startActivity(intent);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
            menu.add(0, MENU_SETTINGS, 20, "Settings").setIcon(
                            R.drawable.settings_icon);
            menu.add(0, MENU_ABOUT, 30, "About").setIcon(R.drawable.about_icon);
            return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
            switch (item.getItemId()) {
            case MENU_SETTINGS:
                    Intent intent = new Intent(DiveDroid.this, Settings.class);
                    this.startActivity(intent);
                    return true;
            case MENU_ABOUT:
                    this.aboutDialog();
                    return true;
            }
            return false;
    }
    
    public void aboutDialog() {
    }

}