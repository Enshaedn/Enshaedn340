package com.enshaedn.ad340.enshaedn340k

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_zombie_list.*

class zombieList : AppCompatActivity() {
    private lateinit var zRecycler: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_zombie_list)
        /*
        val myDataset = Array<String>(3){"Hello"; "is this"; "working?"}

        viewManager = LinearLayoutManager(this)
        viewAdapter = MyAdapter(myDataset)

        zRecycler = findViewById<RecyclerView>(R.id.zombieRecycler).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter

        }*/
    }
}
/*
class MyAdapter(private val myDataset: Array<String>):
        RecyclerView.Adapter<MyAdapter.ViewHolder>() {

    class ViewHolder(val textView: TextView): RecyclerView.ViewHolder(textView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyAdapter.ViewHolder {
        val textView = LayoutInflater.from(parent.context).inflate(R.layout.my_text_view, parent, false) as TextView
        textView.width = ViewGroup.LayoutParams.MATCH_PARENT
        textView.height = ViewGroup.LayoutParams.MATCH_PARENT
        return ViewHolder(textView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textView.text = myDataset[position]
    }

    override fun getItemCount() = myDataset.size
}*/