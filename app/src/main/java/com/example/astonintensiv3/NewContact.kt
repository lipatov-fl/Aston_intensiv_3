package com.example.astonintensiv3

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.astonintensiv3.ContactConstants.id
import com.example.astonintensiv3.databinding.ActivityNewContactBinding

class NewContact : AppCompatActivity() {
    private lateinit var binding: ActivityNewContactBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewContactBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = getString(R.string.add_contact_title)
        }

        binding.addButton.setOnClickListener {
            val name = binding.editName.text.toString()
            val lastName = binding.editLastName.text.toString()
            val phone = binding.editTelephone.text.toString()

            if (name.isEmpty() || lastName.isEmpty() || phone.isEmpty()) {
                Toast.makeText(this, "Пожалуйста, заполните все поля", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val resultIntent = Intent().apply {
                putExtra(ContactConstants.NAME, name)
                putExtra(ContactConstants.LAST_NAME, lastName)
                putExtra(ContactConstants.PHONE, phone)
                putExtra(ContactConstants.ID, id)
            }
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    companion object {
        fun setId(value: Int) {
            id = value
        }
    }
}