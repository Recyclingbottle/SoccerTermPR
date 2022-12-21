package kr.ac.kumoh.s20181091.soccertermpr

import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.collection.LruCache
import com.android.volley.toolbox.ImageLoader
import com.android.volley.toolbox.Volley
import kr.ac.kumoh.s20181091.soccertermpr.databinding.ActivitySoccerActivtyBinding

class SoccerActivity : AppCompatActivity() {
    companion object {
        const val KEY_NAME = "SoccerName"
        const val KEY_COUNTRY = "SoccerCountry"
        const val KEY_HEIGHT = "SoccerHeight"
        const val KEY_IMAGE = "SoccerImage"
    }
    private lateinit var binding: ActivitySoccerActivtyBinding
    private lateinit var imageLoader: ImageLoader

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySoccerActivtyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        imageLoader = ImageLoader(
            Volley.newRequestQueue(this),
            object : ImageLoader.ImageCache {
                private val cache = LruCache<String, Bitmap>(100)
                override fun getBitmap(url: String): Bitmap? {
                    return cache.get(url)
                }
                override fun putBitmap(url: String, bitmap: Bitmap) {
                    cache.put(url, bitmap)
                }
            })

        binding.imageSoccer.setImageUrl(intent.getStringExtra(KEY_IMAGE), imageLoader)
        binding.textName.text = intent.getStringExtra(KEY_NAME)
        binding.textCountry.text = "나라 : "+intent.getStringExtra(KEY_COUNTRY)
        binding.textHeight.text = " 키  :" +intent.getStringExtra(KEY_HEIGHT)
    }
}