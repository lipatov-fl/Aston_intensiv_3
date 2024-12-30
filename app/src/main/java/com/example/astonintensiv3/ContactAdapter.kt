package com.example.astonintensiv3

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.astonintensiv3.databinding.ContactItemBinding

class ContactAdapter(
    var contacts: List<Contact>,
    private val onClickContact: (position: Int) -> Unit
) : RecyclerView.Adapter<ContactViewHolder>() {

    private val deletedContact = mutableSetOf<Contact>()
    private var showCheckbox = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        return ContactViewHolder(
            ContactItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun getItemCount(): Int = contacts.size

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val contact = contacts[position]

        with(holder.binding) {
            checkbox.isVisible = showCheckbox
            checkbox.isChecked = deletedContact.contains(contact)
            firstName.text = contact.firstName
            lastName.text = contact.lastName
            number.text = contact.number

            checkbox.setOnClickListener {
                if (checkbox.isChecked) {
                    deletedContact.add(contact)
                } else {
                    deletedContact.remove(contact)
                }
            }

            root.setOnClickListener {
                if (!showCheckbox || !checkbox.isChecked) {
                    onClickContact(position)
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setShowCheckbox(isShow: Boolean) {
        showCheckbox = isShow
        this.notifyDataSetChanged()
    }

    fun deletedContactList(): List<Contact> {
        val newList = contacts.filterNot { deletedContact.contains(it) }
        deletedContact.clear()
        return newList
    }
}

class ContactViewHolder(
    val binding: ContactItemBinding
) : RecyclerView.ViewHolder(binding.root)
