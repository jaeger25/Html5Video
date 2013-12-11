package org.apache.cordova.plugin;

import java.util.Timer;

import org.apache.cordova.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Html5Video extends CordovaPlugin {
	public final String ACTION_INITIALIZE = "initialize";
	public final String ACTION_PLAY = "play";

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
					int videoId = this.cordova.getActivity().getResources().getIdentifier(video[0], "raw", packageName);
					convertedVideos.put(tagNames.getString(i), "android.resource://" + packageName + "/" + videoId);
					
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
}
