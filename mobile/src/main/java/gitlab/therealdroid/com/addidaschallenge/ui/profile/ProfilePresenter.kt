package gitlab.therealdroid.com.addidaschallenge.ui.profile

import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignIn
import gitlab.therealdroid.com.addidaschallenge.domain.Repository
import gitlab.therealdroid.com.addidaschallenge.domain.models.Profile
import com.google.android.gms.auth.api.signin.GoogleSignInOptions


class ProfilePresenter(var context: Context, private val contract: ProfileContract) {
    private val repository = Repository()

    fun fetchUserProfile() {
        val totalPoints = repository.getTotalPointsEarned()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        GoogleSignIn.getClient(context, gso).silentSignIn()

        val acct = GoogleSignIn.getLastSignedInAccount(context)

        if (acct != null) {
            val displayName = if (acct.displayName == null) {
                "Welcome"
            } else {
                acct.displayName
            }
            val profile = Profile(displayName, acct.photoUrl.toString(), totalPoints)
            contract.fetchUserProfile(profile)
        }
    }

}