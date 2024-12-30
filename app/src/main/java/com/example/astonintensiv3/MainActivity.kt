package com.example.astonintensiv3

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DiffUtil
import com.example.astonintensiv3.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var contacts = contactList()

    private val contactsAdapter = ContactAdapter(
        contacts,
        onClickContact = { position -> onClickContact(position) },
    )

    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                val name = data?.getStringExtra(ContactConstants.NAME) ?: ""
                val lastName = data?.getStringExtra(ContactConstants.LAST_NAME) ?: ""
                val phone = data?.getStringExtra(ContactConstants.PHONE) ?: ""
                val id = data?.getIntExtra(ContactConstants.ID, -1) ?: -1

                val newContact = Contact(id, name, lastName, phone)

                val newContactList = contacts.toMutableList()

                if (id > 0 && id <= contacts.size) {
                    val existingContact = newContactList.find { it.id == id }
                    existingContact?.let {
                        val index = newContactList.indexOf(it)
                        newContactList[index] = newContact
                    }
                    updateList(newContactList)
                } else if (id > contacts.size) {
                    updateList(contacts.plus(newContact))
                }

            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {
            recyclerView.adapter = contactsAdapter

            addButton.setOnClickListener {
                NewContact.setId(contacts.size + 1)
                openNewContactActivity()
            }

            deleteButton.setOnClickListener {
                supportActionBar?.setDisplayHomeAsUpEnabled(false)
                updateList(contactsAdapter.deletedContactList())
                updateUi(isClickedTrash = false)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_delete -> {
                supportActionBar?.setDisplayHomeAsUpEnabled(true)
                updateUi(isClickedTrash = true)
                true
            }

            android.R.id.home -> {
                supportActionBar?.setDisplayHomeAsUpEnabled(false)
                updateUi(isClickedTrash = false)
                false
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    private fun openNewContactActivity() {
        val intent = Intent(this, NewContact::class.java)
        resultLauncher.launch(intent)
    }

    private fun contactList(): List<Contact> {
        return (1..100).map {
            Contact(it, "Name$it", "Surname$it", "+7-965-$it-$it-$it")
        }
    }

    private fun onClickContact(position: Int) {
        val contactToEdit = contacts[position]
        NewContact.setId(contactToEdit.id)
        openNewContactActivity()
    }

    private fun updateUi(isClickedTrash: Boolean) {
        with(binding) {
            if (isClickedTrash) {
                addButton.visibility = View.INVISIBLE
                deleteButton.visibility = View.VISIBLE
            } else {
                addButton.visibility = View.VISIBLE
                deleteButton.visibility = View.INVISIBLE
            }
        }
        contactsAdapter.setShowCheckbox(isClickedTrash)
    }

    private fun updateList(newList: List<Contact>) {
        val result = DiffUtil.calculateDiff(
            ContactsDiffUtilCallback(
                contacts,
                newList
            )
        )
        contactsAdapter.contacts = newList
        result.dispatchUpdatesTo(contactsAdapter)
        contacts = newList
    }
}