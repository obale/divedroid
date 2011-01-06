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
