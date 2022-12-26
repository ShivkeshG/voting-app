package com.example.votingapp.dao

import com.example.votingapp.Candidate
import com.example.votingapp.User
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class UserDao {

    private val database = Firebase.database
    private val userRef = database.getReference("Users")
    private val db = Firebase.firestore


    @OptIn(DelicateCoroutinesApi::class)
    fun addUser(user: User?) {
        user?.let {
            GlobalScope.launch(Dispatchers.IO) {
                user.uid?.let { it1 -> userRef.child(it1).setValue(it) }
            }
        }
    }

    fun voteCandidate(uid: String, candidateName: String) {
//        val currentUserId = auth.currentUser!!.uid
        val candidate = Candidate()
        val isVoted = candidate.votedBy.contains(uid)

        if(isVoted) {
            candidate.votedBy.remove(uid)
        } else {
            candidate.votedBy.add(uid)
        }

        db.collection("Candidates").document(candidateName).get().addOnCompleteListener { task->
            if (task.result.exists()) {
                db.collection("Candidates").document(candidateName).update("votedBy", FieldValue.arrayUnion(uid))
            }else {
                db.collection("Candidates").document(candidateName).set(candidate)
            }
        }
    }

}