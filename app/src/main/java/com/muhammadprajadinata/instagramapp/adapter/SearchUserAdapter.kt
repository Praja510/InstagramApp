package com.muhammadprajadinata.instagramapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.muhammadprajadinata.instagramapp.R
import com.muhammadprajadinata.instagramapp.fragment.ProfilFragment
import com.muhammadprajadinata.instagramapp.model.User
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.list_item_search.view.*

class SearchUserAdapter (private var mContext: Context, private val mUser: List<User>) : RecyclerView.Adapter<SearchViewHolder>(){

    private var firebaseUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.list_item_search, parent, false)
        return SearchViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mUser.size
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val user = mUser[position]
        holder.usernameTxtView.text = user.getUsername()
        holder.fullnameTxtView.text = user.getFullname()
        Picasso.get()
            .load(user.getImage())
            .into(holder.userProfileImage)

        cekFollowStatus(user.getUID(), holder.followButton)

        holder.itemView.setOnClickListener {
            val pref = mContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit()
            pref.putString("profileId", user.getUID())
            pref.apply()

            (mContext as FragmentActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.fragContainer, ProfilFragment()).commit()
        }

        holder.followButton.setOnClickListener {
            if (holder.followButton.text.toString() == "Follow") {
                firebaseUser?.uid.let {it ->
                    FirebaseDatabase.getInstance().reference
                        .child("Follow").child(it.toString())
                        .child("Following").child(user.getUID())
                        .setValue(true).addOnCompleteListener{task ->
                            if (task.isSuccessful) {
                                firebaseUser?.uid.let {
                                    FirebaseDatabase.getInstance().reference
                                        .child("Follow").child(user.getUID())
                                        .child("Followers").child(it.toString())
                                        .setValue(true).addOnCompleteListener {
                                            if (task.isSuccessful) {

                                            }
                                        }
                                }
                            }
                        }
                }
            }else{
                firebaseUser?.uid.let {it ->
                    FirebaseDatabase.getInstance().reference
                        .child("Follow").child(it.toString())
                        .child("Following").child(user.getUID())
                        .removeValue().addOnCompleteListener{task ->
                            if (task.isSuccessful){
                                firebaseUser?.uid.let {
                                    FirebaseDatabase.getInstance().reference
                                        .child("Follow").child(user.getUID())
                                        .child("Followers").child(it.toString())
                                        .removeValue().addOnCompleteListener {task ->
                                            if (task.isSuccessful){

                                            }
                                        }
                                }
                            }
                        }
                }
            }
        }
    }

    private fun cekFollowStatus(uid: String, followButton: Button) {
        val followingRef = firebaseUser?.uid.let {it ->
            FirebaseDatabase.getInstance().reference
                .child("Follow").child(it.toString())
                .child("Following")
        }

        followingRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.child(uid).exists()){
                    followButton.text = "Following"
                }else{
                    followButton.text = "Follow"
                }
            }

        })
    }
}

class SearchViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    val usernameTxtView: TextView = itemView.findViewById(R.id.txt_username_search)
    val fullnameTxtView: TextView = itemView.findViewById(R.id.txt_fullname_search)
    val userProfileImage: ImageView = itemView.findViewById(R.id.img_search_profile)
    val followButton: Button = itemView.findViewById(R.id.btn_follow_search)

}