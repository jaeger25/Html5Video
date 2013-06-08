# Cordova Html5Video Plugin #

## Description ##

This plugin allows playback of local video files using embedded html 5 video tags. This repository contains both the plugin files and a sample Android application that uses the plugin. Tested on Android API 15 - 17


## Setup ##

1. Make sure the <manifest> tag in AndroidManifest.xml has android:hardwareAccelerated="true"
2. Add <plugin name="Html5Video" value="org.apache.cordova.plugin.Html5Video"/> to config.xml
3. Add the file Html5Video.java from the org.apache.cordova.plugin package to your src folder
4. Create a folder called raw inside the res folder and put any video files in it. (not poster image files)
5. Add any poster image files to your www folder 
6. Create any video tags within your html pages as normal, making sure to give each video tag an id. The video tags should be empty, ie no <source> tags inside
7. Add the Html5Video.js file to your project's js folder and include it in index.html after cordova.js

## Usage ##

After device ready, call window.plugins.html5Video.initialize(videos). See Html5Video.js for parameter info
Anytime you wish to play a video, call window.plugins.html5Video.play(videoId, callback). See Html5Video.js for parameter info