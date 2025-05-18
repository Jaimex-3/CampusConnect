package com.example.campusconnect.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ExpandableListView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.campusconnect.R
import com.example.campusconnect.adapter.FaqAdapter
import com.example.campusconnect.model.ResourceItem
import com.example.campusconnect.network.RetrofitClient
import kotlinx.coroutines.launch

class ResourcesFaqActivity : AppCompatActivity() {

    private lateinit var faqExpandableListView: ExpandableListView
    private lateinit var resourcesContainer: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resources_faq)

        faqExpandableListView = findViewById(R.id.faqExpandableListView)
        resourcesContainer = findViewById(R.id.resourcesContainer)

        loadFaqs()
        loadResources()
    }

    private fun loadFaqs() {
        lifecycleScope.launch {
            try {
                val service = RetrofitClient.getClient(this@ResourcesFaqActivity)
                val response = service.getFaqs()
                val faqs = response.faqs

                val titles = faqs.map { it.question }
                val content = HashMap<String, List<String>>()
                faqs.forEach { content[it.question] = listOf(it.answer) }

                val adapter = FaqAdapter(this@ResourcesFaqActivity, titles, content)
                faqExpandableListView.setAdapter(adapter)

            } catch (e: Exception) {
                Toast.makeText(this@ResourcesFaqActivity, "Failed to load FAQs", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadResources() {
        lifecycleScope.launch {
            try {
                val service = RetrofitClient.getClient(this@ResourcesFaqActivity)
                val response = service.getResources()
                val resources = response.resources

                for (resource in resources) {
                    addResourceButton(resource)
                }
            } catch (e: Exception) {
                Toast.makeText(this@ResourcesFaqActivity, "Failed to load resources", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun addResourceButton(resource: ResourceItem) {
        val button = Button(this)
        button.text = resource.title
        button.setBackgroundResource(R.color.primaryColor)
        button.setTextColor(getColor(R.color.buttonText))
        button.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(resource.url))
            startActivity(intent)
        }
        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            setMargins(0, 0, 0, 16)
        }
        button.layoutParams = layoutParams
        resourcesContainer.addView(button)
    }
}
