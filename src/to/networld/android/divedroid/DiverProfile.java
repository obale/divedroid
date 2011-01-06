package to.networld.android.divedroid;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import org.dom4j.DocumentException;

import to.networld.android.divedroid.model.rdf.Buddy;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;

/**
 * Shows information about a dive buddy.
 * 
 * @author Alex Oberhauser
 * 
 */
public class DiverProfile extends Activity {
	private final Context context = DiverProfile.this;
	
	private Buddy agent;
	
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

			if (entry.get(TOP).equals(NAME)) {
				final Intent eMailIntent = new Intent(
						android.content.Intent.ACTION_SEND);
				eMailIntent.setType("text/html");
				eMailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,
						new String[] { entry.get(BOTTOM) });
				startActivity(eMailIntent);

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
		try {
			agent = new Buddy(new File(agentURL), nodeID);
		} catch (DocumentException e) {
			e.printStackTrace();
			new GenericDialog(context, "Error", e.getLocalizedMessage(),
					R.drawable.error_icon);
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
					map.put(ICON, R.drawable.info_icon + "");
					map.put(TOP, CERTORG);
					map.put(BOTTOM, certorg);
					profileList.add(map);
				}
				
				String certnr = agent.getCertNr();
				if ( certnr != null ) {
					map = new HashMap<String, String>();
					map.put(ICON, R.drawable.info_icon + "");
					map.put(TOP, CERTNR);
					map.put(BOTTOM, certnr);
					profileList.add(map);
				}
				
				String certdate = agent.getCertDate();
				if ( certdate != null ) {
					map = new HashMap<String, String>();
					map.put(ICON, R.drawable.info_icon + "");
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
				if ( email != null ) {
					map = new HashMap<String, String>();
					map.put(ICON, R.drawable.tel_icon + "");
					map.put(TOP, PHONE);
					map.put(BOTTOM, phone);
					profileList.add(map);
				}

				guiHandler.post(updateProfile);
				progressDialog.dismiss();
			}
		};
		worker.start();
	}
}
