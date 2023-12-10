package com.example.shoping.common;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class Common {

    public static float getBearing(LatLng begin, LatLng end) {
        double lat=Math.abs(begin.latitude-end.latitude);
        double lng=Math.abs(begin.longitude-end.longitude);
        if(begin.latitude <end.latitude && begin.longitude<end.longitude)
            return (float) (Math.toDegrees(Math.atan(lng/lat)));
else if (begin.latitude >=end.latitude && begin.longitude>=end.longitude)
            return (float) ((90-Math.toDegrees(Math.atan(lng/lat)))+90);
        else if (begin.latitude >=end.latitude && begin.longitude>=end.longitude)
            return (float) (Math.toDegrees(Math.atan(lng/lat))+180);
        else if (begin.latitude < end.latitude && begin.longitude>=end.longitude)
            return (float) ((90-Math.toDegrees(Math.atan(lng/lat)))+270);
        return  -1;
    }

    public static List<LatLng> decodePoly(String encoded) {
        List<LatLng> poly = new ArrayList<>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1F) << shift;
                shift += 5;
            } while (b >= 0x20);

            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1F) << shift;
                shift += 5;
            } while (b >= 0x20);

            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((double) lat / 1E5, (double) lng / 1E5);
            poly.add(p);
        }
        return poly;
    }
}
