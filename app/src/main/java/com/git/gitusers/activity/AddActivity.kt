package com.git.gitusers.activity

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.git.gitusers.R
import com.git.gitusers.util.Constants.Companion.REQUEST_KEY
import com.git.gitusers.util.ManagePermissions
import com.git.gitusers.util.showToast
import kotlinx.android.synthetic.main.activity_add.*


class AddActivity : AppCompatActivity() {


    private val PermissionsRequestCode = 123
    private lateinit var managePermissions: ManagePermissions

    lateinit var mUri: String
    val REQUESTCODE_CAM = 200 //MAKE THIS CENTRAL FILE


    private var imageUri: Uri? = null

     lateinit var vImg: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)

        // Initialize a list of required permissions to request runtime
        val list = listOf<String>(
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )

        // Initialize a new instance of ManagePermissions class
        managePermissions = ManagePermissions(this,list,PermissionsRequestCode)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            managePermissions.checkPermissions()


        setupPermissions()

        imgAdd.setOnClickListener {
            startCam()
        }

        btnAddUser.setOnClickListener(View.OnClickListener {

            getUser()
        })
    }


    private fun setupPermissions() {
        val permission = ContextCompat.checkSelfPermission(this,
            Manifest.permission.CAMERA)

        if (permission != PackageManager.PERMISSION_GRANTED) {
            Log.i("http", "Permission to record denied")
            showToast("No Camera Permission!!!!")
        }
    }

    private fun startCam() {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "New Picture")
        values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera")
        imageUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        val imgU = imageUri.toString()
        vImg = imgU.replace("\\D+".toRegex(), "")
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
        startActivityForResult(intent, REQUESTCODE_CAM)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        imgAdd.setImageURI(imageUri)
    }

    fun openCamera() {
        val callCameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (callCameraIntent.resolveActivity(packageManager) != null) {
            startActivityForResult(callCameraIntent, REQUESTCODE_CAM)
        }
    }

    fun getUser() {

        val username: String = edtAddname.text.toString()
        val usertype: String = edtAddType.text.toString()

        if (username.trim().length > 0 && usertype.trim().length > 0) {
            mUri = imageUri.toString()
            val intent = Intent()
            intent.putExtra(REQUEST_KEY,"FROM_ADD")
            intent.putExtra("name", username)
            intent.putExtra("type", usertype)
            intent.putExtra("uri", mUri)
            setResult(Activity.RESULT_OK, intent)
            finish()
        } else {
            showToast("Fill All Details")
        }


    }
}
