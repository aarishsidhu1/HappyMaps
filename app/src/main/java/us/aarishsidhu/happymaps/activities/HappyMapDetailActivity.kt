package us.aarishsidhu.happymaps.activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_happy_map_detail.*
import us.aarishsidhu.happymaps.R
import us.aarishsidhu.happymaps.models.HappyMapModel

class HappyMapDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_happy_map_detail)

        var happyPlaceDetailModel : HappyMapModel? = null

        if(intent.hasExtra(MainActivity.EXTRA_PLACE_DETAILS)){
            happyPlaceDetailModel = intent.getParcelableExtra(MainActivity.EXTRA_PLACE_DETAILS) as HappyMapModel
        }

        if(happyPlaceDetailModel != null){
            setSupportActionBar((toolbar_happy_map_detail))
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.title = happyPlaceDetailModel.title
            toolbar_happy_map_detail.setNavigationOnClickListener {
                onBackPressed()
            }

            iv_place_image.setImageURI(Uri.parse(happyPlaceDetailModel.image))
            tv_description.text = happyPlaceDetailModel.description
            tv_location.text = happyPlaceDetailModel.location

            btn_view_on_map.setOnClickListener{
                val intent = Intent(this@HappyMapDetailActivity, MapActivity::class.java)
                intent.putExtra((MainActivity.EXTRA_PLACE_DETAILS), happyPlaceDetailModel)
                startActivity(intent)
            }
        }
    }
}