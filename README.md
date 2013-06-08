Make sure the <manifest> tag in AndroidManifest.xml has android:hardwareAccelerated="true"

1. Add <plugin name="Html5Video" value="org.apache.cordova.plugin.Html5Video"/> to config.xml
2. Add the file Html5Video.java from the org.apache.cordova.plugin package to your src folder
3. Create a folder called raw inside the res folder and put any video files in it. (not poster image files)
4. Add any poster image files to your www folder 
5. Create any video tags within your html pages as normal, making sure to give each video tag an id. The video tags should be empty, ie no <source> tags inside
6. Add the Html5Video.js file to your project's js folder and include it in index.html after cordova.js
7. After device ready, call window.plugins.html5Video.initialize(videos, callback). See Html5Video.js and the example project for usage details