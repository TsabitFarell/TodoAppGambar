package id.tsabit.todoappgambar

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Display
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*
import id.tsabit.todoappgambar.databinding.ActivityMainBinding
import id.tsabit.todoappgambar.model.ModelData
import id.tsabit.todoappgambar.recyclerview.adapter.ItemDataAdapter

class MainActivity : AppCompatActivity() {

    // private lateinit var adalah variabel yang perlu diisi nilainya di fungsi onCreate
    private lateinit var binding: ActivityMainBinding

    // Buat variabel adapter untuk RecyclerView
    private lateinit var adapterMain: ItemDataAdapter

    // Buat variabel databaseUser berisi DatabaseReference dari Firebase
    private lateinit var databaseUser : DatabaseReference

    private lateinit var valueEventListener: ValueEventListener


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Isi variabel binding
        val inflater = layoutInflater
        binding = ActivityMainBinding.inflate(inflater)
        setContentView(binding.root)

        adapterMain = ItemDataAdapter()

        // Isi lateinit var databaseUser dengan nama folder lokasi yang dituju dalam Firebase
        databaseUser = FirebaseDatabase.getInstance().reference.child("Daftar Nama")

        binding.extendedFab.setOnClickListener {
            val intent = Intent(this, AddDataActivity::class.java)
            startActivity(intent)
        }


        binding.rvMain.run {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = adapterMain
            setHasFixedSize(true)
        }

        // Buat valueEventListener untuk mengecek data yang di Firebase
        valueEventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.childrenCount > 0) {
                    // Buat array kosong
                    val daftarUser = arrayListOf<ModelData>()
                    for (dataUser in snapshot.children) {
                        val data = dataUser.getValue(ModelData::class.java) as ModelData
                        daftarUser.add(data)
                    }
                    // Menggunakan addData untuk memasukkan data ke adapter RecyclerView
                    adapterMain.addData(daftarUser)
                    // Memberitahu adapter RecyclerView jika ada perubahan data
                    adapterMain.notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        }

        // Tambahkan valueEventListener ke dalam DatabaseUser
        databaseUser.addValueEventListener(valueEventListener)

    }

    override fun onDestroy() {
        super.onDestroy()
        databaseUser.removeEventListener(valueEventListener)
    }
}