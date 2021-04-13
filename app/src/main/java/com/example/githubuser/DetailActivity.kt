package com.example.githubuser

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.githubuser.adapterdata.Data
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        val keyData = intent.getParcelableExtra(EXTRA_MYDATA) as Data
        supportActionBar?.title = getString(R.string.detailuser)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        detailname.text = keyData.name.toString()
        username_detail.text = keyData.name.toString()
        location.text = keyData.location.toString()
        company.text = keyData.company.toString()
        repository_.text = keyData.repository.toString()
        Glide.with(this)
            .load(keyData.avatar)
            .apply(RequestOptions().override(300,300))
            .into(circleImageView)
    }
    companion object{
        const val EXTRA_MYDATA = "extra_mydata"
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}