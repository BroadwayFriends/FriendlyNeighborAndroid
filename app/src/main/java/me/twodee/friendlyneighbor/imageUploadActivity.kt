package me.twodee.friendlyneighbor

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import net.gotev.uploadservice.data.UploadInfo
import net.gotev.uploadservice.exceptions.UploadError
import net.gotev.uploadservice.exceptions.UserCancelledUploadException
import net.gotev.uploadservice.network.ServerResponse
import net.gotev.uploadservice.observer.request.RequestObserverDelegate
import net.gotev.uploadservice.protocols.multipart.MultipartUploadRequest
import org.json.JSONObject
import java.util.*


class imageUploadActivity : AppCompatActivity() {
    private var imageEncoded: String? = null

    companion object {
        // Every intent for result needs a unique ID in your app.
        // Choose the number which is good for you, here I'll use a random one.
        const val pickFileRequestCode = 69
        private const val TAG = "uploadData"
        const val baseUrl = "https://4112a99e.ngrok.io/api/requests"
        var image1: Uri = Uri.EMPTY
        var image2: Uri = Uri.EMPTY
        var image3: Uri = Uri.EMPTY
        private var context: Context? = null
        var PICK_IMAGE_MULTIPLE = 1
        lateinit var imagePath: String
        var imagesPathList: MutableList<String> = arrayListOf()

// https://ptsv2.com/t/bp3of-1589775457/post

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_post_requirement)
//        setContentView(R.layout.activity_main)

        val extras = intent.extras
        if (extras != null) {
            val reqTitle = extras.getString("title").toString()
            val description = extras.getString("description").toString()
            val phoneNumber = extras.getString("phoneNumber").toString()
            val radius = extras.getString("radius").toString()
            val expirationDate = extras.getString("expirationDate").toString()
            val finalPosition = extras.getString("finalPosition").toString()
            val priceQuote = extras.getString("priceQuote").toString()

            val imageUriArray = extras.getStringArrayList("imageUriArray")

            Log.v(TAG, "Size : ${imageUriArray}")
            if (imageUriArray != null) {
                upload(context = this, lifecycleOwner = this,reqTitle = reqTitle,description = description,phoneNumber = phoneNumber,radius = radius,finalPosition=finalPosition,expirationDate = expirationDate,priceQuote= priceQuote,imageUriArray = imageUriArray )
                Log.v(TAG, "title:${reqTitle},description:${description},finalPosition:${(finalPosition)}")
            }

        }
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)

    }




    private fun upload(context: Context, lifecycleOwner: LifecycleOwner,reqTitle: String, description :String , phoneNumber:String,radius:String,finalPosition:String,expirationDate:String, priceQuote:String,imageUriArray :ArrayList<String>) {
        val data = HashMap<String, String>()
        data["title"] = reqTitle
        data["description"] = description
        data["contactNumber"] = phoneNumber
        data["searchRadius"] = radius
        data["finalPosition"] = finalPosition
        data["expiration"] = expirationDate
        data["priceQuote"] = priceQuote
        data["requestedBy"] = "5ebc27d7e6fe7a77013ecd2a"

        var imageCount = imageUriArray?.size
        Log.v(TAG, "Size$imageCount")


        var reqObj = MultipartUploadRequest(this, baseUrl)
                .setMethod("POST")
                .addParameter("data", JSONObject(data as Map<*, *>).toString())
                .addHeader("_id","5ec166d582bc531c24eba282")

//
//                when (imageCount) {
//                        1 -> {
//                            image1 = Uri.parse(imageUriArray?.get(0))
//                            reqObj.addFileToUpload(filePath = image1.toString(), parameterName = "image1")
//                            Log.v(TAG,"Only 1 image selected")
//                        }
//                        2 -> {
//                            image1 = Uri.parse(imageUriArray?.get(0))
//                            image2 = Uri.parse(imageUriArray?.get(1))
//                            reqObj.addFileToUpload(filePath = image1.toString(), parameterName = "image1")
//                            reqObj.addFileToUpload(filePath = image2.toString(), parameterName = "image2")
//                            Log.v(TAG,"Only 2 images selected")
//                        }
//                        3 -> {
//                            image1 = Uri.parse(imageUriArray?.get(0))
//                            image2 = Uri.parse(imageUriArray?.get(1))
//                            image3 = Uri.parse(imageUriArray?.get(2))
//                            reqObj.addFileToUpload(filePath = image1.toString(), parameterName = "image1")
//                            reqObj.addFileToUpload(filePath = image2.toString(), parameterName = "image2")
//                            reqObj.addFileToUpload(filePath = image3.toString(), parameterName = "image3")
//                            Log.v(TAG,"All 3 selected")
//                        }
//                    else -> {
////
//                    }
//                 }
            .subscribe(context = context, lifecycleOwner = lifecycleOwner, delegate = object : RequestObserverDelegate {
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










    }


}


