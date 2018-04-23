package com.csform.android.uiapptemplate;

import java.io.File;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

public class FileCache {

	private File cacheDir;
	
	public FileCache(Context context) {
		
		if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			Log.e("making directory....", "inside if up");
			cacheDir = new File(Environment.getExternalStorageDirectory(), "LazyList");
			Log.e("making directory....", "inside if down");
		} else {
			Log.e("making directory....", "inside else up");
			cacheDir = context.getCacheDir();
			Log.e("making directory....", "inside else down");
		}
		
		if(!cacheDir.exists()) {
			Log.e("creating folder...", "before creating...");
			cacheDir.mkdirs();
			Log.e("creating folder...", "after creating...");
		}
	}
	
	public File getFile(String url) {
		
		String filename = String.valueOf(url.hashCode());
		
		File f = new File(cacheDir, filename);
		return f;
	}
	
	public void clear() {
		File[] files = cacheDir.listFiles();
		if(files == null) {
			return;
		}
		
		for(File f : files) {
			f.delete();
		}
	}
}