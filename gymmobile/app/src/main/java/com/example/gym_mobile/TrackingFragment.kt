package com.example.gym_mobile

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.example.gym_mobile.databinding.FragmentTrackingBinding
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.fragment_add_set.*
import kotlinx.android.synthetic.main.fragment_exercises.*
import kotlinx.android.synthetic.main.fragment_tracking.*

class TrackingFragment : Fragment() {

    private lateinit var binding: FragmentTrackingBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTrackingBinding.inflate(inflater, container, false)


//----------Date Picker
        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select date")
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .build()

        binding.selectDate.setOnClickListener{
            activity?.let { it1 -> datePicker.show(it1.supportFragmentManager, "Material Date Picker") }
        }
//----------Date Picker

        var input = ""
        var myInputField = view?.findViewById<TextInputEditText>(R.id.weightInput)
        if (myInputField != null) {
            myInputField.doOnTextChanged { text, start, before, count ->
                Log.d("LOG", myInputField.text.toString())
            }
        }

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnGoToExercises.setOnClickListener {
            findNavController().navigate(R.id.action_trackingFragment_to_exercisesFragment)
        }
        weightInput
        addNewSet.setOnClickListener(){
            val newFragment = onCreateDialog(savedInstanceState)
            newFragment.show()
            Log.d("LOG", "trying to open")
        }
    }


    private fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            // Get the layout inflater
            val inflater = requireActivity().layoutInflater;

            fun saveDetails(){
                var weightsInput = weightInput.text.toString()
                var setsInput = setInput.text.toString()
//                textView.setText(weightsInput)

            }

            // Inflate and set the layout for the dialog
            // Pass null as the parent view because its going in the dialog layout
            builder.setView(inflater.inflate(R.layout.fragment_add_set, null))

            if (weightInput !== null ){
                weightInput.doOnTextChanged { text, start, before, count ->
                    Log.d("Debugging DDDD","On text changed listener")
                }

            }

            builder.setPositiveButton("Save") { dialog, which ->
                Log.d("Debugging DDDD","jvsjdh")
            //                var weightsInput = weightInput.text.toString()
//                Toast.makeText(binding.root.context, weightsInput, Toast.LENGTH_SHORT).show()
            }

            builder.setNegativeButton("Cancel") { dialog, which ->
                dialog.dismiss()
                Toast.makeText(binding.root.context, "Canceled", Toast.LENGTH_SHORT).show()
            }

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}