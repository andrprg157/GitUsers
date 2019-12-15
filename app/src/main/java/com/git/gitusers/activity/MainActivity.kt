package com.git.gitusers.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.git.gitusers.R
import com.git.gitusers.adapter.ListAdapter
import com.git.gitusers.model.Users
import com.git.gitusers.rest.APIService
import com.git.gitusers.rest.RestClient
import com.git.gitusers.util.Constants
import com.git.gitusers.util.Constants.Companion.REQUESTCODE_DATA
import com.git.gitusers.util.Constants.Companion.REQUEST_KEY
import com.git.gitusers.util.showToast
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

import com.google.android.material.floatingactionbutton.FloatingActionButton


class MainActivity : AppCompatActivity() {

    private var mApiService: APIService? = null

    private var mAdapter: ListAdapter?= null;
    private var mQuestions: MutableList<Users> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mApiService = RestClient.client.create(APIService::class.java)
        listRecyclerView!!.layoutManager = LinearLayoutManager(this)
        mAdapter = ListAdapter(this, mQuestions, R.layout.question_item, { partItem : Users -> partItemClicked(partItem) })
        listRecyclerView!!.adapter = mAdapter

        if (verifyAvailableNetwork(this))
        {
            fetchUsers()
        }
        else
        {

            showHide(prgBar)
            showToast("No Internet Connection!!!")
        }
        val mFab = findViewById<FloatingActionButton>(R.id.fab)
        mFab.setOnClickListener {
            startActivityForResult(Intent(this,AddActivity::class.java),REQUESTCODE_DATA)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


        if (data != null && data!!.hasExtra(REQUEST_KEY))
        {
            val key = data!!.getStringExtra(REQUEST_KEY)
            if (key.equals("FROM_ADD"))
            {
                if (REQUESTCODE_DATA == requestCode && resultCode == Activity.RESULT_OK)
                {
                    //  showToast("INSIDE FROM ADD")
                    val name = data!!.getStringExtra("name")
                    val type = data!!.getStringExtra("type")
                    val imguri = data!!.getStringExtra("uri")

                    mQuestions.add(0, Users(name,imguri,type))
                    mAdapter?.notifyDataSetChanged()
                }


            }
            else if (key.equals("FROM_DELETE"))
            {
                //   showToast("FROM_DELETE")
                val delObj = data.extras!!.get("objUser_Del") as Users
                mQuestions.remove(delObj)
                mAdapter?.notifyDataSetChanged()
            }
            else{
                showToast("in Else")
            }
        }


    }
    fun showHide(view:View) {
        view.visibility = if (view.visibility == View.VISIBLE){
            View.INVISIBLE
        } else{
            View.VISIBLE
        }
    }
    private fun fetchUsers() {
        val call = mApiService!!.fetchQuestions();


        call.enqueue(object : Callback<List<Users>> {

            override fun onResponse(call: Call<List<Users>>, response: Response<List<Users>>) {

                Log.d(TAG, "Total Questions: " + response.body()!!.size)
                val questions = response.body()
               // prgBar.visibilit
                showHide(prgBar)
                if (questions != null) {
                    mQuestions.addAll(questions!!)
                    mAdapter!!.notifyDataSetChanged()
                }
            }

            override fun onFailure(call: Call<List<Users>>, t: Throwable) {
                showHide(prgBar)
                Log.e(TAG, "Got error : " + t.localizedMessage)
                showToast(t.message.toString())
            }
        })
    }
    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }

    private fun partItemClicked(partItem : Users) {
        val intentDetail = Intent(this,DetailActivity::class.java)
        intentDetail.putExtra("objUser",partItem)
        startActivityForResult(intentDetail, Constants.REQUESTCODE_DELETE)
    }


    fun verifyAvailableNetwork(activity:AppCompatActivity):Boolean{
        val connectivityManager=activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo=connectivityManager.activeNetworkInfo
        return  networkInfo!=null && networkInfo.isConnected
    }


}
