package kr.ac.kumoh.s20181091.soccertermpr
import android.app.Application
import android.graphics.Bitmap
import android.widget.Toast
import androidx.collection.LruCache
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.ImageLoader
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONObject
import java.net.URLEncoder

class SoccerViewModel (application: Application) : AndroidViewModel(application) {
    data class Soccer (var id: Int, var name: String, var country: String, var height: Int, var image:String)

    companion object {
        const val QUEUE_TAG = "SoccerVolleyRequest"

        // NOTE: 서버 주소는 본인의 서버 IP 사용할 것
        const val SERVER_URL = "https://songdb-aomtk.run.goorm.io"
    }

    private val soccers = ArrayList<Soccer>()
    private val _list = MutableLiveData<ArrayList<Soccer>>()
    val list: LiveData<ArrayList<Soccer>>
        get() = _list

    private var queue: RequestQueue
    val imageLoader: ImageLoader
    init {
        _list.value = soccers
        queue = Volley.newRequestQueue(getApplication())
        imageLoader = ImageLoader(queue,
            object : ImageLoader.ImageCache {
                private val cache = LruCache<String, Bitmap>(100)
                override fun getBitmap(url: String): Bitmap? {
                    return cache.get(url)
                }
                override fun putBitmap(url: String, bitmap: Bitmap) {
                    cache.put(url, bitmap)
                }
            })
    }
    fun getImageUrl(i: Int): String = "$SERVER_URL/image/" + URLEncoder.encode(soccers[i].image, "utf-8")
    fun requestSoccer() {
        val request = JsonArrayRequest(
            Request.Method.GET,
            SERVER_URL,
            null,
            {
                //Toast.makeText(getApplication(), it.toString(), Toast.LENGTH_LONG).show()
                soccers.clear()
                parseJson(it)
                _list.value = soccers
            },
            {
                Toast.makeText(getApplication(), it.toString(), Toast.LENGTH_LONG).show()
            }
        )

        request.tag = QUEUE_TAG
        queue.add(request)
    }

    private fun parseJson(items: JSONArray) {
        for (i in 0 until items.length()) {
            val item: JSONObject = items[i] as JSONObject
            val id = item.getInt("id")
            val name = item.getString("name")
            val country = item.getString("country")
            val height = item.getInt("height")
            val image = item.getString("image")
            soccers.add(Soccer(id, name, country, height,image))
        }
    }

    override fun onCleared() {
        super.onCleared()
        queue.cancelAll(QUEUE_TAG)
    }
}