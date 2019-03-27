package gitlab.therealdroid.com.addidaschallenge.ui.profile

import gitlab.therealdroid.com.addidaschallenge.domain.models.Profile

interface ProfileContract {
    fun fetchUserProfile(profile: Profile)
}