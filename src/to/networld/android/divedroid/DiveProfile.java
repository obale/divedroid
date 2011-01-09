/**
 * DiveDroid
 *
 * Copyright (C) 2010-2011 by Networld Project
 * Written by Alex Oberhauser <oberhauseralex@networld.to>
 * All Rights Reserved
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by 
 * the Free Software Foundation, version 3 of the License.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this software.  If not, see <http://www.gnu.org/licenses/>
 */

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
 * 
 * @author Alex Oberhauser
 *
 */
public class DiveProfile extends TabActivity {
	private Dive dive;
	private String profileLink = null;
	
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
	private static final String BOATNAME = "Boat Name";
	private static final String WEATHER = "Weather Condition: ";
	private static final String TIME_DEEP = "Bottom Time and Max Deep";
	private static final String TANK_IN_OUT = "Scuba Tank Pressure";
	private static final String WATER_VISIBILITY = "Water Visibility";
	private static final String TEMPERATURE_PRESSURE = "Temperature/Pressure Graph";
	private static final String DIVE_PROFILE = "Dive Profile";
	
	private ArrayList<HashMap<String, String>> infoList;
	private ArrayList<HashMap<String, String>> graphList;
	
	private OnItemClickListener listClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> list, View view, int position, long id) {
			HashMap<String, String> entry = infoList.get(position);

			if ( entry.get(TOP).startsWith(LOCATION) ) {
				String latitude = dive.getLatitude();
				String longitude = dive.getLongitude();
				if ( latitude != null && !latitude.equals("") &&
						longitude != null && !longitude.equals("") ) {
					final Intent mapIntent = new Intent(
							android.content.Intent.ACTION_VIEW, 
							Uri.parse("geo:" + latitude + "," + longitude + "?z=14"));
					startActivity(mapIntent);
				}
			} else if ( entry.get(TOP).startsWith(BUDDY) ) {
				Intent mapIntent = new Intent(DiveProfile.this, DiverProfile.class);
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
				String[] linkParts = profileLink.split("#");
				if ( linkParts.length > 1 ) {
					TemperaturePressureGraph tempGraph = new TemperaturePressureGraph(new File(dive.getPath(), linkParts[0]), linkParts[1]);
					Intent intent = tempGraph.execute(DiveProfile.this);
					if ( intent != null )
						startActivity(intent);
				}
			} else if ( entry.get(TOP).contentEquals(DIVE_PROFILE) ) {
				String[] linkParts = profileLink.split("#");
				if ( linkParts.length > 1 ) {
					DiveProfileGraph diveGraph = new DiveProfileGraph(new File(dive.getPath(), linkParts[0]), linkParts[1]);
					Intent intent = diveGraph.execute(DiveProfile.this);
					if ( intent != null )
						startActivity(intent);
				}
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
	        String filename = getIntent().getStringExtra("filename");
	        String nodeID = getIntent().getStringExtra("nodeid");
			this.dive = new Dive(new File(filename), nodeID);
			
			ListView list = (ListView) findViewById(R.id.profileList);
			list.setOnItemClickListener(this.listClickListener);
			this.infoList = new ArrayList<HashMap<String, String>>();
			
			HashMap<String, String> map;
			
			String activity = this.dive.getActivity();
			if ( activity != null && !activity.equals("") ) {
				map = new HashMap<String, String>();
				map.put(ICON, R.drawable.info_icon + "");
				map.put(TOP, this.dive.getName());
				map.put(BOTTOM, "Activity: " + activity);
				this.infoList.add(map);
			}
	
			String dateTime = this.dive.getDateTime();
			if ( dateTime != null && !dateTime.equals("") ) {
				String[] dateTimeEntries = dateTime.split("T");
				map = new HashMap<String, String>();
				map.put(ICON, R.drawable.cal_icon + "");
				map.put(TOP, DIVE_DATE);
				if ( dateTimeEntries.length > 1 ) {
					map.put(BOTTOM, dateTimeEntries[0] + " at " + dateTimeEntries[1]);
				} else {
					map.put(BOTTOM, dateTimeEntries[0]);
				}
				this.infoList.add(map);
			}
			
			map = new HashMap<String, String>();
			map.put(ICON, R.drawable.bottomtime_icon + "");
			map.put(TOP, TIME_DEEP);
			String bottomTime = this.dive.getBottomTime();
			if ( bottomTime == null || bottomTime.equals("") )
				bottomTime = "--";
			String maxdepth = this.dive.getMaxDepth();
			if ( maxdepth == null || maxdepth.equals("") )
				maxdepth = "--";
			map.put(BOTTOM, bottomTime + " Minutes on maximum " + maxdepth + " meters.");
			this.infoList.add(map);

			String tankIN = this.dive.getScubaTankIn();
			String tankOUT = this.dive.getScubaTankOut();
			map = new HashMap<String, String>();
			map.put(ICON, R.drawable.tank_icon + "");
			map.put(TOP, TANK_IN_OUT);
			String pressure = "";
			if ( tankIN != null && !tankIN.equals("") )
				pressure += "Start = " + tankIN + " bar, ";
			if ( tankOUT != null && !tankOUT.equals("") )
				pressure += "End = " + tankOUT + " bar";
			if ( pressure.equals("") )
				map.put(BOTTOM, "No tank pressure available!");
			else
				map.put(BOTTOM, pressure);
			this.infoList.add(map);

			String visibility = this.dive.getWaterVisibility();
			if ( visibility != null && !visibility.equals("") ) {
				map = new HashMap<String, String>();
				map.put(ICON, R.drawable.visibility_icon + "");
				map.put(TOP, WATER_VISIBILITY);
				map.put(BOTTOM, visibility + " m");
				this.infoList.add(map);
			}
			
			String country = this.dive.getCountry();
			if ( country != null && !country.equals("") ) {
				map = new HashMap<String, String>();
				map.put(ICON, R.drawable.world_icon + "");
				map.put(TOP, COUNTRY);
				map.put(BOTTOM, country);
				this.infoList.add(map);
			}
			
			map = new HashMap<String, String>();
			map.put(ICON, R.drawable.location_icon + "");
			String location = this.dive.getLocation();
			if ( location == null || location.equals("") )
				location = "unknown";
			map.put(TOP, LOCATION + ": " + location);
			String divesite = this.dive.getDivesite();
			if ( divesite == null || divesite.equals("") )
				divesite = "unknown";
			map.put(BOTTOM, "Divesite: " + divesite);
			this.infoList.add(map);
			
			String weather = this.dive.getWeatherCondition();
			if ( weather != null && !weather.equals("") ) {
				map = new HashMap<String, String>();
				map.put(ICON, R.drawable.weather_icon + "");
				map.put(TOP, WEATHER + " " + weather);
				String temperature = "Temperature :- ";
				String airTemp = this.dive.getAirTemperature();
				if ( airTemp != null && !airTemp.equals("") )
					temperature += "Air = " + airTemp + " °C";
				String bottomTemp = this.dive.getBottomTemperature();
				if ( bottomTemp != null && !bottomTemp.equals("") )
					temperature += ", Bottom = " + bottomTemp + " °C";
				map.put(BOTTOM, temperature);
				this.infoList.add(map);
			}
			
			String exposureprotection = this.dive.getExposureProtection();
			if ( exposureprotection != null && !exposureprotection.equals("") ) {
				map = new HashMap<String, String>();
				map.put(ICON, R.drawable.exposuretype_icon + "");
				map.put(TOP, EXPOSURE_TYPE);
				map.put(BOTTOM, exposureprotection);
				this.infoList.add(map);
			}
			
			String entrancetype = this.dive.getEntranceType();
			if ( entrancetype != null && !entrancetype.equals("") ) {
				map = new HashMap<String, String>();
				map.put(ICON, R.drawable.entrancetype_icon + "");
				map.put(TOP, ENTRANCE_TYPE);
				map.put(BOTTOM, entrancetype);
				this.infoList.add(map);
			}
			
			String boatname = this.dive.getBoatName();
			if ( boatname != null && !boatname.equals("") ) {
				map = new HashMap<String, String>();
				map.put(ICON, R.drawable.ship_icon + "");
				map.put(TOP, BOATNAME);
				map.put(BOTTOM, boatname);
				this.infoList.add(map);
			}
			
			String comment = this.dive.getComment();
			if ( comment != null && !comment.equals("") ) {
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
			} catch (Exception e) { }
		}
		
		/*
		 * Diagram Tab
		 */
		ListView layout = (ListView)findViewById(R.id.stat);
		layout.setOnItemClickListener(this.graphClickListener);
		this.graphList = new ArrayList<HashMap<String, String>>();
		
		profileLink = this.dive.getProfileLink();
		if ( profileLink != null ) {
			HashMap<String, String> map = new HashMap<String, String>();

			map.put(ICON, R.drawable.diveprofile_icon + "");
			map.put(TOP, DIVE_PROFILE);
			map.put(BOTTOM, "Shows the dive profile (depth in relation to the time).");
			this.graphList.add(map);
			
			map = new HashMap<String, String>();
			map.put(ICON, R.drawable.graph_icon + "");
			map.put(TOP, TEMPERATURE_PRESSURE);
			map.put(BOTTOM, "Shows the water temperature and the tank pressure during the dive.");
			this.graphList.add(map);
		}
		
		SimpleAdapter adapterGraphList = new SimpleAdapter(this, graphList, 
				R.layout.list_entry, new String[]{ ICON, TOP, BOTTOM },
				new int[] { R.id.icon, R.id.topText, R.id.bottomText });
		layout.setAdapter(adapterGraphList);
	}
}
