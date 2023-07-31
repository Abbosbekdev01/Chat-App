package uz.abbosbek.chatappcodial.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessaging
import uz.abbosbek.chatappcodial.R
import uz.abbosbek.chatappcodial.databinding.FragmentAuthBinding
import uz.abbosbek.chatappcodial.models.User
import uz.abbosbek.chatappcodial.utils.MyData.USER
import uz.abbosbek.chatappcodial.utils.MySharedPreference
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class AuthFragment : Fragment() {

    override fun onAttach(context: Context) {
        super.onAttach(context)
        MySharedPreference.init(context)
        if (MySharedPreference.deviceToken!!.isEmpty()) {
            FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
                MySharedPreference.deviceToken = task.result
                Toast.makeText(context, "token taken", Toast.LENGTH_SHORT).show()
            })

        }
    }

    private val binding by lazy { FragmentAuthBinding.inflate(layoutInflater) }
    lateinit var auth: FirebaseAuth
    private lateinit var databse: FirebaseDatabase
    private lateinit var reference: DatabaseReference
    private var token = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail().build()

        val googleSigningClient = GoogleSignIn.getClient(requireActivity(), gso)
        auth = FirebaseAuth.getInstance()
        databse = FirebaseDatabase.getInstance()
        reference = databse.getReference("users")



        MySharedPreference.init(requireContext())


        if (auth.currentUser != null) {
            USER = User(
                auth.uid,
                auth.currentUser?.displayName,
                auth.currentUser?.photoUrl.toString(),
                auth.currentUser?.email,
                SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date()),
                "true",
                MySharedPreference.deviceToken!!
            )
            reference.child(auth.uid!!).setValue(USER)

            findNavController().navigate(
                R.id.homeFragment,
                null,
                NavOptions.Builder()
                    .setPopUpTo(findNavController().currentDestination?.id ?: 0, true).build()
            )
        }

        binding.btnGoogle.setOnClickListener {
            startActivityForResult(googleSigningClient.signInIntent, 1)
        }

        return binding.root
    }


}