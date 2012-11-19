package com.columbia.location;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.widget.Toast;

public class GPSHelper {
	
	public static UserPoint getLocation(Context context) {
		LocationManager locMgr = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);
		LocationProvider locProvider = locMgr
				.getProvider(LocationManager.NETWORK_PROVIDER);
		String provider = locProvider.getName();
		Location location = locMgr.getLastKnownLocation(provider);
		if (null == location) {
			Toast.makeText(
					context,
					"Cannot acquire location. Are your location settings enabled?",
					Toast.LENGTH_SHORT).show();
			return null;
		}
		double lon = (double) (location.getLongitude() * 1E6);
		double lat = (double) (location.getLatitude() * 1E6);
		return new UserPoint((int) lat, (int) lon);
	}

}
