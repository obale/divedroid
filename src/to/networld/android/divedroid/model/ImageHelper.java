package to.networld.android.divedroid.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

/**
 * Image Manipulation class.
 * 
 * @author Alex Oberhauser
 *
 */
public class ImageHelper {
	public static Drawable rotateImage(String _imageFilePath) {
		Bitmap orgBmp = BitmapFactory.decodeFile(_imageFilePath);
		Matrix matrix = new Matrix();
		matrix.postRotate(90);
		Bitmap resizedBitmap = Bitmap.createBitmap(orgBmp, 0, 0, orgBmp.getWidth(), orgBmp.getHeight(), matrix, true);
		return new BitmapDrawable(resizedBitmap);
	}
}
