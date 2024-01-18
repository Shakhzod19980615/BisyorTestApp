package com.example.testapp.presentation.announcementDetail.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.example.testapp.R

class ImagePagerAdapter(private val context :Context, private val imageList : List<String>):PagerAdapter() {
    override fun getCount(): Int {
        return imageList.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val inflater = LayoutInflater.from(context)
        val itemView = inflater.inflate(R.layout.item_image, container, false)
        val imageView: ImageView = itemView.findViewById(R.id.image)

        // Load image into ImageView using Glide or your preferred image loading library
        Glide.with(context)
            .load(imageList[position])
            .into(imageView)

        container.addView(itemView)
        return itemView
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}