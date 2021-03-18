package us.aarishsidhu.happymaps.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_map.*
import us.aarishsidhu.happymaps.R
import us.aarishsidhu.happymaps.models.HappyMapModel

class MapActivity : AppCompatActivity(), OnMapReadyCallback {

    private var mHappyPlaceDetail: HappyMapModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        if(intent.hasExtra(MainActivity.EXTRA_PLACE_DETAILS)){
            mHappyPlaceDetail = intent.getParcelableExtra(MainActivity.EXTRA_PLACE_DETAILS)
                    as HappyMapModel
        }

        if(mHappyPlaceDetail != null){
            setSupportActionBar(toolbar_map)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.title = mHappyPlaceDetail!!.title

            toolbar_map.setNavigationOnClickListener {
                onBackPressed()
            }

            val supportMapFragment : SupportMapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
            supportMapFragment.getMapAsync(this)
        }
    }

    override fun onMapReady(map: GoogleMap?) {
        val position = LatLng(mHappyPlaceDetail!!.latitude,mHappyPlaceDetail!!.longitude)
        map!!.addMarker(MarkerOptions().position(position).title(mHappyPlaceDetail!!.location))
        val newLatLngZoom = CameraUpdateFactory.newLatLngZoom(position, 10f)
        map.animateCamera(newLatLngZoom)
    }
}