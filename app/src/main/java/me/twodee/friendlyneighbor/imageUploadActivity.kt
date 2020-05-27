package me.twodee.friendlyneighbor

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Resources
import android.net.Uri
import android.os.Bundle
import android.provider.Settings.Global.getString
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import net.gotev.uploadservice.data.UploadInfo
import net.gotev.uploadservice.exceptions.UploadError
import net.gotev.uploadservice.exceptions.UserCancelledUploadException
import net.gotev.uploadservice.network.ServerResponse
import net.gotev.uploadservice.observer.request.RequestObserverDelegate
import net.gotev.uploadservice.protocols.multipart.MultipartUploadRequest
import org.json.JSONException
import org.json.JSONObject
import java.util.*
import kotlin.collections.HashMap


class imageUploadActivity : AppCompatActivity() {
    private var imageEncoded: String? = null
    var preferences: SharedPreferences? = null
    companion object {
        // Every intent for result needs a unique ID in your app.
        // Choose the number which is good for you, here I'll use a random one.
        const val pickFileRequestCode = 69
        private const val TAG = "uploadData"
        const val baseUrl = "https://fn.twodee.me/api/requests"
//        const val baseUrl = "https://988d618d.ngrok.io/api/requests"
        var image1: Uri = Uri.EMPTY
        var image2: Uri = Uri.EMPTY
        var image3: Uri = Uri.EMPTY
        private var context: Context? = null
        var PICK_IMAGE_MULTIPLE = 1
        lateinit var imagePath: String
        var imagesPathList: MutableList<String> = arrayListOf()

// "https://ptsv2.com/t/pt68f-1589802042/post"
// "https://4112a99e.ngrok.io/api/requests"

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_post_requirement)
//        setContentView(R.layout.activity_main)
        preferences = getSharedPreferences("UserDetails", Context.MODE_PRIVATE)

        val extras = intent.extras
        if (extras != null) {
            val reqTitle = extras.getString("title").toString()
            val description = extras.getString("description").toString()
            val phoneNumber = extras.getString("phoneNumber").toString()
            val radius = extras.getString("radius").toString()
            val expirationDate = extras.getString("expirationDate").toString()
            val lat = extras.getString("lat").toString()
            val lng = extras.getString("lng").toString()
            val requestType = extras.getString("requestType").toString()
            val priceQuote = extras.getString("priceQuote").toString()

            val imageUriArray = extras.getStringArrayList("imageUriArray")

            Log.v(TAG, "Size : ${imageUriArray}")
            if (imageUriArray != null) {
                upload(context = this, lifecycleOwner = this,reqTitle = reqTitle,description = description,phoneNumber = phoneNumber,radius = radius,lat=lat,lng=lng,expirationDate = expirationDate,priceQuote= priceQuote,imageUriArray = imageUriArray ,requestType =requestType )
                Log.v(TAG, "title:${reqTitle},description:${description},finalPosition:${(lat)}")
            }
            else {uploadWithoutImages(reqTitle = reqTitle,description = description,phoneNumber = phoneNumber,radius = radius,lat=lat,lng=lng,expirationDate = expirationDate,priceQuote= priceQuote,requestType =requestType)}

        }
        val intent = Intent(this, DashboardActivity::class.java)
        startActivity(intent)

    }

