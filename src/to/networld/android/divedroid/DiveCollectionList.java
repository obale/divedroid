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
import java.util.List;
import java.util.Vector;

import to.networld.android.divedroid.model.DiveCollectionHandler;
import to.networld.android.divedroid.model.MediaHandler;
import to.networld.android.divedroid.model.map.CollectionOverlay;
import to.networld.android.divedroid.model.rdf.Dive;
import to.networld.android.divedroid.model.rdf.DiveCollection;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;

/**
 * 
 * @author Alex Oberhauser
 *
 */
public class DiveCollectionList extends MapActivity {
	private final DiveCollectionHandler collectionHandler = new DiveCollectionHandler();
	private Vector<DiveCollection> collections; 
	
	private MapController mapControl;
	private List<Overlay> mapOverlays;
	private Drawable mapIcon;
	private CollectionOverlay collectionOverlay;
	
	private static final String BOTTOM = "bottom";
	private static final String TOP = "top";
	private static final String ICON = "icon";
	private static final String ARROW = "rightArrow";
	
	private final Handler guiHandler = new Handler();
	private final Runnable updateCollection = new Runnable() {
		@Override
		public void run() {
			 updateGUI();
		}
	};
	
	private OnItemClickListener listClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> list, View view, int position, long id) {	
			DiveCollection collection = collections.get(position);
			final Vector<Dive> dives = collection.getDives();
			final CharSequence[] items = new CharSequence[dives.size()];
			AlertDialog.Builder builder = new AlertDialog.Builder(DiveCollectionList.this);
			builder.setTitle("Dives " + collection.getDiveBase());
			int count = 0;
			for (Dive dive : dives) {
				if ( dive.getName() != null ) {
					items[count] = dive.getName() + "\n- " + dive.getActivity() + " -";
					count++;
				} else {
					items[count] = "Dive #" + count;
					count++;
				}
			}
			
			builder.setItems(items, new DialogInterface.OnClickListener() {
			    public void onClick(DialogInterface dialog, int item) {
                    Intent profileIntent = new Intent(DiveCollectionList.this, DiveProfile.class);
                    Dive dive = dives.get(item);
                    profileIntent.putExtra("filename", dive.getFilename());
                    profileIntent.putExtra("nodeid", dive.getNodeID());
                    startActivity(profileIntent);
			    }
			});
			builder.create().show();
		}
	};
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.divecollection);
        
        final MapView mapView = (MapView) findViewById(R.id.mapView);
        mapView.setSatellite(true);
        mapOverlays = mapView.getOverlays();
        
        this.mapIcon = getResources().getDrawable(R.drawable.map_icon);
        this.collectionOverlay = new CollectionOverlay(mapIcon);
        
		ListView list = (ListView) findViewById(R.id.collectionList);
		list.setOnItemClickListener(this.listClickListener);
        
        final ProgressDialog progressDialog = ProgressDialog.show(DiveCollectionList.this, null, "Searching for Dive Collections...", false);
        
        Thread seeker = new Thread() {
        	@Override
        	public void run() {
        		Looper.prepare();
        		try {
        			mapControl = mapView.getController();
        			mapControl.setZoom(2);
        			mapControl.stopPanning();
        			mapControl.setCenter(new GeoPoint(0, 0));
        			
        			MediaHandler mediaHandler = new MediaHandler(); 
        			File[] files = mediaHandler.getFileList();
        			for ( int count=0; count < files.length; count++ ) {
        				collectionHandler.addDiveCollection(files[count]);
        			}
        		} finally {
        			guiHandler.post(updateCollection);
        			progressDialog.dismiss();
        		}
        	}
        };
        seeker.start();
    }
	
	/**
	 * Reads the dive collections from the handler class and stores
	 */
	public void updateGUI() {
		ArrayList<HashMap<String, String>> collectionList = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> map;
		
		this.collections = this.collectionHandler.getDiveCollections();
		/**
		 * TODO: Sort the dive collection so that the last made dive collection is shown first. 
		 */
		for ( DiveCollection entry : collections ) {
			map = new HashMap<String, String>();
			map.put(ICON, R.drawable.logbook + "");
			String divebase = entry.getDiveBase();
			if ( divebase == null || divebase.equals("") )
				divebase = "Collection: " + entry.getID();
			map.put(TOP, divebase);
			map.put(BOTTOM, entry.getCountry() + " (" + entry.getStartDate() + " - " + entry.getStopDate() + ")");
			map.put(ARROW, R.drawable.right_arrow_icon + "");
			collectionList.add(map);	
			
			try {
				double latDouble = new Double(entry.getLatitude());
				double lonDouble = new Double(entry.getLongitude());
				if ( latDouble != 0.0 && lonDouble != 0.0 ) {
					int lat = (int) (latDouble * 1E6);
					int lon = (int) (lonDouble * 1E6);
					GeoPoint geo = new GeoPoint(lat, lon);
					OverlayItem overlayitem = new OverlayItem(geo, entry.getCountry(), "");
					this.collectionOverlay.addOverlay(overlayitem);   
				}
			} catch (Exception e) {
				// No GPS coordinates, no icon on the map...
			}
		}
		
		ListView list = (ListView)findViewById(R.id.collectionList);
		SimpleAdapter adapterCollectionList = new SimpleAdapter(DiveCollectionList.this, collectionList, 
				R.layout.list_entry, new String[]{ ICON, TOP, BOTTOM, ARROW },
				new int[] { R.id.icon, R.id.topText, R.id.bottomText, R.id.nextArrow });
		list.setAdapter(adapterCollectionList);
        this.mapOverlays.add(this.collectionOverlay);
	}

	@Override
	protected boolean isRouteDisplayed() { return false; }
}
