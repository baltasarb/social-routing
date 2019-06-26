package ps.g49.socialroutingclient.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import ps.g49.socialroutingclient.R
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task
import com.google.android.gms.common.api.ApiException
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_main.*
import ps.g49.socialroutingclient.SocialRoutingApplication
import ps.g49.socialroutingclient.kotlinx.getViewModel
import ps.g49.socialroutingclient.model.UserAccount
import ps.g49.socialroutingclient.model.inputModel.AuthenticationDataInput
import ps.g49.socialroutingclient.viewModel.SocialRoutingViewModel
import ps.g49.socialroutingclient.dagger.factory.ViewModelFactory
import ps.g49.socialroutingclient.model.inputModel.SocialRoutingRootResource
import javax.inject.Inject

class LoginActivity : BaseActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var socialRoutingViewModel: SocialRoutingViewModel
    private lateinit var socialRoutingApplication: SocialRoutingApplication

    companion object {
        const val RC_SIGN_IN = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        socialRoutingApplication = application as SocialRoutingApplication

        // Configure sign-in to request the user's ID, email address, and basic profile.
        // ID and basic profile are included in DEFAULT_SIGN_IN.
        val gso = GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.server_client_id))
            .requestEmail()
            .build()

        socialRoutingViewModel = getViewModel(viewModelFactory)
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        sign_in_google_account_button.setOnClickListener {
            val signInIntent = mGoogleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }

        obtainSocialRoutingAPIRootResources()
    }

    private fun obtainSocialRoutingAPIRootResources() {
        val liveData = socialRoutingViewModel.getRootResource()
        handleRequestedData(liveData, ::successHandlerRootResources, ::errorHandlerRootResource)
    }

    private fun successHandlerRootResources(socialRoutingRootResource: SocialRoutingRootResource?) {
        socialRoutingApplication.setSocialRoutingRootResource(socialRoutingRootResource!!)
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        val account = GoogleSignIn.getLastSignedInAccount(this)
        if (account != null) {
            saveAccount(account)
            val authenticationUrl = socialRoutingRootResource.authenticationUrls["google"]!!
            val liveData = socialRoutingViewModel.signIn(authenticationUrl, account.idToken!!)
            handleRequestedData(liveData, ::successHandlerSignIn)
        }
    }

    private fun errorHandlerRootResource(msg: String) {
        showToast("The Server is currently down, sorry! Try later...")
        sign_in_google_account_button.visibility = View.INVISIBLE
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
            saveAccount(account)

            val authenticationUrl = socialRoutingApplication
                .getSocialRoutingRootResource()
                .authenticationUrls["google"]!!
            val liveData = socialRoutingViewModel.signIn(authenticationUrl, idToken)
            handleRequestedData(liveData, ::successHandlerSignIn)

        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            val errorLoginMessage = getString(R.string.google_login_error)
            showToast(errorLoginMessage)
            stopSpinner()
        }
    }


    private fun successHandlerSignIn(authenticationData: AuthenticationDataInput?) {
        val user = socialRoutingApplication.getUser()
        user.accessToken = authenticationData!!.accessToken
        user.refreshToken = authenticationData.refreshToken
        user.userUrl = authenticationData.userUrl!!

        val welcomeMessage = getString(R.string.welcome_message)
        showToast(String.format(welcomeMessage, user.name))
        val intent = Intent(this, NavigationActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun saveAccount(account: GoogleSignInAccount) {
        val accountName = account.displayName ?: "No name"
        val accountEmail = account.email ?: "No email"
        val accountPhotoUrl = account.photoUrl ?: Uri.EMPTY
        val userAccount = UserAccount(accountName, accountEmail, accountPhotoUrl)
        socialRoutingApplication.setUser(userAccount)
    }

}
