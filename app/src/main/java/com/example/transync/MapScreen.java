package com.example.transync;

import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import androidx.appcompat.app.AppCompatActivity;

/*
 *  The class for displaying the google map that contains the bus routes
 *  of indy. Uses the google maps api and needs the correct api key to work.
 */

public class MapScreen extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnPolylineClickListener, GoogleMap.OnPolygonClickListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.route_map);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

    } /* onCreate() */


    /*
     *  Map Function that describes all bus routes that exist in indy,
     *  and set the clickListeners for all of the routes.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {

        /*
         * Polylines and polygon will eventually be implemented here,
         * not feasible due to time for this challenge however.
         */
        Polyline polyline1 = googleMap.addPolyline(new PolylineOptions()
                .clickable(true)
                .add(
                        new LatLng(-35.016, 143.321),
                        new LatLng(-34.747, 145.592),
                        new LatLng(-34.364, 147.891),
                        new LatLng(-33.501, 150.217),
                        new LatLng(-32.306, 149.248),
                        new LatLng(-32.491, 147.309)));

        /* Set the camera to indy and then zoom in */
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(39.768506, -86.158035), 4));
        googleMap.animateCamera( CameraUpdateFactory.zoomTo( 17.0f ) );

        /* Set listeners for click events. */
        googleMap.setOnPolylineClickListener(this);
        googleMap.setOnPolygonClickListener(this);

    } /* onMapReady() */

    /*
     * The following two classes are planned for implementing what happens
     * when a stop or route gets clicked on in the map.
     */
    @Override
    public void onPolygonClick(Polygon polygon) {

    } /* onPolygonClick() */
    @Override
    public void onPolylineClick(Polyline polyline) {

    } /* onPolyLineClick() */


} /* MapScreen Class */
