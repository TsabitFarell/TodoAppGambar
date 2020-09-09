package id.tsabit.todoappgambar

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.util.Log
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.theartofdev.edmodo.cropper.CropImage
import id.tsabit.todoappgambar.databinding.ActivityAddDataBinding
import id.tsabit.todoappgambar.model.ModelData
import kotlinx.android.synthetic.main.activity_add_data.*

class AddDataActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddDataBinding

    private var userData: ModelData? = null

    private lateinit var databaseRef: DatabaseReference

    private lateinit var firebaseStorage: StorageReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Isi nilai viewBinding
        val inflater = layoutInflater
        binding = ActivityAddDataBinding.inflate(inflater)
        setContentView(binding.root)

        databaseRef = FirebaseDatabase.getInstance().reference

        firebaseStorage = FirebaseStorage.getInstance().reference.child("Profile Photo")

        // userData ambil nilai parcel dari Intent
        userData = intent.getParcelableExtra("DATA")

        userData?.let { dataUser ->
            binding.run {
                btnCancel.visibility = View.VISIBLE
                btnSave.visibility = View.VISIBLE
                imgProfile.visibility = View.VISIBLE
                edtName.text = SpannableStringBuilder(dataUser.profile_nama)
                edtClass.text = SpannableStringBuilder(dataUser.profile_kelas)
                edtAddress.text = SpannableStringBuilder(dataUser.profile_alamat)
                Glide.with(this@AddDataActivity)
                    .load(dataUser.profile_gambar)
                    .placeholder(R.drawable.gambar_placeholder)
                    .into(imgProfile)

                btn_delete.setOnClickListener {
                    deleteData(dataUser)
                }

                binding.btnChoosePhoto.setOnClickListener {
                    CropImage.activity().setAspectRatio(1, 1).start(this@AddDataActivity)
                }
            }
        }

        binding.btnSave.setOnClickListener {
            binding.run {
                val namaUser = edtName.text.toString()
                val kelasUser = edtClass.text.toString()
                val alamatUser = edtAddress.text.toString()

                val peringatan =
                    when {
                        namaUser.isBlank() -> "Nama User Kosong"
                        kelasUser.isBlank() -> "Kelas User Kosong"
                        alamatUser.isBlank() -> "Alamat User Kosong"
                        else -> ""
                    }

                if (peringatan.isBlank()) {
                    val dataUser = ModelData(
                        userData?.profile_gambar ?: "",
                        namaUser,
                        kelasUser,
                        alamatUser
                    )
                    saveData(dataUser)
                } else {
                    Toast.makeText(this@AddDataActivity, peringatan, Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.btnCancel.setOnClickListener {
            finish()
        }
    }

        private fun deleteData(userData: ModelData) {
            val userDB = databaseRef.child("Daftar Nama")
                .child(userData.profile_nama)
                .removeValue()
            userDB.addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(this, "User Telah dihapus", Toast.LENGTH_SHORT).show()
                }
            }
        }

        private fun saveData(userData: ModelData) {
            val userDB = databaseRef.child("Daftar Nama")
                .child(userData.profile_nama)
                .setValue(userData)

            userDB.addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(this, "User Telah Diperbarui", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }

        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            super.onActivityResult(requestCode, resultCode, data)
            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE
                && resultCode == Activity.RESULT_OK
                && data != null
            ) {
                val resultUriImage = CropImage.getActivityResult(data).uri
                val fileRef = firebaseStorage.child(userData?.profile_nama + ".jpg")
                val uploadImage = fileRef.putFile(resultUriImage)
                uploadImage.continueWithTask { task ->
                    if (!task.isSuccessful) {
                        task.exception?.let { error ->
                            Log.e("Gagal Upload", error.localizedMessage.toString())
                        }
                    }
                    fileRef.downloadUrl
                }.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val downloadUri = task.result

                        userData?.let {
                            it.profile_gambar = downloadUri.toString()
                            Glide.with(this).load(it.profile_gambar).into(binding.imgProfile)
                        }
                    } else {
                        Toast.makeText(this, "Gagal Upload Foto Profile", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }
    }