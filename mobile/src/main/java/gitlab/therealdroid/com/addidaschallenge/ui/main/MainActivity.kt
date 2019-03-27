package gitlab.therealdroid.com.addidaschallenge.ui.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.Scopes
import com.google.android.gms.fitness.Fitness
import com.google.android.gms.fitness.FitnessOptions
import com.google.android.gms.fitness.data.DataType
import com.squareup.picasso.Picasso
import gitlab.therealdroid.com.addidaschallenge.R
import gitlab.therealdroid.com.addidaschallenge.configuration.ProjectConfiguration
import gitlab.therealdroid.com.addidaschallenge.configuration.device.hasInternetConnection
import gitlab.therealdroid.com.addidaschallenge.configuration.device.openActivity
import gitlab.therealdroid.com.addidaschallenge.configuration.device.showSnack
import gitlab.therealdroid.com.addidaschallenge.domain.models.Profile
import gitlab.therealdroid.com.addidaschallenge.ui.goals.DailyGoalsFragment
import gitlab.therealdroid.com.addidaschallenge.ui.profile.ProfileContract
import gitlab.therealdroid.com.addidaschallenge.ui.profile.ProfilePresenter
import gitlab.therealdroid.com.addidaschallenge.ui.rewards.RewardsActivity
import jp.wasabeef.picasso.transformations.CropCircleTransformation
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.empty_view.*
import kotlinx.android.synthetic.main.menu_rewards.*

class MainActivity : AppCompatActivity(), ProfileContract, DailyGoalsFragment.GoalsReachedListener {

    private val GOOGLE_FIT_PERMISSIONS_REQUEST_CODE = 0x1001

    var profilePresenter = ProfilePresenter(this, this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupView()
        setupGoogleFit()
        setupListeners()
        profilePresenter.fetchUserProfile()
    }

    private fun setupView() {
        setSupportActionBar(toolbar)
        toolbarTitle?.text = resources.getString(R.string.TOOLBAR_daily_challenge_title)
    }

    private fun setupListeners() {
        profileAndRewards.setOnClickListener {
            openActivity(RewardsActivity::class.java)
        }
    }

    private fun setupGoogleFit() {
        val fitnessOptions = FitnessOptions.builder()
            .addDataType(DataType.TYPE_DISTANCE_CUMULATIVE, FitnessOptions.ACCESS_WRITE)
            .addDataType(DataType.TYPE_DISTANCE_DELTA, FitnessOptions.ACCESS_WRITE)
            .addDataType(DataType.TYPE_STEP_COUNT_CUMULATIVE, FitnessOptions.ACCESS_WRITE)
            .addDataType(DataType.TYPE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_WRITE)
            .addDataType(DataType.AGGREGATE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_WRITE)
            .build()

        if (!GoogleSignIn.hasPermissions(GoogleSignIn.getLastSignedInAccount(this), fitnessOptions)) {
            GoogleSignIn.requestPermissions(
                this,
                GOOGLE_FIT_PERMISSIONS_REQUEST_CODE,
                GoogleSignIn.getLastSignedInAccount(this),
                fitnessOptions
            )
            //Check onResult
            //to make sure login was successfully
            //then proceed with your app stuffs
        } else {
            //signIn was made correctly
            subscribeRecording()
            displayFragment()
        }
    }

    private fun displayFragment() {
        supportFragmentManager.beginTransaction()
            .add(R.id.goalsFragment, DailyGoalsFragment(), DailyGoalsFragment::class.java.simpleName)
            .commit()
    }

    /**
     * Subscribe to record user steps
     *
     */
    private fun subscribeRecording() {
        if (GoogleSignIn.getLastSignedInAccount(this) != null) {
            Fitness.getRecordingClient(this, GoogleSignIn.getLastSignedInAccount(this)!!)
                .subscribe(DataType.TYPE_STEP_COUNT_CUMULATIVE)
                .addOnSuccessListener {
                    Log.d(ProjectConfiguration.TAG, "Successfully subscribed!")
                }
                .addOnFailureListener { e ->
                    Log.d(ProjectConfiguration.TAG, e.stackTrace.toString())
                }
        } else {
            //No account found
            showSnack("You must be signed with Google")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == GOOGLE_FIT_PERMISSIONS_REQUEST_CODE) {
                subscribeRecording()
                displayFragment()
            }
        } else if (!hasInternetConnection()) { //Internet connection could be off
            Picasso.get().load(R.drawable.ic_no_connection).into(emptyImage)
            emptyView.visibility = View.VISIBLE
            emptyTitle.text = getString(R.string.empty_view_no_connection_title)
            emptyDescription.text = getString(R.string.empty_view_no_connection_description)
        }
    }

    override fun fetchUserProfile(profile: Profile) {
        Picasso.get().load(profile.photoUrl).placeholder(R.drawable.ic_user_placeholder)
            .transform(CropCircleTransformation()).into(userProfileImage)
        userProfileName.text = profile.username

        userPointsRewarded.text = ("${profile.pointsRewarded} pts")
    }


    override fun onAttachFragment(fragment: Fragment?) {
        super.onAttachFragment(fragment)
        if (fragment is DailyGoalsFragment) {
            fragment.setGoalReachedListener(this)
        }
    }

    override fun onGoalsReached() {
        //Update menu info when earn new points
        profilePresenter.fetchUserProfile()
    }

}
