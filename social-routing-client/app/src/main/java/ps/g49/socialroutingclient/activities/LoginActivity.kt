package ps.g49.socialroutingclient.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import ps.g49.socialroutingclient.R
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task
import com.google.android.gms.common.api.ApiException
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import ps.g49.socialroutingclient.kotlinx.getViewModel
import ps.g49.socialroutingclient.model.UserAccount
import ps.g49.socialroutingclient.viewModel.SocialRoutingViewModel


class LoginActivity : BaseActivity() {

    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var socialRoutingViewModel: SocialRoutingViewModel

    companion object {
        private const val RC_SIGN_IN = 1
        private const val SUCCESS_LOGIN_MESSAGE = "Logged in with success"
        private const val ERROR_LOGIN_MESSAGE = "Something went wrong try again"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Configure sign-in to request the user's ID, email address, and basic profile.
        // ID and basic profile are included in DEFAULT_SIGN_IN.
        val gso = GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.server_client_id))
            .requestEmail()
            .build()

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
        socialRoutingViewModel = getViewModel()

        sign_in_google_account_button.setOnClickListener {
            val signInIntent = mGoogleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }
    }

    override fun onStart() {
        super.onStart()

        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        val account = GoogleSignIn.getLastSignedInAccount(this);
        //updateUI(account);
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach a listener.
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)!!
            val idToken = account.idToken!!
            val accountName = account.displayName ?: "No name"
            val accountEmail = account.email ?: "No email"
            val accountPhotoUrl = account.photoUrl ?: Uri.EMPTY
            val userAccount = UserAccount(accountName, accountEmail, accountPhotoUrl)

            val liveData = socialRoutingViewModel.signIn(idToken)
            handleRequestedData(liveData, ::requestSuccessHandlerSignIn)

        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            showToast(ERROR_LOGIN_MESSAGE)
            stopSpinner()
        }
    }

    private fun requestSuccessHandlerSignIn() {
        showToast(SUCCESS_LOGIN_MESSAGE)
        val intent = Intent(this, NavigationActivity::class.java)
        startActivity(intent)
    }

}
