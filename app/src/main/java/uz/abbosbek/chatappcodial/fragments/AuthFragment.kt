package uz.abbosbek.chatappcodial.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
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


private const val TAG = "AuthFragment"

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
    private var RC_SIGN_IN = 1
    private lateinit var googleSigningClient: GoogleSignInClient

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail().build()

        googleSigningClient = GoogleSignIn.getClient(requireActivity(), gso)
        auth = FirebaseAuth.getInstance()
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
        databse = FirebaseDatabase.getInstance()
        reference = databse.getReference("users")

        MySharedPreference.init(requireContext())
        binding.btnGoogle.setOnClickListener {
            signIn()
        }

        return binding.root
    }

    private fun signIn() {
        val signInIntent = googleSigningClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                Log.d(TAG, "onActivityResult:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                Log.d(TAG, "onActivityResult:", e)
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "firebaseAuthWithGoogle: Success")
                    val user = User(
                        auth.currentUser?.uid,
                        auth.currentUser?.displayName,
                        auth.currentUser?.photoUrl.toString(),
                        auth.currentUser?.email,
                        auth.currentUser?.tenantId,
                        auth.currentUser?.phoneNumber,
                        auth.currentUser?.getIdToken(true).toString()
                    )
                    Toast.makeText(requireContext(), "${user.email}", Toast.LENGTH_SHORT).show()
                } else {
                    Log.d(TAG, "firebaseAuthWithGoogle: ${task.exception}")
                    Toast.makeText(
                        requireContext(),
                        "${task.exception?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }
    }

}