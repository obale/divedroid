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

package to.networld.android.divedroid.model;

import java.io.File;
import java.io.FilenameFilter;

import android.os.Environment;

/**
 * 
 * @author Alex Oberhauser
 *
 */
public class MediaHandler {

	public static final String BUDDY_DIR = "/buddies";
	public static final String PROFILE_DIR = "/profiles";
	public static final String SAT_PIC_DIR = "/sat_pic";
	
	boolean storageReadable = false;
	boolean storageWriteable = false;
	
	public File[] getFileList() {
		this.checkAccess();
		if ( !this.storageReadable)
			return new File[0];
		
		File fd = new File(Environment.getExternalStorageDirectory(), "divedroid");
		fd.mkdir();
		
		FilenameFilter filter = new FilenameFilter() {
			@Override
			public boolean accept(File dir, String filename) {
				if ( filename.endsWith(".rdf") ) return true;
				else if ( filename.endsWith(".owl") ) return true;
				else if ( filename.endsWith(".xml") ) return true;
				return false;
			}
		};
		File[] files = fd.listFiles(filter);
		if ( files != null )
			return files;
		else
			return new File[0];
	}
	
	public String getBuddyPath() {
		this.checkAccess();
		if ( !this.storageReadable)
			return "";
		File fd = new File(Environment.getExternalStorageDirectory(), "divedroid");
		return fd.getAbsolutePath() + BUDDY_DIR;
	}
	
	public String getProfilePath() {
		this.checkAccess();
		if ( !this.storageReadable )
			return "";
		File fd = new File(Environment.getExternalStorageDirectory(), "divedroid");
		return fd.getAbsolutePath() + PROFILE_DIR;
	}

	public void checkAccess() {
		String state = Environment.getExternalStorageState();
		if ( Environment.MEDIA_MOUNTED.equals(state) ) {
			storageReadable = storageWriteable = true;
		} else if ( Environment.MEDIA_MOUNTED_READ_ONLY.equals(state) ) {
			storageReadable = true;
			storageWriteable = false;
		} else {
			storageReadable = false;
			storageWriteable = false;
		}
	}
}
