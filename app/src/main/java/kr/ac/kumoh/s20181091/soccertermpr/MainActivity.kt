package kr.ac.kumoh.s20181091.soccertermpr

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.toolbox.NetworkImageView
import kr.ac.kumoh.s20181091.soccertermpr.databinding.ActivityMainBinding
import kr.ac.kumoh.s20181091.soccertermpr.databinding.ActivitySoccerActivtyBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var model: SoccerViewModel
    private val soccerAdapter = SoccerAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        model = ViewModelProvider(this)[SoccerViewModel::class.java]

        binding.list.apply {
            layoutManager = LinearLayoutManager(applicationContext)
            setHasFixedSize(true)
            itemAnimator = DefaultItemAnimator()
            adapter = soccerAdapter
        }

        model.list.observe(this) {
            // 좀더 구체적인 이벤트를 사용하라고 warning 나와서 변경함
            //songAdapter.notifyDataSetChanged()
            //Log.i("size", "${model.list.value?.size ?: 0}")

            // Changed가 아니라 Inserted
            soccerAdapter.notifyItemRangeInserted(0,
                model.list.value?.size ?: 0)
        }

        model.requestSoccer()
    }
    inner class SoccerAdapter: RecyclerView.Adapter<SoccerAdapter.ViewHolder>() {
        inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView), OnClickListener{

            val txName: TextView = itemView.findViewById(R.id.text1)
            val txCountry: TextView = itemView.findViewById(R.id.text2)
            val niImage: NetworkImageView = itemView.findViewById<NetworkImageView>(R.id.image)

            init {
                niImage.setDefaultImageResId(android.R.drawable.ic_menu_report_image)
                itemView.setOnClickListener(this)
            }
            override fun onClick(p0: View?) {
                val intent = Intent(application, SoccerActivity::class.java)
                intent.putExtra(SoccerActivity.KEY_NAME,
                    model.list.value?.get(adapterPosition)?.name)
                intent.putExtra(SoccerActivity.KEY_COUNTRY,
                    model.list.value?.get(adapterPosition)?.country)
                intent.putExtra(SoccerActivity.KEY_HEIGHT,
                    model.list.value?.get(adapterPosition)?.height.toString())
                intent.putExtra(SoccerActivity.KEY_IMAGE,
                    model.getImageUrl(adapterPosition))
                startActivity(intent)
            }

        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            //val view = layoutInflater.inflate(android.R.layout.simple_list_item_2,
            val view = layoutInflater.inflate(R.layout.item_soccer,
                parent,
                false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.txName.text = model.list.value?.get(position)?.name
            holder.txCountry.text = model.list.value?.get(position)?.country
            holder.niImage.setImageUrl(model.getImageUrl(position), model.imageLoader)
        }

        override fun getItemCount() = model.list.value?.size ?: 0
    }
}