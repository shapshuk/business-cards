package com.example.businesscards.ui.createcard

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.businesscards.R
import com.example.businesscards.core.coreui.BaseFragment
import com.example.businesscards.core.extensions.getAppComponent
import com.example.businesscards.databinding.FragmentCreateCardBinding

class CreateCardFragment : BaseFragment() {

    private val binding: FragmentCreateCardBinding by lazy {
        FragmentCreateCardBinding.inflate(layoutInflater)
    }

    private val viewModel : CreateCardViewModel by viewModels {
        getAppComponent().viewModelsFactory()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        getAppComponent().inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun initViews() {
        binding.addCardImage.setOnClickListener {
            pickImage()
        }
    }

    fun pickImage() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.setType("image/*")
        startActivityForResult(intent, PICK_PHOTO_FOR_AVATAR)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_PHOTO_FOR_AVATAR && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                //Display an error
                Toast.makeText(requireContext(), "Picture loading failed", Toast.LENGTH_SHORT).show()
                return
            }
            val selectedImage = data.data
            Log.d("Picture stream", selectedImage?.path ?: "empty path")
            Glide.with(requireContext())
                .load(selectedImage)
                .circleCrop()
                .placeholder(R.drawable.ic_account_circle_orange)
                .into(binding.addCardImage)
        }
    }

    companion object {
        const val PICK_PHOTO_FOR_AVATAR = 17
    }
}