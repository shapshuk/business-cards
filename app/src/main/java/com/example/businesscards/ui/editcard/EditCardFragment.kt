package com.example.businesscards.ui.editcard

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.nfc.NfcAdapter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.businesscards.R
import com.example.businesscards.core.coreui.BaseFragment
import com.example.businesscards.core.coreui.visible
import com.example.businesscards.core.extensions.getAppComponent
import com.example.businesscards.databinding.FragmentEditCardBinding
import com.example.businesscards.ui.createcard.CreateCardFragmentDirections
import com.example.businesscards.ui.dialog.SuccessDialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.*

class EditCardFragment : BaseFragment() {

    private val binding : FragmentEditCardBinding by lazy {
        FragmentEditCardBinding.inflate(layoutInflater)
    }
    val navArgs: EditCardFragmentArgs by navArgs()

    private val navController: NavController by lazy { findNavController() }

    private val storageRef: StorageReference by lazy {
        FirebaseStorage.getInstance().reference
    }
    private val currentUserId : String by lazy {
        FirebaseAuth.getInstance().currentUser?.uid ?: ""
    }

    private val viewModel: EditCardViewModel by viewModels {
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
        with(binding) {
            nameEditText.setText(navArgs.cardToShare.userName)
            emailEditText.setText(navArgs.cardToShare.email)
            phoneNumberEditText.setText(navArgs.cardToShare.phoneNumber)

            Glide.with(requireContext())
                .load(navArgs.cardToShare.imageUrl)
                .placeholder(R.drawable.ic_account_circle_orange)
                .circleCrop()
                .into(addCardImage)

            saveCardButton.setOnClickListener {
                visible(progressLayout)
                saveCard()
            }
            addCardImage.setOnClickListener {
                pickImage()
            }
            backButton.setOnClickListener {
                requireActivity().onBackPressed()
            }
        }
    }

    private fun pickImage() {
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
            viewModel.imageUri = selectedImage
            Log.d("Picture stream", selectedImage?.path ?: "empty path")
            Glide.with(requireContext())
                .load(selectedImage)
                .circleCrop()
                .placeholder(R.drawable.ic_account_circle_orange)
                .into(binding.addCardImage)
        }
    }

    private fun saveCard() {
        val userName = binding.nameEditText.text.toString()
        val email = binding.emailEditText.text.toString()
        val phoneNumber = binding.phoneNumberEditText.text.toString()
        val jobPosition = "${binding.positionEditText.text} at ${binding.companyEditText.text}"
        val currentCardId = navArgs.cardToShare.cardId

        if (userName.isNotEmpty() && email.isNotEmpty() && phoneNumber.isNotEmpty()) {

            val imageUri = viewModel.imageUri
            val oldImageUrl = navArgs.cardToShare.imageUrl
            if (imageUri != null) {
                val filename = UUID.randomUUID().toString()
                val imageRef = storageRef.child("images/$filename")

                val uploadTask = imageRef.putFile(imageUri)

                uploadTask.addOnSuccessListener { taskSnapshot ->
                    val imageDownloadUrl = taskSnapshot.metadata?.reference?.downloadUrl!!
                    imageDownloadUrl.addOnCompleteListener {
                        viewModel.createCard(currentCardId, currentUserId, userName, email, phoneNumber, jobPosition, it.result.toString())
                    }
                }.addOnFailureListener { exception ->
                    // Handle upload failure
                }
            } else if (!oldImageUrl.isNullOrEmpty()){
                viewModel.createCard(currentCardId, currentUserId, userName, email, phoneNumber, jobPosition, oldImageUrl)
            } else {
                viewModel.createCard(currentCardId, currentUserId, userName, email, phoneNumber, jobPosition, "")
            }
        } else {
            // Display an error message indicating missing fields
        }
    }

    override fun initObservers() {
        viewModel.showSuccessMessageLiveData.observe(viewLifecycleOwner) {
            SuccessDialogFragment().show(childFragmentManager)
        }
        viewModel.navigateBackToMainScreenLiveData.observe(viewLifecycleOwner) {
            navController.navigate(EditCardFragmentDirections.actionEditCardFragmentToCardsMainFragment())
        }
    }

    companion object {
        const val PICK_PHOTO_FOR_AVATAR = 17
    }
}