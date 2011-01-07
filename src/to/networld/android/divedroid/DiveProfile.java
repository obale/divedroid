package to.networld.android.divedroid;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import org.dom4j.DocumentException;

import to.networld.android.divedroid.R;
import to.networld.android.divedroid.graphs.DiveProfileGraph;
import to.networld.android.divedroid.graphs.TemperaturePressureGraph;
import to.networld.android.divedroid.model.ImageHelper;
import to.networld.android.divedroid.model.rdf.Buddy;
import to.networld.android.divedroid.model.rdf.Dive;
import android.app.TabActivity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TabHost;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView.ScaleType;

/**
 * TODO: Visualize the detailed dive profile in a graph...
 * 
 * @author Alex Oberhauser
 *
 */
public class DiveProfile extends TabActivity {
	private Dive dive;
	
	private static final String ICON = "icon";
	private static final String TOP = "top";
	private static final String BOTTOM = "bottom";
	
	private static final String COMMENT = "Comment";
	private static final String EXPOSURE_TYPE = "Exposure Protection Type";
	private static final String LOCATION = "Location";
	private static final String COUNTRY = "Country";
	private static final String BUDDY = "Buddy";
	private static final String DIVE_DATE = "Dive Date";
	private static final String ENTRANCE_TYPE = "Entrance Type";
	private static final String TIME_DEEP = "Bottom Time and Max Deep";
	
	private static final String TEMPERATURE_PRESSURE = "Temperature/Pressure Graph";
	private static final String DIVE_PROFILE = "Dive Profile";
	
	private ArrayList<HashMap<String, String>> infoList;
	private ArrayList<HashMap<String, String>> graphList;
	
