package gitlab.therealdroid.com.addidaschallenge.ui.profile

import android.graphics.Typeface
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import gitlab.therealdroid.com.addidaschallenge.R
import gitlab.therealdroid.com.addidaschallenge.configuration.device.length
import gitlab.therealdroid.com.addidaschallenge.domain.models.Profile
import jp.wasabeef.picasso.transformations.CropCircleTransformation
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : Fragment(), ProfileContract {

    private var profilePresenter: ProfilePresenter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        profilePresenter = ProfilePresenter(context!!, this)
        return view
    }

    override fun onResume() {
        super.onResume()
        profilePresenter?.fetchUserProfile()
    }

    override fun fetchUserProfile(profile: Profile) {
        val spannable = SpannableString("${profile.pointsRewarded} points earned")

        spannable.setSpan(
            StyleSpan(Typeface.BOLD),
            0, profile.pointsRewarded.length(),
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        Picasso.get().load(profile.photoUrl).placeholder(R.drawable.ic_user_placeholder)
            .transform(CropCircleTransformation()).into(userProfileImage)

        currentRewardedPoints.text = spannable

    }

}