package uz.abbosbek.chatappcodial.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import uz.abbosbek.chatappcodial.R
import uz.abbosbek.chatappcodial.adapters.GroupAdapter
import uz.abbosbek.chatappcodial.databinding.FragmentGroupsBinding
import uz.abbosbek.chatappcodial.models.Group
import uz.abbosbek.chatappcodial.utils.MyData.USER


class GroupsFragment : Fragment() {

    private lateinit var binding: FragmentGroupsBinding
    private lateinit var databse: FirebaseDatabase
    private lateinit var reference: DatabaseReference
    private lateinit var groupAdapter: GroupAdapter
    private val homeFragment = HomeFragment()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentGroupsBinding.inflate(layoutInflater, container, false)

        databse = FirebaseDatabase.getInstance()
        reference = databse.getReference("groups")


        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = ArrayList<Group>()
                val children = snapshot.children
                children.forEach {
                    val group = it.getValue(Group::class.java)
                    if (group != null && group.groupUsersIds!!.contains(USER.uid!!)) list.add(group)

                }

                groupAdapter = GroupAdapter(list) { group: Group, position: Int ->
                    listItemClicked(
                        group,
                        position
                    )
                }
                binding.myRv.adapter = groupAdapter
                binding.progressBar.visibility = View.INVISIBLE

            }

            override fun onCancelled(error: DatabaseError) {

            }
        })



        binding.btnAddNewGroup.setOnClickListener {
            findNavController().navigate(R.id.addGroupFragment)
        }
        return binding.root
    }

    private fun listItemClicked(group: Group, position: Int) {
        findNavController().navigate(R.id.groupChatFragment, bundleOf("group" to group))

    }
}