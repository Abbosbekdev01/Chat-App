package uz.abbosbek.chatappcodial.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import uz.abbosbek.chatappcodial.R
import uz.abbosbek.chatappcodial.adapters.AddGroupAdapter
import uz.abbosbek.chatappcodial.databinding.FragmentAddGroupBinding
import uz.abbosbek.chatappcodial.models.Group
import uz.abbosbek.chatappcodial.models.MessageText
import uz.abbosbek.chatappcodial.models.User
import uz.abbosbek.chatappcodial.utils.MyData.USER
import uz.abbosbek.chatappcodial.utils.MyData.userList


class AddGroupFragment : Fragment() {

    private lateinit var binding: FragmentAddGroupBinding
    private lateinit var addGroupAdapter: AddGroupAdapter
    private lateinit var reference: DatabaseReference
    private lateinit var database: FirebaseDatabase
    private var listOfSelectedUserIds = ArrayList<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddGroupBinding.inflate(layoutInflater, container, false)

        database = FirebaseDatabase.getInstance()
        reference = database.getReference("groups")
        listOfSelectedUserIds.add(USER.uid!!)
        val members = userList
        members.removeAt(0)
        addGroupAdapter = AddGroupAdapter(members) { user: User, boolean: Boolean ->
            listItemSelectedOrUnselected(
                user,
                boolean
            )
        }
        binding.myRv.adapter = addGroupAdapter

        binding.btnAddNewGroup.setOnClickListener {
            if (binding.edtName.text.toString().trim().isNotEmpty()) {
                binding.btnAddNewGroup.isEnabled = false
                addNewGroup(binding.edtName.text.toString().trim())
            } else {
                Toast.makeText(context, "Enter Group name", Toast.LENGTH_SHORT).show()
            }
        }
        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
        return binding.root
    }

    private fun addNewGroup(name: String) {
        val key = reference.push().key
        val group = Group()
        group.id = key
        group.name = name
        group.listMessages = ArrayList<MessageText>().toString()
        group.groupMemberCount = listOfSelectedUserIds.size.toString()

        var id = ""
        listOfSelectedUserIds.forEach {
            id += it
        }

        group.groupUsersIds = id

        reference.child(key!!).setValue(group)
        binding.btnAddNewGroup.isEnabled = true
        findNavController().navigate(
            R.id.homeFragment,
            null,
            NavOptions.Builder()
                .setPopUpTo(findNavController().currentDestination?.id ?: 0, true).build()
        )
    }

    private fun listItemSelectedOrUnselected(user: User, boolean: Boolean) {
        if (boolean) {
            listOfSelectedUserIds.add(user.uid!!)
        } else {
            listOfSelectedUserIds.remove(user.uid!!)
        }
    }
}