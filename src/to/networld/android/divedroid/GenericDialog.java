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

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 
 * @author Alex Oberhauser
 * 
 */
public class GenericDialog extends Dialog {
	private final int icon;
	private final String errorTitle;
	private final String errorMessage;

	private final android.view.View.OnClickListener okButtonListener = new android.view.View.OnClickListener() {
		public void onClick(View _view) {
			dismiss();
		}
	};

	public GenericDialog(Context _context, String _errorTitle,
			String _errorMessage, int _icon) {
		super(_context);
		this.icon = _icon;
		this.errorTitle = _errorTitle;
		this.errorMessage = _errorMessage;
	}

	@Override
	public void onStart() {
		this.setContentView(R.layout.alert_dialog);
		this.setCancelable(true);
		this.setTitle(this.errorTitle);
		TextView text = (TextView) this.findViewById(R.id.alert_msg);
		text.setText(this.errorMessage);
		ImageView image = (ImageView) this.findViewById(R.id.alert_image);
		image.setImageResource(this.icon);
		Button okButton = (Button) this.findViewById(R.id.alert_ok);
		if (okButton != null)
			okButton.setOnClickListener(this.okButtonListener);
	}

}
