package com.example.shoping;

import android.Manifest;
import android.animation.ValueAnimator;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.shoping.common.Common;
import com.example.shoping.databinding.ActivityShippingBinding;
import com.example.shoping.model.ShippingOrderModel;
import com.example.shoping.remote.IGoogleAPI;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.JointType;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.SquareCap;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;


public class ShippingActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityShippingBinding binding;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Marker shipperMarker;

    private TextView txtDate;
    private TextView txtOrderNumber;
    private TextView txtName;
    private TextView txtAddress;
    private Button btnStartTrip;
    private Button btnCall;
    private Button btnDone;
    private ImageView imageFood;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private Handler handler;
    private int index, next;
    private LatLng start, end;
    private float v;
    private double lat, lng;
    private Polyline blackPolyline, greyPoliline;
    private PolylineOptions polylineOptions, blackPolylineOptions;
    private List<LatLng> polylineList;
    private IGoogleAPI iGoogleAPI;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private static final int YOUR_PERMISSION_REQUEST_CODE = 123; // Thay đổi thành một giá trị duy nhất
    boolean isInit = false;
    private Location previousLocation = null;
    private ShippingOrderModel shippingOrderModel; // Khai báo biến ở mức độ toàn cục
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shipping);




        // Initialize your Retrofit instance with RxJava3CallAdapterFactory
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://maps.googleapis.com/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build();

        // Create an instance of your IGoogleAPI interface
        iGoogleAPI  = retrofit.create(IGoogleAPI.class);

        btnStartTrip = findViewById(R.id.btnStartTrip);
        btnCall = findViewById(R.id.btnCall);
        btnDone = findViewById(R.id.btnDone);
        txtDate = findViewById(R.id.txt_date);
        txtOrderNumber = findViewById(R.id.txt_order_number);
        txtName = findViewById(R.id.txt_name);
        txtAddress = findViewById(R.id.txt_address);
        imageFood = findViewById(R.id.image_food);
        imageFood.setImageResource(R.drawable.anhne);
        buildLocationRequest();
        buildLocationCallBack();


        previousLocation = new Location("dummyProvider");

        setShippingOrder();

        Dexter.withActivity(this)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                        mapFragment.getMapAsync(ShippingActivity.this::onMapReady);
                        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(ShippingActivity.this);
                        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                        Toast.makeText(ShippingActivity.this, "Bạn phải bật vị trí", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();
        btnStartTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Gọi startTrip và truyền shippingOrderModel vào
                startTrip();

            }
        });
    }


    private void startTrip() {
        if (shipperMarker != null) {
            double startLat = shipperMarker.getPosition().latitude;
            double startLng = shipperMarker.getPosition().longitude;

            // Get the destination location from the address
            String destinationAddress = "364/85 Tô Ký, Tân Chánh Hiệp, Quận 12, Thành phố Hồ Chí Minh, Việt Nam";
            LatLng destinationLatLng = getLocationFromAddress(destinationAddress);

            if (destinationLatLng != null) {
                // Execute DownloadTask asynchronously
                String url = getDirectionsUrl(startLat, startLng, destinationLatLng.latitude, destinationLatLng.longitude);
                DownloadTask downloadTask = new DownloadTask();
                downloadTask.execute(url);
            } else {
                Toast.makeText(ShippingActivity.this, "Không thể xác định vị trí đích", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(ShippingActivity.this, "Vị trí shipper chưa được xác định", Toast.LENGTH_SHORT).show();
        }
    }


    private LatLng getLocationFromAddress(String destinationAddress) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses;

        try {
            addresses = geocoder.getFromLocationName(destinationAddress, 1);
            if (addresses != null && addresses.size() > 0) {
                double latitude = addresses.get(0).getLatitude();
                double longitude = addresses.get(0).getLongitude();
                return new LatLng(latitude, longitude);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }




    private String getDirectionsUrl(double startLat, double startLng, double endLat, double endLng) {
        // Tạo một StringBuilder để xây dựng URL
        StringBuilder url = new StringBuilder("https://maps.googleapis.com/maps/api/directions/json?");
        url.append("origin="); // Vị trí xuất phát
        url.append(startLat).append(",").append(startLng);
        url.append("&destination="); // Vị trí đích
        url.append(endLat).append(",").append(endLng);
        url.append("&mode=driving"); // Chế độ lái xe

        // Thêm khóa API của bạn vào URL
        url.append("&key=AIzaSyBUfbRpjXlRVOGAtX4tJ3uf8MxbuThrItk"); // Thêm khóa API của bạn ở đây

        return url.toString();
    }

    // AsyncTask để tải dữ liệu từ Google Directions URL
    private class DownloadTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... url) {
            String data = "";
            try {
                Log.d("DownloadTask", "URL: " + url[0]); // Log the URL being requested
                // Tạo một đối tượng DownloadURL để thực hiện yêu cầu
                DownloadURL downloadURL = new DownloadURL();
                // Tải dữ liệu từ URL
                data = downloadURL.readUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
                Log.e("DownloadTask", "Error downloading data: " + e.getMessage());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            // Parse dữ liệu JSON và vẽ đường đi trên Google Map
            ParserTask parserTask = new ParserTask();
            parserTask.execute(result);
        }
    }

    // AsyncTask để phân tích cú pháp dữ liệu JSON
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {
            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                // Phân tích cú pháp JSON
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Bắt đầu phân tích dữ liệu
                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            // Log dữ liệu JSON nhận được từ API
            Log.d("DirectionsAPI", "JSON Result: " + result);
            // Vẽ đường đi trên Google Map
            ArrayList<LatLng> points = new ArrayList<>();
            PolylineOptions lineOptions = new PolylineOptions();

            for (int i = 0; i < result.size(); i++) {
                List<HashMap<String, String>> path = result.get(i);

                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                lineOptions.addAll(points);
                lineOptions.width(10);
                lineOptions.color(Color.BLUE);
                lineOptions.geodesic(true);
            }

            // Vẽ đường đi trên Google Map
            mMap.addPolyline(lineOptions);
            // Log dữ liệu nhận được từ API
            Log.d("DirectionsAPI", "Parsed Result: " + result.toString());
        }
    }


    private void setShippingOrder() {
        // ... (Các khối mã khác đã được triể
    }


    private void buildLocationCallBack() {
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);
                LatLng locationShipper = new LatLng(locationResult.getLastLocation().getLatitude(), locationResult.getLastLocation().getLongitude());
                if (shipperMarker == null) {
                    int height = 80;
                    int width = 80;
                    BitmapDrawable bitmapDrawable = (BitmapDrawable) ContextCompat.getDrawable(ShippingActivity.this, R.drawable.shipper);
                    Bitmap resized = Bitmap.createScaledBitmap(bitmapDrawable.getBitmap(), width, height, false);
                    shipperMarker = mMap.addMarker(new MarkerOptions()
                            .icon(BitmapDescriptorFactory.fromBitmap(resized))
                            .position(locationShipper)
                            .title("You"));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(locationShipper, 15));
                }

                if (isInit && previousLocation != null) {
                    String from = new StringBuilder()
                            .append(previousLocation.getLatitude())
                            .append(",")
                            .append(previousLocation.getLongitude())
                            .toString();

                    String to = new StringBuilder()
                            .append(locationShipper.latitude)
                            .append(",")
                            .append(locationShipper.longitude)
                            .toString();

                    moveMarkerAnimation(shipperMarker, from, to);
                }
                if (!isInit) {
                    isInit = true;
                    previousLocation = locationResult.getLastLocation();
                }
            }
        };
    }

    private void buildLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(15000); // 15 seconds
        locationRequest.setFastestInterval(10000); // 10 seconds
        locationRequest.setSmallestDisplacement(20f); // 20 meters
    }

    private void moveMarkerAnimation(Marker shipperMarker, String from, String to) {
        compositeDisposable.add(
                iGoogleAPI.getDirections(
                                "driving",
                                from,
                                to,
                                getString(R.string.google_maps_key),
                                "less_driving"
                        )
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                returnResult -> {
                                    try {
                                        JSONObject jsonObject = new JSONObject(returnResult);
                                        JSONArray jsonArray = jsonObject.getJSONArray("routes");

                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject routeObject = jsonArray.getJSONObject(i);
                                            JSONObject poly = routeObject.getJSONObject("overview_polyline");
                                            String polyline = poly.getString("points");
                                            polylineList = Common.decodePoly(polyline);

                                        }
                                        polylineOptions = new PolylineOptions();
                                        polylineOptions.color(Color.GRAY);
                                        polylineOptions.width(5);
                                        polylineOptions.startCap(new SquareCap());
                                        polylineOptions.jointType(JointType.ROUND);
                                        polylineOptions.addAll(polylineList);
                                        greyPoliline = mMap.addPolyline(polylineOptions);

                                        blackPolylineOptions = new PolylineOptions();
                                        blackPolylineOptions.color(Color.GRAY);
                                        blackPolylineOptions.width(5);
                                        blackPolylineOptions.startCap(new SquareCap());
                                        blackPolylineOptions.jointType(JointType.ROUND);
                                        blackPolylineOptions.addAll(polylineList);
                                        blackPolyline = mMap.addPolyline(blackPolylineOptions);

                                        //animator
                                        ValueAnimator polylineAnimator = ValueAnimator.ofInt(0, 100);
                                        polylineAnimator.setDuration(2000);
                                        polylineAnimator.setInterpolator(new LinearInterpolator());
                                        polylineAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                            @Override
                                            public void onAnimationUpdate(@NonNull ValueAnimator valueAnimator) {
                                                List<LatLng> points = greyPoliline.getPoints();
                                                int percentValue = (int) valueAnimator.getAnimatedValue();
                                                int size = points.size();
                                                int newPoints = (int) (size * (percentValue / 100.0f));
                                                List<LatLng> p = points.subList(0, newPoints);
                                                blackPolyline.setPoints(p);
                                            }
                                        });
                                        polylineAnimator.start();

                                        //BIKE MOVING
                                        handler = new Handler();
                                        index = -1;
                                        next = 1;
                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                if (index < polylineList.size() - 1) {
                                                    index++;
                                                    next = index + 1;
                                                    start = polylineList.get(next);
                                                }
                                                ValueAnimator valueAnimator = ValueAnimator.ofInt(0, 1);
                                                valueAnimator.setDuration(1500);
                                                valueAnimator.setInterpolator(new LinearInterpolator());
                                                valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                                    @Override
                                                    public void onAnimationUpdate(@NonNull ValueAnimator valueAnimator) {
                                                        v = valueAnimator.getAnimatedFraction();
                                                        lng = v * end.longitude + (1 - v) * start.longitude;
                                                        lat = v * end.latitude + (1 - v) * start.latitude;
                                                        LatLng newPos = new LatLng(lat, lng);
                                                        shipperMarker.setPosition(newPos);
                                                        shipperMarker.setAnchor(0.5f, 0.5f);
                                                        shipperMarker.setRotation(Common.getBearing(start, newPos));
                                                        mMap.animateCamera(CameraUpdateFactory.newLatLng(shipperMarker.getPosition()));
                                                    }
                                                });
                                                valueAnimator.start();
                                                if (index < polylineList.size() - 2) {
                                                    handler.postDelayed(this, 1500);
                                                }
                                            }

                                        }, 1500);
                                    } catch (JSONException e) {
                                        Toast.makeText(ShippingActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                },
                                throwable -> {
                                    if (throwable != null) {
                                        Toast.makeText(ShippingActivity.this, "" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                        )
        );
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
    }

    @Override
    protected void onDestroy() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
        compositeDisposable.clear();
        super.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == YOUR_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
            } else {
                Toast.makeText(this, "Quyền truy cập vị trí bị từ chối", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

  
