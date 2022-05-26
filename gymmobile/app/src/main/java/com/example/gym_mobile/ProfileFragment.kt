package com.example.gym_mobile

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.example.gym_mobile.Dto.GetFilePathDto
import com.example.gym_mobile.Dto.SavePathDto
import com.example.gym_mobile.Entities.Exercise
import com.example.gym_mobile.Model.User
import com.example.gym_mobile.Repository.ApiConnector
import com.example.gym_mobile.Repository.ExercisesRepo
import com.example.gym_mobile.Repository.FriendsRepo
import com.example.gym_mobile.Repository.UserRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class ProfileFragment : Fragment() {

    val TAG = "xyz"
    private val PERMISSION_REQUEST_CODE = 1
    val CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE_BY_FILE = 101
    val CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE_BY_BITMAP = 102
    var mFile: File? = null
    lateinit var activity: MainActivity
    private lateinit var binding: ProfileFragment
    lateinit var userRepo: UserRepo
    lateinit var profilePicture: ImageView

    val btnOpenCamera = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_profile, container, false)

        this.activity = context as MainActivity
        checkPermissions()
        val btnOpenCamera : Button = root.findViewById<Button>(R.id.btnOpenCamera)
        btnOpenCamera.setOnClickListener{
            onTakeByFile()
        }
        profilePicture = root.findViewById<ImageView>(R.id.profilePicture)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val email: String? = User.getUserEmail()
        userRepo = ApiConnector.getInstance().create(UserRepo::class.java)
        userRepo.getFilePath(email).enqueue(object:
            Callback<GetFilePathDto> {
            override fun onResponse(
                call: Call<GetFilePathDto>,
                response: Response<GetFilePathDto>
            ) {
                val dto : GetFilePathDto? = response.body()
                val myF: File = File(dto?.path ?: "failed to fetch the file")
                if (dto != null) {
                    Log.d("TESTING ", dto.path)
                }
                profilePicture.setImageURI(Uri.fromFile(myF))
            }
            override fun onFailure(call: Call<GetFilePathDto>, t: Throwable) {
            }
            })
    }

    fun onTakeByFile() {
        checkPermissions()
        mFile = getOutputMediaFile("Camera01") // create a file to save the image

        if (mFile == null) {
            Toast.makeText(activity, "Could not create file...", Toast.LENGTH_LONG).show()
            return
        }

        // create Intent to take a picture
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        // Add extra to inform the app where to put the image.
        val applicationId = "com.example.gym_mobile"
        intent.putExtra(
            MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(
            activity,
            "${applicationId}.provider",  //use your app signature + ".provider"
            mFile!!))

        startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE_BY_FILE)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        println(data)
        val profilePicture = activity.findViewById<ImageView>(R.id.profilePicture)
        val tvImageInfo = activity.findViewById<TextView>(R.id.imageInfo)
        when (requestCode) {

            CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE_BY_FILE ->
                if (resultCode == AppCompatActivity.RESULT_OK)
                    showImageFromFile(profilePicture, tvImageInfo, mFile!!)
                else handleOther(resultCode)

        }
    }

    private fun handleOther(resultCode: Int) {
        if (resultCode == AppCompatActivity.RESULT_CANCELED)
            println("Result Cancelled:  "+ resultCode )
        else println("In activity failure")
    }

    private fun showImageFromFile(img: ImageView, txt: TextView, f: File) {
        img.setImageURI(Uri.fromFile(f))

        //Todo for output
        //mImageView.setImageBitmap(bitmap)

        val dto: SavePathDto? = User.getUserEmail()?.let { SavePathDto(it, f.toString()) }
        CoroutineScope(Dispatchers.IO).launch {
            userRepo.uploadPath(dto)
        }

        val filePath: String = f.getPath()
        val bitmap = BitmapFactory.decodeFile(filePath)

        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream)
        val imageBytes = stream.toByteArray()

        //api here
        txt.text = "File at:" + f.absolutePath + " - size = " + f.length()
    }

    private fun getOutputMediaFile(folder: String): File? {
        // in an emulated device you can see the external files in /sdcard/Android/data/<your app>.
        val mediaStorageDir = File(activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES), folder)
        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(TAG, "failed to create directory")
                return null
            }
        }

        // Create a media file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val postfix = "jpg"
        val prefix = "IMG"
        return File(mediaStorageDir.path +
                File.separator + prefix +
                "_" + timeStamp + "." + postfix)
    }

    fun onTakeByBitmap(view: View) {
        checkPermissions()
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE_BY_BITMAP)

    }

    private fun checkPermissions() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return
        val permissions = mutableListOf<String>()
        if ( ! isGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE) ) permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if ( ! isGranted(Manifest.permission.CAMERA) ) permissions.add(Manifest.permission.CAMERA)
        if (permissions.size > 0)
            ActivityCompat.requestPermissions(activity, permissions.toTypedArray(), PERMISSION_REQUEST_CODE)
    }

    private fun isGranted(permission: String): Boolean =
        ActivityCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED
}