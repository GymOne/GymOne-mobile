package com.example.gym_mobile

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.fragment_add_set.*

class AddSetFragment : DialogFragment() {
    // Use this instance of the interface to deliver action events
    internal lateinit var listener: AddSetDialogListener

    /* The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it. */
    interface AddSetDialogListener {
        fun onDialogPositiveClick(dialog: DialogFragment){
            Log.d("LOG", "positive click")
        }
        fun onDialogNegativeClick(dialog: DialogFragment) {
            Log.d("LOG", "negative click")
        }
    }

    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    override fun onAttach(context: Context) {
        super.onAttach(context)
        // Verify that the host activity implements the callback interface

//        saveSet.setOnClickListener(){
//            Log.d("LOG", "trying to save set")
//
//        }
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            listener = context as AddSetDialogListener
        } catch (e: ClassCastException) {
            // The activity doesn't implement the interface, throw exception
            throw ClassCastException((context.toString() +
                    " must implement DialogListener"))
        }
    }
}