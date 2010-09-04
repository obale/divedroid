package to.networld.android.divedroid;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
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
    
	private OnItemClickListener listClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> list, View view, int position, long id) {
			switch (position) {
				case 0:
					showDiveCollection();
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

		SimpleAdapter adapterMainList = new SimpleAdapter(this, buttonList, 
				R.layout.main_list_entry, new String[]{ "icon", "top", "bottom" },
				new int[] { R.id.icon, R.id.topText, R.id.bottomText });
		list.setAdapter(adapterMainList);
    }
    
    private void showDiveCollection() {
		Intent mapIntent = new Intent(DiveDroid.this, DiveCollectionList.class);
		this.startActivity(mapIntent);
    }
}