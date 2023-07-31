package uz.abbosbek.chatappcodial.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import uz.abbosbek.chatappcodial.R
import uz.abbosbek.chatappcodial.adapters.UsersAdapter
import uz.abbosbek.chatappcodial.databinding.FragmentUsersBinding
import uz.abbosbek.chatappcodial.models.User
import uz.abbosbek.chatappcodial.utils.MyData.USER
import uz.abbosbek.chatappcodial.utils.MyData.userList

class UsersFragment : Fragment() {
    private lateinit var binding: FragmentUsersBinding
    private lateinit var databse: FirebaseDatabase
    private lateinit var reference: DatabaseReference
    private lateinit var usersAdapter: UsersAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUsersBinding.inflate(layoutInflater, container, false)

        databse = FirebaseDatabase.getInstance()
        reference = databse.getReference("users")

        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = ArrayList<User>()
                val children = snapshot.children
                val owner = snapshot.child(USER.uid!!).getValue(User::class.java)
                owner?.imageLink = R.drawable.baseline_bookmark_border_24.toString()
                owner?.name = "Saved Messages"
                list.add(owner!!)
                children.forEach {
                    val user = it.getValue(User::class.java)
                    if (user != null && user.uid != owner.uid) list.add(user)

                }
                userList = list

                usersAdapter = UsersAdapter(list) { user: User, position: Int ->
                    listItemClicked(
                        user,
                        position
                    )
                }
                binding.myRv.adapter = usersAdapter
                binding.progressBar.visibility = View.INVISIBLE

            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
        return binding.root
    }

    private fun listItemClicked(user: User, position: Int) {

        findNavController().navigate(R.id.chatFragment, bundleOf("user" to user))
    }
}