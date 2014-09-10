ResourceRegions
===============

Regional texture-switching

#### Region

- name: the name of the region
- packUrl: the url to the region's resourcepack
- weight: the 'weight' (see below) that the region holds
- xpoints: an array of x co-ordinates
- zpoints: an array of z co-ordinates
 
<b>Weight</b><br>
<i>Weights determine which region's pack will be applied when<br>
the user is between two or more overlapping regions.</i>

#### Region Commands
<b>/resourceregions</b> or <b>/rr</b> - prints available region commands<br>
'<b>resourceregions.region</b>' - permission node for the /rr command

- <b>/rr create <regionName></b> - create a new region with the given name
- <b>/rr url \<http://url_to_pack.zip\></b> - set the selected region's pack url
- <b>/rr weight \<#weightValue\></b> - set the selected region's weight
- <b>/rr bounds</b> - set your current worldedit selection as the selected region's bounds
- <b>/rr save</b> - save the changes to the selected region
- <b>/rr load \<regionName\></b> - load an existing region to edit
- <b>/rr reload</b> - reload regions from disk - must be done to see edits made to any regions

#### Step-by-step - Create
1. /rr create myAwesomeNewRegion
2. /rr url http://herp.a/derp.zip
3. /rr weight 3
4. /rr bounds
5. /rr save
6. /rr reload

#### Step-by-step - Edit
1. /rr load existingRegion
2. /rr url http://a.new/url.zip, /rr weight 5, etc...
3. /rr save
4. /rr reload
