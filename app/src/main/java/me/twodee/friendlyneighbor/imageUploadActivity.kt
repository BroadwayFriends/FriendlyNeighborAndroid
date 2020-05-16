package me.twodee.friendlyneighbor

import android.content.ClipDescription
import android.content.Context
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
import kotlin.collections.ArrayList


class imageUploadActivity : AppCompatActivity() {
    private var imageEncoded: String? = null

    companion object {
        // Every intent for result needs a unique ID in your app.
        // Choose the number which is good for you, here I'll use a random one.
        const val pickFileRequestCode = 69
        private const val TAG = "uploadData"
        const val baseUrl = "https://670934a3.ngrok.io/api/requests"
        var filePath = ""
        private var context: Context? = null
        var PICK_IMAGE_MULTIPLE = 1
        lateinit var imagePath: String
        var imagesPathList: MutableList<String> = arrayListOf()


//        "https://1687298c.ngrok.io/api/users/5ebc27d7e6fe7a77013ecd2a"
//https://ptsv2.com/t/7kkz7-1589603943/post
//        https://ptsv2.com/t/ix2wo-1589549265/post
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_requirement)

        val extras = intent.extras
        if (extras != null) {
            val reqTitle = extras.getString("title").toString()
            val description = extras.getString("description").toString()
            val phoneNumber = extras.getString("phoneNumber").toString()
            val distance = extras.getString("distance").toString()
            val expirationDate = extras.getString("expirationDate").toString()
            val temp = extras.getStringArrayList("imageUriArray")
            val image1 = Uri.parse(temp?.get(0))
            val image2 = Uri.parse(temp?.get(1))
            val image3 = Uri.parse(temp?.get(2))

            upload(context = this, lifecycleOwner = this,reqTitle = reqTitle,description = description,phoneNumber = phoneNumber,distance = distance,expirationDate = expirationDate,image1 =image1,image2=image2,image3 = image3)
            Log.v(TAG, "title:${reqTitle},description:${description},imageStringPaths:${(image1)}")
        }
//        pickFile()
//        btnUploadImage.setOnClickListener {
//
//        }
    }
//  *************************************************************************************************
//    // Pick a file with a content provider
//    private fun pickFile() {
//        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
//            // Filter to only show results that can be "opened", such as files
//            addCategory(Intent.CATEGORY_OPENABLE)
//            // search for all documents available via installed storage providers
//            type = "image/*"
//        }
//        startActivityForResult(intent, pickFileRequestCode)
//
//    }
//
//
//
//        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        // The ACTION_OPEN_DOCUMENT intent was sent with the request code
//        // READ_REQUEST_CODE. If the request code seen here doesn't match, it's the
//        // response to some other intent, and the code below shouldn't run at all.
//        if (requestCode == pickFileRequestCode && resultCode == Activity.RESULT_OK) {
//            data?.let {
////                onFilePicked(it.data.toString())
//                 filePath = it.data.toString()
//                Log.v(TAG,"Sent Image${filePath}")
//                upload(context = this, lifecycleOwner = this,newfilePath = filePath)
//
//            }
//        } else {
//            super.onActivityResult(requestCode, resultCode, data)
//        }
//    }


//    private fun onFilePicked(filePath: String) {
//        MultipartUploadRequest(this, serverUrl = baseUrl)
//                .setMethod("PATCH")
//                .addFileToUpload(
//                        filePath = filePath,
//                        parameterName = "image"
//                ).startUpload()
//    }
//  *************************************************************************************************

