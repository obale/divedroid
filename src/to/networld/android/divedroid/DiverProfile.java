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

import to.networld.android.divedroid.model.rdf.Buddy;
import to.networld.android.divedroid.model.rdf.Diver;
import to.networld.android.divedroid.model.rdf.Equipment;
import to.networld.android.divedroid.model.rdf.IDiver;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;

/**
 * Shows information about the diver.
 * 
 * @author Alex Oberhauser
 * 
 */
public class DiverProfile extends Activity {
	private final Context context = DiverProfile.this;
	
	private IDiver agent;
	
	private static final String BOTTOM = "bottom";
	private static final String TOP = "top";
	private static final String ICON = "icon";
	private static final String ARROW = "nextArrow";

	private static final String NAME = "Name";
	private static final String EMAIL = "E-Mail";
	private static final String PHONE = "Telephone Number";
	private static final String ROLE = "Role";
	private static final String CERTORG = "Certification Organisation";
	private static final String CERTNR = "Certification Number";
	private static final String CERTDATE = "Certification Date";

	private static final String TOTAL_DIVES = "Total Dives";
	private static final String EQUIPMENT = "Equipment";
	
	private ListView list;

	private final Handler guiHandler = new Handler();
	private final Runnable updateProfile = new Runnable() {
		@Override
		public void run() {
			updateProfileInGUI();
		}
	};

	private ArrayList<HashMap<String, String>> profileList;

	private void updateProfileInGUI() {
		SimpleAdapter adapterProfileList = new SimpleAdapter(context,
				profileList, R.layout.list_entry, new String[] { ICON, TOP,
						BOTTOM, ARROW }, new int[] { R.id.icon, R.id.topText,
						R.id.bottomText, R.id.nextArrow });
		list.setAdapter(adapterProfileList);
	}

	private OnItemClickListener listClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> list, View view, int position,
				long id) {
			HashMap<String, String> entry = profileList.get(position);
			
			if ( entry.get(TOP).equals(EMAIL) ) {
				final Intent eMailIntent = new Intent(android.content.Intent.ACTION_SEND);
				eMailIntent.setType("text/html");
				eMailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,
						new String[] { entry.get(BOTTOM) });
				startActivity(eMailIntent);
			} else if ( entry.get(TOP).equals(PHONE) ) {
				final Intent phoneIntent = new Intent(android.content.Intent.ACTION_DIAL);
				phoneIntent.setData(Uri.parse(entry.get(BOTTOM)));
				startActivity(phoneIntent);
			}
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		final ProgressDialog progressDialog = ProgressDialog.show(
				DiverProfile.this, null, "Preparing Diver Profile...", false);
		setContentView(R.layout.diverprofile);
		String agentURL = getIntent().getStringExtra("filename");
		String nodeID = getIntent().getStringExtra("nodeid");
		final boolean isBuddy = getIntent().getBooleanExtra("isbuddy", true);
		try {
			if ( isBuddy )
				agent = new Buddy(new File(agentURL), nodeID);
			else 
				agent = new Diver(new File(agentURL), nodeID);
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("[***************] Error");
			progressDialog.dismiss();
			new GenericDialog(context, "Error", e.getLocalizedMessage(),
					R.drawable.error_icon);
			return;
		}

		this.list = (ListView) findViewById(R.id.profileList);
		this.list.setOnItemClickListener(listClickListener);
		this.profileList = new ArrayList<HashMap<String, String>>();

		Thread worker = new Thread() {
			@Override
			public void run() {
				/**
				 * Name
				 */
				HashMap<String, String> map = new HashMap<String, String>();
				map.put(ICON, R.drawable.info_icon + "");
				map.put(TOP, NAME);
				map.put(BOTTOM, agent.getName());
				profileList.add(map);

				String role = agent.getRole();
				if ( role != null ) {
					map = new HashMap<String, String>();
					map.put(ICON, R.drawable.scuba_diver + "");
					map.put(TOP, ROLE);
					map.put(BOTTOM, role);
					profileList.add(map);
				}
				
				String certorg = agent.getCertOrg();
				if ( certorg != null ) {
					map = new HashMap<String, String>();
					map.put(ICON, R.drawable.certificate_icon + "");
					map.put(TOP, CERTORG);
					map.put(BOTTOM, certorg);
					profileList.add(map);
				}
				
				String certnr = agent.getCertNr();
				if ( certnr != null ) {
					map = new HashMap<String, String>();
					map.put(ICON, R.drawable.certificate_icon + "");
					map.put(TOP, CERTNR);
					map.put(BOTTOM, certnr);
					profileList.add(map);
				}
				
				String certdate = agent.getCertDate();
				if ( certdate != null ) {
					map = new HashMap<String, String>();
					map.put(ICON, R.drawable.certificate_icon + "");
					map.put(TOP, CERTDATE);
					map.put(BOTTOM, certdate);
					profileList.add(map);
				}
				
				String email = agent.getEMail();
				if ( email != null ) {
					map = new HashMap<String, String>();
					map.put(ICON, R.drawable.email_icon + "");
					map.put(TOP, EMAIL);
					map.put(BOTTOM, email);
					profileList.add(map);
				}
				
				String phone = agent.getPhone();
				if ( phone != null ) {
					map = new HashMap<String, String>();
					map.put(ICON, R.drawable.tel_icon + "");
					map.put(TOP, PHONE);
					map.put(BOTTOM, phone);
					profileList.add(map);
				}
				
				if ( !isBuddy ) {
					Diver myself = (Diver)agent;
					
					String totalDives = myself.getTotalDives();
					if ( totalDives != null ) {
						map = new HashMap<String, String>();
						map.put(ICON, R.drawable.info_icon + "");
						map.put(TOP, TOTAL_DIVES);
						map.put(BOTTOM, totalDives);
						profileList.add(map);
					}
					
					Vector<Equipment> equipment = myself.getEquipment();
					for ( Equipment entry : equipment ) {
						map = new HashMap<String, String>();
						map.put(ICON, R.drawable.equipment_icon + "");
						map.put(TOP, EQUIPMENT + ": " + entry.getType());
						String name = entry.getBrand() + " - " + entry.getModel();
						map.put(BOTTOM, name);
						profileList.add(map);
					}
				}

				guiHandler.post(updateProfile);
				progressDialog.dismiss();
			}
		};
		worker.start();
	}
}
