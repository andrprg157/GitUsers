package com.git.gitusers.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.git.gitusers.R
import com.git.gitusers.model.Users
import com.git.gitusers.util.Constants
import kotlinx.android.synthetic.main.activity_detail.*

//import kotlinx.android.synthetic.main.activity_detail.*


class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val objct = intent.extras!!.get("objUser") as Users


        Glide.with(this)
            .load(objct.avatar_url)
            .apply(RequestOptions())
            .into(imgDisplay as ImageView?)
        tvName.text = "User Name : "+ objct.login
        tvType.text = "Type : "+ objct.type

        btnDeleteUser.setOnClickListener(View.OnClickListener {
            val intent = Intent()

            intent.putExtra(Constants.REQUEST_KEY,"FROM_DELETE")
            intent.putExtra("objUser_Del", objct)
            setResult(Activity.RESULT_OK, intent)
            finish()
        })
    }
}
