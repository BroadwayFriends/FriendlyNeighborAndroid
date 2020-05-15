package me.twodee.friendlyneighbor

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import net.gotev.uploadservice.data.UploadInfo
import net.gotev.uploadservice.exceptions.UploadError
import net.gotev.uploadservice.exceptions.UserCancelledUploadException
import net.gotev.uploadservice.network.ServerResponse
import net.gotev.uploadservice.observer.request.RequestObserver
import net.gotev.uploadservice.observer.request.RequestObserverDelegate
import net.gotev.uploadservice.protocols.multipart.MultipartUploadRequest

class imageUploadActivity :AppCompatActivity() {
    companion object {
        // Every intent for result needs a unique ID in your app.
        // Choose the number which is good for you, here I'll use a random one.
        const val pickFileRequestCode = 69
        const val baseUrl = "https://1687298c.ngrok.io/api/users/5ebc27d7e6fe7a77013ecd2a"
        var filePath  = ""

//        https://ptsv2.com/t/ix2wo-1589549265/post
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_requirement)
        pickFile()

//        btnUploadImage.setOnClickListener {
//
//        }
    }

    // Pick a file with a content provider
    private fun pickFile() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            // Filter to only show results that can be "opened", such as files
            addCategory(Intent.CATEGORY_OPENABLE)
            // search for all documents available via installed storage providers
            type = "image/*"
        }
        startActivityForResult(intent, pickFileRequestCode)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        // The ACTION_OPEN_DOCUMENT intent was sent with the request code
        // READ_REQUEST_CODE. If the request code seen here doesn't match, it's the
        // response to some other intent, and the code below shouldn't run at all.
        if (requestCode == pickFileRequestCode && resultCode == Activity.RESULT_OK) {
            data?.let {
//                onFilePicked(it.data.toString())
                 filePath = it.data.toString()
                upload(context = this, lifecycleOwner = this)
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun onFilePicked(filePath: String) {
        MultipartUploadRequest(this, serverUrl = baseUrl)
                .setMethod("PATCH")
                .addFileToUpload(
                        filePath = filePath,
                        parameterName = "myFile"
                ).startUpload()
    }

    fun upload(context: Context, lifecycleOwner: LifecycleOwner) {
        MultipartUploadRequest(this, baseUrl)
                .setMethod("PATCH")
                .addFileToUpload(filePath = filePath, parameterName = "image")
                .subscribe(context = context, lifecycleOwner = lifecycleOwner, delegate = object : RequestObserverDelegate {
                    override fun onProgress(context: Context, uploadInfo: UploadInfo) {
                        // do your thing
                    }

                    override fun onSuccess(
                            context: Context,
                            uploadInfo: UploadInfo,
                            serverResponse: ServerResponse
                    ) {
                        // do your thing
                        Log.e("Response", "Success: ${serverResponse.bodyString}")
                    }

                    override fun onError(context: Context, uploadInfo: UploadInfo, exception: Throwable) {
                        when (exception) {
                            is UserCancelledUploadException -> {
                                Log.e("RECEIVER", "Error, user cancelled upload: $uploadInfo")
                            }

                            is UploadError -> {
                                Log.e("RECEIVER", "Error, upload error: ${exception.serverResponse}")
                            }

                            else -> {
                                Log.e("RECEIVER", "Error: $uploadInfo", exception)

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