//    private fun pickFile() {
//        if (Build.VERSION.SDK_INT < 19) {
//            var intent = Intent()
//            intent.type = "image/*"
//            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
//            intent.action = Intent.ACTION_GET_CONTENT
//            startActivityForResult(
//                    Intent.createChooser(intent, "Select Picture")
//                    , PICK_IMAGE_MULTIPLE
//            )
//        } else {
//            var intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
//            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
//            intent.addCategory(Intent.CATEGORY_OPENABLE)
//            intent.type = "image/*"
//            startActivityForResult(intent, PICK_IMAGE_MULTIPLE);
//        }
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        try {
//            // When an Image is picked
//            if (requestCode == PICK_IMAGE_MULTIPLE && resultCode == Activity.RESULT_OK && null != data) {
//                // Get the Image from data
//                val filePathColumn = arrayOf(MediaStore.Images.Media._ID)
//                var imagesEncodedList = ArrayList<String>()
//                if (data.data != null) {
//                    val mImageUri = data.data
//
//                    // Get the cursor
//                    val cursor: Cursor? = contentResolver.query(mImageUri!!,
//                            filePathColumn, null, null, null)
//                    // Move to first row
//                    cursor?.moveToFirst()
//                    val columnIndex: Int? = cursor?.getColumnIndex(filePathColumn[0])
//                    imageEncoded  = columnIndex?.let { cursor?.getString(it) }
//                    cursor?.close()
//                } else {
//                    if (data.clipData != null) {
//                        val mClipData = data.clipData
//                        val mArrayUri = ArrayList<Uri>()
//                        for (i in 0 until mClipData!!.itemCount) {
//                            val item = mClipData.getItemAt(i)
//                            val uri = item.uri
//                            mArrayUri.add(uri)
//                            // Get the cursor
//                            val cursor: Cursor? = contentResolver.query(uri, filePathColumn, null, null, null)
//                            // Move to first row
//                            cursor?.moveToFirst()
//                            val columnIndex: Int? = cursor?.getColumnIndex(filePathColumn[0])
//                            imageEncoded  = columnIndex?.let { cursor?.getString(it) }
//                            imagesEncodedList.add(imageEncoded.toString())
//                            cursor?.close()
//                        }
//                        Log.v(TAG, "Selected Images$mArrayUri")
//                        if (mArrayUri.size <= 3)
//                        {for ((i, item) in mArrayUri.withIndex()) {
//                            Log.v(TAG,"Sent Image${item.toString()} with number $i")
//                            upload(context = this, lifecycleOwner = this, newfilePath = item.toString(),fileCount = i.toString() )
//                        }
//                            Toast.makeText(this,"Images Uploaded Successfully ",Toast.LENGTH_SHORT).show()
//                        }
//                        else {
//                            Toast.makeText(this,"Only 3 images can uploaded, Please try again",Toast.LENGTH_SHORT).show()
//                        }
////                        for (item in mArrayUri)
////                        {   Log.v(TAG,"Sent Image${item.toString()}")
////
////
////                        }
//                    }
//                }
//            } else {
//                Toast.makeText(this, "You haven't picked Image",
//                        Toast.LENGTH_LONG).show()
//            }
//        } catch (e: Exception) {
//            Toast.makeText(this, "Something went wrong,Please try to upload again", Toast.LENGTH_LONG)
//                    .show()
//        }
//        super.onActivityResult(requestCode, resultCode, data)
//    }
//




    private fun upload(context: Context, lifecycleOwner: LifecycleOwner,reqTitle: String, description :String , phoneNumber:String,distance:String,expirationDate:String,image1 :Uri,image2 :Uri,image3 :Uri) {
        val data = HashMap<String, String>()
        data.put("title",reqTitle)
        data.put("description",description)
        data.put("contactNumber", phoneNumber)
        data.put("searchRadius", distance)
        data.put("expiration", expirationDate)
        data.put("requestedBy","5ebc27d7e6fe7a77013ecd2a")

        MultipartUploadRequest(this, baseUrl)
                .setMethod("POST")
                .addParameter("data", JSONObject(data as Map<*, *>).toString())
                .addFileToUpload(filePath = image1.toString(), parameterName = "image1")
                .addFileToUpload(filePath = image2.toString(), parameterName = "image2")
                .addFileToUpload(filePath = image3.toString(), parameterName = "image3")

                .subscribe(context = context, lifecycleOwner = lifecycleOwner, delegate = object : RequestObserverDelegate {
                    override fun onProgress(context: Context, uploadInfo: UploadInfo) {
                        Toast.makeText(applicationContext,"Rukjaa kutte! We're creating a req.",Toast.LENGTH_SHORT)
                    }

                    override fun onSuccess(
                            context: Context,
                            uploadInfo: UploadInfo,
                            serverResponse: ServerResponse
                    ) {
                        // do your thing
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
                        // do your thing


                    }

                    override fun onCompletedWhileNotObserving() {

                        // do your thing
                    }
                })



    }


}


