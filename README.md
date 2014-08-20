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
<b>/resourceregions</b> or <b>/rr</b><br>

- <b>/rr create <regionName></b> - create a new region with the given name
- <b>/rr url \<http://url_to_pack.zip\></b> - set the selected region's pack url
- <b>/rr weight \<#weightValue\></b> - set the selected region's weight
- <b>/rr bounds</b> - set your current worldedit selection as the selected region's bounds
- <b>/rr save</b> - save the changes to the selected region
- <b>/rr load \<regionName\></b> - load an existing region to edit
- <b>/rr reload</b> - reload regions from disk - must be done to see edits made to any regions
