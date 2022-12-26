package com.example.adminvotingapp

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.adminvotingapp.databinding.ActivityMainBinding
import com.google.android.gms.tasks.Task
import com.google.firebase.database.*
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val db = Firebase.firestore
    private val voteCollection = db.collection("Candidates")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getData()
    }

    private fun getVoteById(voteId: String): Task<DocumentSnapshot> {
        return voteCollection.document(voteId).get()
    }

    private fun getData() {
        CoroutineScope(Dispatchers.IO).launch{
            val candidate1 = getVoteById("Candidate 1").await().toObject(Vote::class.java)
            val candidate2 = getVoteById("Candidate 2").await().toObject(Vote::class.java)
            val candidate3 = getVoteById("Candidate 3").await().toObject(Vote::class.java)
            val candidate4 = getVoteById("Candidate 4").await().toObject(Vote::class.java)

            withContext(Dispatchers.Main){
                binding.apply {
                    tvCandidate1.text = candidate1?.votedBy?.size.toString()
                    tvCandidate2.text = candidate2?.votedBy?.size.toString()
                    tvCandidate3.text = candidate3?.votedBy?.size.toString()
                    tvCandidate4.text = candidate4?.votedBy?.size.toString()

                }

            }
        }
    }
}