# Cordova Html5Video Plugin #

## Description ##

This plugin allows playback of local video files using embedded html 5 video tags. This repository contains both the plugin files and a sample Android application that uses the plugin. Tested on Android API 15 - 17


## Setup ##

1. Create your cordova project: cordova create ProjectName
2. Add platforms: cordova platform add android
3. Add the plugin: cordova plugin add https://github.com/jaeger25/Html5Video.git

The next changes should be done inside your PlatformName/www folder

4. Create any video tags within your html pages as normal, making sure to give each video tag an id. The video tags should be empty, ie no <source> tags inside
5. Add any poster image files to your www folder 


The next changes should be done inside your ProjectName/platforms/android folder

6. Make sure the <manifest> tag in AndroidManifest.xml has android:hardwareAccelerated="true"
7. Create a folder called raw inside the res folder and put any video files in it. (not poster image files)



8. run cordova build
9. launch app

## Code Usage ##

After device ready, call window.plugins.html5Video.initialize({"video1":"video1file.mp4", "video2":"video2file.mp4"}).

Anytime you wish to play a video, call window.plugins.html5Video.play("video1", callback).