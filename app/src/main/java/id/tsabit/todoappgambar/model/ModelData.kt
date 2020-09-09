package id.tsabit.todoappgambar.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ModelData(
    var profile_gambar : String = "",
    var profile_nama : String = "",
    var profile_kelas : String = "",
    var profile_alamat : String = ""
) : Parcelable