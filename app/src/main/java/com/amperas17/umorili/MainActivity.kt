package com.amperas17.umorili

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.find
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

class MainActivity : AppCompatActivity() {

    var postAdapter = PostsAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        postsRecyclerView.adapter = postAdapter
        postsRecyclerView.layoutManager = LinearLayoutManager(this)

        loadData()

    }

    fun loadData() {
        val retrofit = Retrofit.Builder()
                .baseUrl("http://www.umori.li/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        val umoriliApi: UmoriliApi = retrofit.create(UmoriliApi::class.java)

        umoriliApi.getData("ideer", 5).enqueue(object : Callback<List<PostModel>> {
            override fun onFailure(call: Call<List<PostModel>>, t: Throwable) {
                Log.d("vova", t.toString())
            }

            override fun onResponse(call: Call<List<PostModel>>, response: Response<List<PostModel>>) {
                if (response.body().isNotEmpty()) {
                    postAdapter.data.clear()
                    postAdapter.data.addAll(response.body())
                    postAdapter.notifyDataSetChanged()
                }
            }

        })


    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val tvDesc: TextView = view.find(R.id.tvDesc)
        val tvElementPureHtml: TextView = view.find(R.id.tvElementPureHtml)
        val tvSite: TextView = view.find(R.id.tvSite)

        fun bind(item: PostModel) {
            tvDesc.text = item.desc
            tvElementPureHtml.text = Html.fromHtml(item.elementPureHtml)
            tvSite.text = item.site
        }

    }

    inner class PostsAdapter : RecyclerView.Adapter<ViewHolder>() {

        val data = ArrayList<PostModel>()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(this@MainActivity).inflate(R.layout.item_post, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = data[position]
            holder.bind(item)
        }

        override fun getItemCount() = data.size
    }

}
