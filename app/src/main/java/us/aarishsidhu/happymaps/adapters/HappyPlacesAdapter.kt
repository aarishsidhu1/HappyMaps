package us.aarishsidhu.happymaps.adapters

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_add_happy_map.view.*
import kotlinx.android.synthetic.main.item_happy_map.view.*
import us.aarishsidhu.happymaps.R
import us.aarishsidhu.happymaps.activities.AddHappyMapActivity
import us.aarishsidhu.happymaps.activities.MainActivity
import us.aarishsidhu.happymaps.database.DatabaseHandler
import us.aarishsidhu.happymaps.models.HappyMapModel


open class HappyPlacesAdapter(
    private val context: Context,
    private val list: ArrayList<HappyMapModel>

): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var onClickListener: OnClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return MyViewHolder(LayoutInflater.from(context)
            .inflate(R.layout.item_happy_map,parent,false)
        )

    }

    fun setOnClickListener(onClickListener: OnClickListener){
        this.onClickListener = onClickListener
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]

        if(holder is MyViewHolder){
            holder.itemView.iv_place_image.setImageURI((Uri.parse(model.image)))
            holder.itemView.tvTitle.text = model.title
            holder.itemView.tvDescription.text = model.description
            holder.itemView.setOnClickListener{
                if(onClickListener != null){
                    onClickListener!!.onClick(position, model)
                }
            }
        }
    }

    fun notifyEditItem(activity : Activity, position : Int, requestCode : Int){
        val intent = Intent(context, AddHappyMapActivity::class.java)
        intent.putExtra(MainActivity.EXTRA_PLACE_DETAILS, list[position])
        activity.startActivityForResult(intent, requestCode)
        notifyItemChanged(position)
    }

    fun removeAt(position: Int){
        val dbHandler = DatabaseHandler(context)
        val isDeleted = dbHandler.deleteHappyPlace(list[position])
        if(isDeleted > 0){
            list.removeAt(position)
            notifyItemRemoved(position)
        }
    }


    interface OnClickListener{
        fun onClick(position: Int, model: HappyMapModel)
    }
    override fun getItemCount(): Int {
        return list.size
    }

    private class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)

}