	private OnItemClickListener listClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> list, View view, int position, long id) {
			HashMap<String, String> entry = infoList.get(position);

			if ( entry.get(TOP).startsWith(LOCATION) ) {
					final Intent mapIntent = new Intent(
							android.content.Intent.ACTION_VIEW, 
							Uri.parse("geo:" + dive.getLatitude() + "," + dive.getLongitude() + "?z=14"));
					startActivity(mapIntent);
					return;
			} else if ( entry.get(TOP).startsWith(BUDDY) ) {
				Intent mapIntent = new Intent(DiveProfile.this, DiverProfile.class);
				System.err.println("[*****] " + entry.get("hiddenFilename") + "#" + entry.get("hiddenNodeID"));
				mapIntent.putExtra("filename", entry.get("hiddenFilename"));
				mapIntent.putExtra("nodeid", entry.get("hiddenNodeID"));
				startActivity(mapIntent);
			} else if ( entry.get(TOP).equals(COMMENT) ) {
				Toast.makeText(getApplicationContext(), dive.getComment(), Toast.LENGTH_LONG).show();
			}
		}
	};
	
	private OnItemClickListener graphClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> list, View view, int position, long id) {
			HashMap<String, String> entry = graphList.get(position);

			if ( entry.get(TOP).contentEquals(TEMPERATURE_PRESSURE) ) {
				TemperaturePressureGraph tempGraph = new TemperaturePressureGraph();
				Intent intent = tempGraph.execute(DiveProfile.this);
				startActivity(intent);
			} else if ( entry.get(TOP).contentEquals(DIVE_PROFILE) ) {
				DiveProfileGraph diveGraph = new DiveProfileGraph();
				Intent intent = diveGraph.execute(DiveProfile.this);
				startActivity(intent);
			}
		}
	};
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.diveprofile);
        
        TabHost tabHost = getTabHost();
        tabHost.setup();
        tabHost.addTab(tabHost.newTabSpec("summary").setIndicator("Summary",
        		getResources().getDrawable(R.drawable.logbook_tabicon)).setContent(R.id.profileList));
        tabHost.addTab(tabHost.newTabSpec("satpic").setIndicator("Satellite", 
        		getResources().getDrawable(R.drawable.sat_tabicon)).setContent(R.id.geoPicture));
        tabHost.addTab(tabHost.newTabSpec("stat").setIndicator("Stat", 
        		getResources().getDrawable(R.drawable.divelog_tabicon)).setContent(R.id.stat));

        tabHost.setCurrentTab(0);
        
        try {
	        String filename = getIntent().getStringExtra("filename");;
	        String nodeID = getIntent().getStringExtra("nodeid");;
			this.dive = new Dive(new File(filename), nodeID);
			
			ListView list = (ListView) findViewById(R.id.profileList);
			list.setOnItemClickListener(this.listClickListener);
			this.infoList = new ArrayList<HashMap<String, String>>();
			
			HashMap<String, String> map = new HashMap<String, String>();
			map.put(ICON, R.drawable.info_icon + "");
			map.put(TOP, this.dive.getName());
			map.put(BOTTOM, "Activity: " + this.dive.getActivity());
			this.infoList.add(map);
	
			map = new HashMap<String, String>();
			map.put(ICON, R.drawable.cal_icon + "");
			map.put(TOP, DIVE_DATE);
			map.put(BOTTOM, this.dive.getDate());
			this.infoList.add(map);
			
			map = new HashMap<String, String>();
			map.put(ICON, R.drawable.bottomtime_icon + "");
			map.put(TOP, TIME_DEEP);
			map.put(BOTTOM, this.dive.getBottomTime() + " Minutes on maximum " + this.dive.getMaxDeep() + " meters.");
			this.infoList.add(map);
			
			map = new HashMap<String, String>();
			map.put(ICON, R.drawable.world_icon + "");
			map.put(TOP, COUNTRY);
			map.put(BOTTOM, this.dive.getCountry());
			this.infoList.add(map);
			
			map = new HashMap<String, String>();
			map.put(ICON, R.drawable.location_icon + "");
			map.put(TOP, LOCATION + ": " + this.dive.getLocation());
			map.put(BOTTOM, "Divesite: " + this.dive.getDivesite());
			this.infoList.add(map);
			
			map = new HashMap<String, String>();
			map.put(ICON, R.drawable.exposuretype_icon + "");
			map.put(TOP, EXPOSURE_TYPE);
			map.put(BOTTOM, this.dive.getExposureProtection());
			this.infoList.add(map);
			
			map = new HashMap<String, String>();
			map.put(ICON, R.drawable.entrancetype_icon + "");
			map.put(TOP, ENTRANCE_TYPE);
			map.put(BOTTOM, this.dive.getEntranceType());
			this.infoList.add(map);
			
			String comment = this.dive.getComment();
			if ( comment != null ) {
				map = new HashMap<String, String>();
				map.put(ICON, R.drawable.comment_icon + "");
				map.put(TOP, COMMENT);
				if ( comment.length() <= 30 )
					map.put(BOTTOM, comment);
				else 
					map.put(BOTTOM, comment.substring(0, 30) + "...");
				this.infoList.add(map);
			}
			
			Vector<Buddy> buddies =  this.dive.getBuddies();
			for ( Buddy buddy : buddies ) {
				map = new HashMap<String, String>();
				map.put(ICON, R.drawable.scuba_diver + "");
				map.put(TOP, BUDDY + " (Role: " + buddy.getRole() + ")");
				String buddyCertOrg = buddy.getCertOrg();
				String bottomString = buddy.getName();
				if ( buddyCertOrg != null ) {
					bottomString += " (" + buddyCertOrg;
					String buddyCertNr = buddy.getCertNr();
					if ( buddyCertNr != null )
						bottomString += ": " + buddyCertNr;
					bottomString += ")";
				}
				map.put(BOTTOM, bottomString);
				map.put("hiddenFilename", buddy.getFilename());
				map.put("hiddenNodeID", buddy.getNodeID());
				infoList.add(map);
			}
			
			SimpleAdapter adapterMainList = new SimpleAdapter(this, infoList, 
					R.layout.list_entry, new String[]{ ICON, TOP, BOTTOM },
					new int[] { R.id.icon, R.id.topText, R.id.bottomText });
			list.setAdapter(adapterMainList);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		
		String image = dive.getGeoImage();
		if ( image != null ) {
			try {
				ImageView geoPicView = (ImageView)findViewById(R.id.geoPicture);
				geoPicView.setImageDrawable(ImageHelper.rotateImage(image));
				geoPicView.setScaleType(ScaleType.CENTER_INSIDE);
			} catch (NullPointerException e) { }
		}
		
		/**
		 * TODO: Implement here a statistic of the dive (__read out from a dive computer__)
		 *       x-values ... the time
		 *       y-values ... the deep
		 */
		ListView layout = (ListView)findViewById(R.id.stat);
		layout.setOnItemClickListener(this.graphClickListener);
		this.graphList = new ArrayList<HashMap<String, String>>();
		
		HashMap<String, String> map = new HashMap<String, String>();
		map.put(ICON, R.drawable.info_icon + "");
		map.put(TOP, TEMPERATURE_PRESSURE);
		map.put(BOTTOM, "Shows the water temperature and the tank pressure during the dive.");
		this.graphList.add(map);
		
		map = new HashMap<String, String>();
		map.put(ICON, R.drawable.info_icon + "");
		map.put(TOP, DIVE_PROFILE);
		map.put(BOTTOM, "Shows the dive profile (depth in relation to the time).");
		this.graphList.add(map);
		
		SimpleAdapter adapterGraphList = new SimpleAdapter(this, graphList, 
				R.layout.list_entry, new String[]{ ICON, TOP, BOTTOM },
				new int[] { R.id.icon, R.id.topText, R.id.bottomText });
		layout.setAdapter(adapterGraphList);
	}
}
