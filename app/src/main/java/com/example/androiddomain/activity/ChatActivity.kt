package com.example.androiddomain.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.lumipixdatingapp.adapter.MessageAdapter
import com.example.lumipixdatingapp.databinding.ActivityChatBinding
import com.example.lumipixdatingapp.model.MessageModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class ChatActivity : AppCompatActivity() {

    private lateinit var binding : ActivityChatBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getData(intent.getStringExtra("chat_id"))

        binding.imageView4.setOnClickListener {
            if(binding.yourMessage.text!!.isEmpty()){
                Toast.makeText(this, "Please enter your message", Toast.LENGTH_SHORT).show()
            }else {
               sendMessage(binding.yourMessage.text.toString())
            }
        }
    }

    private fun getData(chatId: String?) {

        FirebaseDatabase.getInstance().getReference("chats")
            .child(chatId!!).addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {

                    val list = arrayListOf<MessageModel>()

                    for (show in snapshot.children){
                        list.add(show.getValue(MessageModel::class.java)!!)
                    }

                    binding.recyclerView2.adapter = MessageAdapter(this@ChatActivity, list)

                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@ChatActivity, error.message, Toast.LENGTH_SHORT).show()
                }

            })

    }

    @SuppressLint("SuspiciousIndentation")
    private fun sendMessage(msg: String) {

        val receiverId = intent.getStringExtra("userId")
        val senderID = FirebaseAuth.getInstance().currentUser!!.phoneNumber


        val chatID = senderID+receiverId
        val reverseChatId = receiverId + senderID





        val reference = FirebaseDatabase.getInstance().getReference("chats")

        reference.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                if (snapshot.hasChild(reverseChatId)){
                    storeData(reverseChatId ,msg,senderID)
                }else{
                    storeData(chatID, msg,senderID)
                }

            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@ChatActivity, "Something went wrong", Toast.LENGTH_SHORT).show()
            }

        })



    }

    private fun storeData(chatId: String, msg: String, senderID: String?) {

        val currentDate: String = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
        val currentTime = SimpleDateFormat("HH:mm a", Locale.getDefault()).format(Date())



        val map = hashMapOf<String, String>()
        map["message"] = msg
        map["senderId"] = senderID!!
        map["currentTime"] =currentTime
        map["currentDate"] =currentDate

        val reference = FirebaseDatabase.getInstance().getReference("chats").child(chatId)
        reference.child(reference.push().key!!).setValue(map).addOnCompleteListener {
            if (it.isSuccessful){
                binding.yourMessage.text = null
                Toast.makeText(this, "Message Sended", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this, "Something Went Wrong", Toast.LENGTH_SHORT).show()
            }
        }
    }
}