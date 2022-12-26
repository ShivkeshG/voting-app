package com.example.votingapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.RadioButton
import android.widget.Toast
import com.example.votingapp.dao.UserDao
import com.example.votingapp.databinding.ActivityMainBinding
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    private val db = Firebase.firestore
    private val candidateCollection = db.collection("Candidates")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        auth = Firebase.auth

        binding.btnLogout.setOnClickListener {
            auth.signOut()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        binding.btnVote.setOnClickListener {
            vote()
        }
    }

    private fun getCandidateById(candidateId: String): Task<DocumentSnapshot> {
        return candidateCollection.document(candidateId).get()
    }

    private fun vote() {
        val checkedCandidate = binding.radioGroup.checkedRadioButtonId
        val voted = findViewById<RadioButton>(checkedCandidate).text.toString()
        val uid = auth.currentUser?.uid
        val votesList = ArrayList<Candidate>()

        CoroutineScope(Dispatchers.IO).launch {
            votesList.add(getCandidateById("Candidate 1").await().toObject(Candidate::class.java)!!)
            votesList.add(getCandidateById("Candidate 2").await().toObject(Candidate::class.java)!!)
            votesList.add(getCandidateById("Candidate 3").await().toObject(Candidate::class.java)!!)
            votesList.add(getCandidateById("Candidate 4").await().toObject(Candidate::class.java)!!)

            val userDao = UserDao()
            uid?.let {
                if (votesList[0].votedBy.contains(uid)) {
                    withContext(Dispatchers.Main){
                        alreadyVoted()
                    }
                }else if (votesList[1].votedBy.contains(uid)) {
//                    alreadyVoted()
                }else if (votesList[2].votedBy.contains(uid)) {
//                    alreadyVoted()
                }else if (votesList[3].votedBy.contains(uid)) {
//                    alreadyVoted()
                }else{
                    userDao.voteCandidate(uid, voted)
                    withContext(Dispatchers.Main){
                        Toast.makeText(this@MainActivity, "You have voted${voted}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

    }

    private fun alreadyVoted() {
            Toast.makeText(this@MainActivity, "You have already voted", Toast.LENGTH_SHORT).show()
    }
}