package org.apache.cordova.plugin;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;

import org.apache.cordova.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.Resources.NotFoundException;
import android.os.Build;
import android.util.Log;

public class Html5Video extends CordovaPlugin {
	private static final String TAG = "Html5VideoCordovaPlugin";
	
	public final String ACTION_INITIALIZE = "initialize";
	public final String ACTION_PLAY = "play";

	FileDataStore dataStore = new FileDataStore();
	
	@Override
	public boolean execute(String action, JSONArray args, final CallbackContext callbackContext) throws JSONException {

		boolean status = false;

		if (action.equals(ACTION_INITIALIZE)) {
			String packageName = this.cordova.getActivity().getPackageName();
			JSONObject videos = args.getJSONObject(0);
			JSONArray tagNames = videos.names();
			JSONObject convertedVideos = new JSONObject();

			if (tagNames != null) {
				for (int i = 0; i < tagNames.length(); i++) {
					String[] video = videos.getString(tagNames.getString(i)).split("\\.");
					String newUrl = null;
					
					// Kitkat and above don't allow loading android.resource:// URLs,
					// (see https://code.google.com/p/android/issues/detail?id=63033 ),
					// so we do a little trick.
					if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
						// copy vids to global-accessible directory, then replace with that URL.
						// idea from this thread:  https://github.com/jaeger25/Html5Video/issues/23#issuecomment-51174558
						String newFilePath = dataStore.getFilePath(video[0]);
						
						if (newFilePath != null) {
							newUrl = "file://" + newFilePath;
							Log.d(TAG, "Using KitKat, can't load local videos. Loading from: " + newUrl);
						}
					}
					

					if (newUrl == null) { // do the old way if the KitKat way failed (or if we're not on KitKat)
						int videoId = this.cordova.getActivity().getResources().getIdentifier(video[0], "raw", packageName);
						newUrl = "android.resource://" + packageName + "/" + videoId; 
					}
					
					convertedVideos.put(tagNames.getString(i), newUrl);
					
					LOG.d("Html5Video", "Id: " + tagNames.getString(i) + " , src: " + convertedVideos.getString(tagNames.getString(i)));
				}
				
				callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, convertedVideos));				
				status = true;
			}
		} else if (action.equals(ACTION_PLAY)) {
			final String videoId = args.getString(0);
			if (videoId != null) {
				cordova.getActivity().runOnUiThread(new Runnable() {
				     public void run() {
				           webView.loadUrl("javascript:window.plugins.html5Video._play(" + videoId + ")");
				     }
				});
				status = true;
				
				LOG.d("Html5Video", "Playing video with id: " + videoId);
			}
		}

		return status;
	}

	@Override
	public Object onMessage(String id, Object data) {
		return super.onMessage(id, data);
	}
	
	
	private class FileDataStore {
		
		/***
		 * Gets the file:// URL of a video file in the res/raw directory, copying it over if necessary.
		 * 
		 * Note: the file will be world-readable (not-recommended by Google), so be careful: other apps will be able to read your videos!
		 * 
		 * @param rawFileName Name of the file in the raw directory.
		 * @return The absolute path of the file, or null if copy failed.
		 */
		public String getFilePath(String rawFileName) {
			String fileName = "html5video_" + rawFileName; // path separators not allowed
			File f = cordova.getActivity().getFileStreamPath(fileName);
			
			// transfer raw resrouce file into app data directory so it can be read globally.
			if (!f.exists()) {				
				
				if (!copyResourceFileToDataDir(rawFileName, fileName)) {
					return null;
				}
			}
			
			return f.getAbsolutePath();
		}
		
		/***
		 * Copies a raw file from resources directory to the app's private data directory.
		 * @param rawFileName
		 * @param newFileName
		 * @return True on success, false otherwise
		 */
		private boolean copyResourceFileToDataDir(String rawFileName, String newFileName) {
			String packageName = cordova.getActivity().getPackageName();
			
			AssetFileDescriptor fd = null;
			BufferedInputStream in = null;
			BufferedOutputStream out = null;
			try {
				Activity activity = cordova.getActivity();
				int id = activity.getResources().getIdentifier(rawFileName, "raw", packageName);
				fd = activity.getResources().openRawResourceFd(id);
				if (fd == null) {
					throw new NotFoundException("Raw file not found: " + rawFileName);
				}
				
				in = new BufferedInputStream(fd.createInputStream()); // this is auto-close!
				// well we need world readable because that's the only way chrome can read it!
				// don't worry about their scare-tactic "oooh security holes" warnings: who cares if other apps can read your videos?
				out = new BufferedOutputStream(activity.openFileOutput(newFileName, Context.MODE_WORLD_READABLE));
				
				// copy the file over. it's 2014, we still have to do this by hand?
				byte buffer[] = new byte[8192];
				while(in.available() > 0) {
					int bytesRead = in.read(buffer);
					
					if (bytesRead > 0) {
						out.write(buffer, 0, bytesRead);
					}
				}
				
				in.close();
				out.close();
				
				return true;
			} catch (NotFoundException e) {
				Log.e(TAG, "Failed to copy video file to data dir.", e);
			} catch (IOException e) {
				Log.e(TAG, "Failed to copy video file to data dir.", e);
			}
			finally {

				try {
					if (fd != null) {
						fd.close();					
					}
				} catch (IOException e) {
					Log.e(TAG, "Failed to fail to copy video file to data dir.", e);
				}
					
				try {
					if (in != null) {
						in.close();
					}
				} catch (IOException e) {
					Log.e(TAG, "Failed to fail to copy video file to data dir.", e);
				}
					
				try {
					if (out != null) {
						out.close();
					}
				} catch (IOException e) {
					Log.e(TAG, "Failed to fail to copy video file to data dir.", e);
				}
			}
			
			return false;
		}
	}
}
