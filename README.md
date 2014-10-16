# Cordova Html5Video Plugin #

### Description ###

This plugin enables the use of normal HTML5 <video> tags for local video - otherwise not functional in Cordova for Android. If you are looking to simply play videos in full screen started by JS, try the [VideoPlayer plugin for Phonegap](https://github.com/macdonst/VideoPlayer)

Android Webview (which Cordova is based upon) limits access to local files (such as videos) and prohibits reading them, both via relative files and `file:///` URIs. Cordova Html5Video Plugin solves this by giving each of you video a `android.resource://` path and updating your `<video>` tags accordingly.

### Limitations ###
For Android only. Tested on Android API 15-19.

For API >= 19, a workaround is employed to copy the video files over to your application's data directory, as world-readable.

*WARNING*: this is potentially insecure - other apps will be able to read your videos! However it is the only way to get around Chrome's strict limitations on content:// URLs. See [Issue #20](https://github.com/jaeger25/Html5Video/issues/20) for details.

### Install ###

 ``cordova plugin add https://github.com/jaeger25/Html5Video.git ``

For more help on installing Cordova plugins, please read the official [documentation](http://docs.phonegap.com/en/edge/guide_cli_index.md.html#The%20Command-Line%20Interface_add_plugin_features)

### HTML ###

Create any video tags withing your html pages as normal, but: 

1. Give each video tag a unique ID. 
2. The video tags should be empty (i.e no <source> tags inside)
3. Add any poster image files to your www folder
4. Autoplay is not enabled, but controlled via JS instead

Example:

``<video id="myvideo" loop></video>``

The next changes should be done inside your ProjectName/platforms/android folder

### Enable Hardware Acceleration in AndroidManifest.xml ###

Android Webview disables video for none-hardware accelerated applications. Thus, make sure your manifest tag in AndroidManifest.xml contains:

``android:hardwareAccelerated="true"``

Consult the official Android [documentation](http://developer.android.com/guide/topics/graphics/hardware-accel.html) if you require more help. This is enabled by default in newer versions of cordova.


### Move video files ###

Create a folder called raw inside the res folder (`$PROJECT_ROOT/platforms/android/res/raw`) where you place your videos. Due to limitations in how Android uses the res folder, make sure:

1. Your filenames doesn't contain spaces and special characters
2. That all filenames are unique and doesn't contain period (.) delimiters. Android considers `video.lowres.mp4` and `video.highres.mp4` the same file

If your file doesn't meet the above requirements `cordova build` will fail to compile your application.

### Client-side code (javascript) ###

After [deviceReady](http://docs.phonegap.com/en/edge/cordova_events_events.md.html#deviceready) callback from Cordova, initialize your videos with:

```javascript
window.plugins.html5Video.initialize( { "id":"path", ... } ) 
```

Example:

```javascript
window.plugins.html5Video.initialize({
      "video1" : "video1file.mp4", 
     "video2" : "video2file.mp4"
  })
```

### Playing a video ###
Anytime you wish to play a video, call:

```javascript
window.plugins.html5Video.play("video1" [, callback ])
```

Where callback is optional and is triggered when the video is finished (at the end)

Examples:

Play video1 with no callback:

```javascript
window.plugins.html5Video.play("video1")
```

Play video1 and call function finished:

```javascript
window.plugins.html5Video.play("video1", finished).
```

Play video1 with [anonymous function](http://kangax.github.io/nfe/#names-in-debuggers):

```javascript
window.plugins.html5Video.play("video1", function videoIsFinished() {
  console.log("Video is finished")
})
```