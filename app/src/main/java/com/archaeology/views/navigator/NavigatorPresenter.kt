package com.archaeology.views.navigator

/*
import android.content.Intent
import android.net.Uri
import com.archaeology.R
import com.archaeology.helpers.checkLocationPermissions
import com.archaeology.helpers.isPermissionGranted
import com.archaeology.models.SiteModel
import com.archaeology.views.BasePresenter
import com.archaeology.views.BaseView
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.maps.DirectionsApi
import com.google.maps.GeoApiContext
import com.google.maps.android.PolyUtil
import com.google.maps.model.DirectionsResult
import com.google.maps.model.DirectionsRoute
import com.google.maps.model.TravelMode
import org.jetbrains.anko.alert
import org.jetbrains.anko.toast
import java.io.IOException
import java.util.concurrent.TimeUnit


class NavigatorPresenter(view: BaseView) : BasePresenter(view) {

    private var locationService: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(view)
    private lateinit var myLocation: LatLng
    private val overview = 0

    private var site = SiteModel()
    private var siteLocation = LatLng(0.0, 0.0)

    init {
        if (checkLocationPermissions(view)) {
            doSetCurrentLocation()
        }
        if (view.intent.hasExtra("site")) {
            site = view.intent.extras?.getParcelable("site")!!
            siteLocation = LatLng(site.location.lat, site.location.lng)
        }
    }

    fun populateMap(map: GoogleMap, myLocation: LatLng, mode: TravelMode) {
        if (myLocation == siteLocation) {
            view?.alert("Site location is set to current location!")
                ?.show()
        } else {
            map.uiSettings.isZoomControlsEnabled = true
            val myLocationOptions = MarkerOptions().title("My Location").position(myLocation)
            map.addMarker(myLocationOptions).tag = "My Location"
            val directions = getDirectionsDetails(myLocation, siteLocation, mode)
            if (directions != null) {
                addPolyline(directions, map)
                positionCamera(directions.routes[overview], map)
                addMarkersToMap(directions, map)
            }
        }
    }

    override fun doRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (isPermissionGranted(requestCode, grantResults)) {
            getCurrentLocation()
        }
    }

    private fun doSetCurrentLocation() {
        locationService.lastLocation.addOnSuccessListener {
            myLocation = LatLng(it.latitude, it.longitude)
        }
    }

    fun getCurrentLocation(): LatLng {
        return myLocation
    }

    // below section taken from the following source (with changes made)
    // source: https://android.jlelse.eu/google-maps-directions-api-5b2e11dee9b0
    private fun getGeoContext(): GeoApiContext? {
        val geoApiContext = GeoApiContext()
        return geoApiContext.setQueryRateLimit(3)
            .setApiKey(view?.getString(R.string.google_maps_key))
            .setConnectTimeout(1, TimeUnit.SECONDS).setReadTimeout(1, TimeUnit.SECONDS)
            .setWriteTimeout(1, TimeUnit.SECONDS)
    }

    private fun getDirectionsDetails(
        origin: LatLng,
        destination: LatLng,
        mode: TravelMode
    ): DirectionsResult? {
        val originString = "${origin.latitude},${origin.longitude}"
        val destinationString = "${destination.latitude},${destination.longitude}"
        val now = DateTime()
        return try {
            DirectionsApi.newRequest(getGeoContext())
                .mode(mode)
                .origin(originString)
                .destination(destinationString)
                .departureTime(now)
                .await()
        } catch (e: ApiException) {
            view?.toast("API Error: ${e.message}")
            null
        } catch (e: InterruptedException) {
            view?.toast("Interrupt Error: ${e.message}")
            null
        } catch (e: IOException) {
            view?.toast("IO Error: ${e.message}")
            null
        }
    }

    private fun addMarkersToMap(results: DirectionsResult, mMap: GoogleMap) {
        mMap.addMarker(
            MarkerOptions().position(
                LatLng(
                    results.routes[overview].legs[overview].endLocation.lat,
                    results.routes[overview].legs[overview].endLocation.lng
                )
            ).title(site.name).snippet(
                getEndLocationTitle(results)
            )
        )
    }

    private fun positionCamera(route: DirectionsRoute, mMap: GoogleMap) {
        mMap.moveCamera(
            CameraUpdateFactory.newLatLngZoom(
                LatLng(
                    route.legs[overview].startLocation.lat,
                    route.legs[overview].startLocation.lng
                ), 12f
            )
        )
    }

    private fun addPolyline(results: DirectionsResult, mMap: GoogleMap) {
        val decodedPath: List<LatLng> =
            PolyUtil.decode(results.routes[overview].overviewPolyline.encodedPath)
        mMap.addPolyline(PolylineOptions().addAll(decodedPath))
    }

    private fun getEndLocationTitle(results: DirectionsResult): String? {
        return "Time: " + results.routes[overview].legs[overview].duration.humanReadable + " Distance: " + results.routes[overview].legs[overview].distance.humanReadable
    }

    fun doOpenMaps() {
        val intent =
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse("geo:${myLocation.latitude},${myLocation.longitude}?q=${siteLocation.latitude},${siteLocation.longitude}(Label+Name)")
            )
        view?.startActivity(intent)
    }
}
 */