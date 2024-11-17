package com.example.androiddomain.ui

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.androiddomain.R
import com.example.androiddomain.activity.EditProfileActivity
import com.example.androiddomain.auth.LoginActivity
import com.example.androiddomain.databinding.FragmentProfileBinding
import com.example.androiddomain.model.UserModel
import com.example.androiddomain.utils.Config
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.ar.core.Config
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase


class ProfileFragment : Fragment() {

    private lateinit var binding : FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        Bitmap.Config.showDialog(requireContext())

        binding = FragmentProfileBinding.inflate(layoutInflater)



        FirebaseDatabase.getInstance().getReference("users")
            .child(FirebaseAuth.getInstance().currentUser!!.phoneNumber!!).get()
            .addOnSuccessListener {
                if(it.exists()){
                    val data = it.getValue(UserModel::class.java)


                    binding.name.setText(data!!.name.toString())
                    binding.city.setText(data!!.city.toString())
                    binding.email.setText(data!!.email.toString())
                    binding.number.setText(data!!.number.toString())

                    Glide.with(requireContext()).load(data.image).placeholder(R.drawable.users).into(binding.userImage)


                    Bitmap.Config.hideDialog()
                }
            }



        binding.logout.setOnClickListener{
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(requireContext(), LoginActivity::class.java))
            requireActivity().finish()
        }

        binding.editProfile.setOnClickListener{
            startActivity(Intent(requireContext(),EditProfileActivity::class.java))
        }



        return (binding.root)
    }
}}