private fun uploadWithoutImages(reqTitle: String, description :String , phoneNumber:String,radius:String,lat:String,lng:String,expirationDate:String,requestType:String, priceQuote:String){
    val data = HashMap<String, String>()
    val id = preferences?.getString("_id", null)
    val uid = id
    data["title"] = reqTitle
    data["description"] = description
    data["contactNumber"] = phoneNumber
    data["searchRadius"] = radius
    data["location"] = "{\"latitude\":$lat,\"longitude\":$lng}"
    data["expiration"] = expirationDate
    data["cost"] = priceQuote
    data["requestType"] = requestType
    data["image1"] = ""
    data["requestedBy"] = id.toString()



    val newRequestQueue = Volley.newRequestQueue(applicationContext)
    Log.v(TAG, "Works")
    val jsonObjectRequest: JsonObjectRequest = object : JsonObjectRequest(Method.POST, baseUrl, JSONObject(data as Map<*, *>),
            Response.Listener { response: JSONObject ->
                Log.v(TAG, response.toString())
                startActivity(Intent(this@imageUploadActivity, DashboardActivity::class.java))

            }, Response.ErrorListener { error: VolleyError? -> Log.w(TAG, error.toString()) }) {

        @Throws(AuthFailureError::class)
        override fun getHeaders(): HashMap<String, String>? {
            val headers:  HashMap<String, String> =  HashMap()
            headers["_id"] = id.toString()
            return headers
        }
    }

    newRequestQueue.add(jsonObjectRequest)





}






    private fun upload(context: Context, lifecycleOwner: LifecycleOwner,reqTitle: String, description :String , phoneNumber:String,radius:String,lat:String,lng:String,expirationDate:String,requestType:String, priceQuote:String,imageUriArray :ArrayList<String>) {

        preferences = getSharedPreferences("UserDetails", Context.MODE_PRIVATE)

        val data = HashMap<String, String>()
        val location = HashMap<String,String>()
        val id = preferences?.getString("_id", null)
        val uid = preferences?.getString("uid", null)
//        val id = "5ec7e77bb6bad31464e5ae9b"
//        data.put("requestedBy","5ebc27d7e6fe7a77013ecd2a")
//        data.put("title",reqTitle)
//        data.put("description",description)
//        data.put("contactNumber",phoneNumber)
//        data.put("searchRadius",radius)
//        data.put("finalPosition","{lat:$lat,lng:$lng}")
//        data.put("priceQuote",priceQuote)
//        data.put("expiration",expirationDate)


        data["title"] = reqTitle
        data["description"] = description
        data["contactNumber"] = phoneNumber
        data["searchRadius"] = radius
        data["location"] = "{\"latitude\":$lat,\"longitude\":$lng}"
        data["expiration"] = expirationDate
        data["cost"] = priceQuote
        data["requestType"] = requestType
//        data["requestedBy"] = "5ec7e77bb6bad31464e5ae9b"
        data["requestedBy"] = id.toString()
        val imageCount = imageUriArray.size
        Log.v(TAG, "Size$imageCount")


        val reqObj = MultipartUploadRequest(this, baseUrl)
                .setMethod("POST")
                .addParameter("data", JSONObject(data as Map<*, *>).toString())
                .addParameter("uid", uid.toString())
                .addHeader("_id", id.toString())


                when (imageCount) {
                        1 -> {
                            image1 = Uri.parse(imageUriArray.get(0))
                            reqObj.addFileToUpload(filePath = image1.toString(), parameterName = "image1")
                            Log.v(TAG,"Only 1 image selected")
                        }
                        2 -> {
                            image1 = Uri.parse(imageUriArray.get(0))
                            image2 = Uri.parse(imageUriArray.get(1))
                            reqObj.addFileToUpload(filePath = image1.toString(), parameterName = "image1")
                            reqObj.addFileToUpload(filePath = image2.toString(), parameterName = "image2")
                            Log.v(TAG,"Only 2 images selected")
                        }
                        3 -> {
                            image1 = Uri.parse(imageUriArray.get(0))
                            image2 = Uri.parse(imageUriArray.get(1))
                            image3 = Uri.parse(imageUriArray.get(2))
                            reqObj.addFileToUpload(filePath = image1.toString(), parameterName = "image1")
                            reqObj.addFileToUpload(filePath = image2.toString(), parameterName = "image2")
                            reqObj.addFileToUpload(filePath = image3.toString(), parameterName = "image3")
                            Log.v(TAG,"All 3 selected")
                        }
                    else -> {
//
                    }
                 }
            reqObj.subscribe(context = context, lifecycleOwner = lifecycleOwner, delegate = object : RequestObserverDelegate {
                            override fun onProgress(context: Context, uploadInfo: UploadInfo) {
                                Toast.makeText(applicationContext,"Uploading !!",Toast.LENGTH_SHORT)
                            }

                            override fun onSuccess(
                                    context: Context,
                                    uploadInfo: UploadInfo,
                                    serverResponse: ServerResponse
                            ) {
                                // do your thing
                                Toast.makeText(this@imageUploadActivity,"${serverResponse.bodyString}",Toast.LENGTH_SHORT).show();
                                Log.i(TAG, "Success: ${serverResponse.bodyString}")


                            }

                            override fun onError(context: Context, uploadInfo: UploadInfo, exception: Throwable) {
                                when (exception) {
                                    is UserCancelledUploadException -> {
                                        Log.e(TAG, "Error, user cancelled upload: $uploadInfo")
                                    }

                                    is UploadError -> {
                                        Log.e(TAG, "Error, upload error: ${exception.serverResponse.bodyString}")
                                    }

                                    else -> {
                                        Log.e(TAG, "Error: $uploadInfo", exception)

                                    }
                                }
                            }

                            override fun onCompleted(context: Context, uploadInfo: UploadInfo) {
                                Toast.makeText(this@imageUploadActivity,"Successfully posted your request!",Toast.LENGTH_SHORT).show();


                            }

                            override fun onCompletedWhileNotObserving() {

                                // do your thing
                            }
                        })
                reqObj.setUploadID("request")









    }
}





