from landmarks.models import *
from pyquery import PyQuery as pq
import urllib
d = pq(url="https://nycopendata.socrata.com/api/views/crwx-795d/rows.xml?accessType=DOWNLOAD", opener=lambda url: urllib.urlopen(url).read())
cnt = 0

for item in d("row"):
	if cnt:
		name = d("lm_name", item).text()
		longitude = d("shape", item).attr("longitude")
		latitude = d("shape", item).attr("latitude")
		address = d("desig_addr", item).text()
		lm = nylandmark(name = name, latitude = latitude, longitude = longitude, address = address) 
		lm.save()
	cnt += 1
