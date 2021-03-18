package us.aarishsidhu.happymaps.activities

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import pl.kitek.rvswipetodelete.SwipeToEditCallback
import us.aarishsidhu.happymaps.R
import us.aarishsidhu.happymaps.adapters.HappyPlacesAdapter
import us.aarishsidhu.happymaps.database.DatabaseHandler
import us.aarishsidhu.happymaps.models.HappyMapModel
import us.aarishsidhu.happymaps.utils.SwipeToDeleteCallback


class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fabAddHappyPlace.setOnClickListener{
            val intent = Intent(this@MainActivity, AddHappyMapActivity::class.java)

            startActivityForResult(intent, ADD_PLACE_ACTIVITY_REQUEST_CODE)
        }

        getHappyPlaceListFromLocalDB()
    }

    private fun getHappyPlaceListFromLocalDB(){
        val dbHandler = DatabaseHandler(this)
        val getHappyPlaceList: ArrayList<HappyMapModel> =  dbHandler.getHappyPlacesList()

        if(getHappyPlaceList.size > 0){
            rv_happy_places_list.visibility = View.VISIBLE
            tv_no_records_available.visibility = View.GONE
            setupHappyPlacesRecyclerView(getHappyPlaceList)
        }else {
            rv_happy_places_list.visibility = View.GONE
            tv_no_records_available.visibility = View.VISIBLE
        }

    }

    private fun setupHappyPlacesRecyclerView(happyPlaceList : ArrayList<HappyMapModel>){
        rv_happy_places_list.layoutManager = LinearLayoutManager(this)
        rv_happy_places_list.setHasFixedSize(true)

        val placesAdapter = HappyPlacesAdapter(this, happyPlaceList)
        rv_happy_places_list.adapter = placesAdapter

        placesAdapter.setOnClickListener(object: HappyPlacesAdapter.OnClickListener{
            override fun onClick(position: Int, model: HappyMapModel) {
                val intent = Intent(this@MainActivity, HappyMapDetailActivity::class.java)
                intent.putExtra(EXTRA_PLACE_DETAILS, model)
                startActivity(intent)
            }
        })

        val editSwipeHandler = object : SwipeToEditCallback(this){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = rv_happy_places_list.adapter as HappyPlacesAdapter
                adapter.notifyEditItem(this@MainActivity,viewHolder.adapterPosition, ADD_PLACE_ACTIVITY_REQUEST_CODE)
            }
        }

        val editItemTouchHelper = ItemTouchHelper(editSwipeHandler)
        editItemTouchHelper.attachToRecyclerView((rv_happy_places_list))

        val deleteSwipeHandler = object : SwipeToDeleteCallback(this){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = rv_happy_places_list.adapter as HappyPlacesAdapter
                adapter.removeAt(viewHolder.adapterPosition)
                getHappyPlaceListFromLocalDB()
            }
        }

        val deleteItemTouchHelper = ItemTouchHelper(deleteSwipeHandler)
        deleteItemTouchHelper.attachToRecyclerView((rv_happy_places_list))

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == ADD_PLACE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                getHappyPlaceListFromLocalDB()
            }else{
                Log.e("Activity", "Cancelled or Back Pressed")
            }
        }
    }
    companion object {
        private var ADD_PLACE_ACTIVITY_REQUEST_CODE = 1
        internal var EXTRA_PLACE_DETAILS = "extra_place_details"
    }
}