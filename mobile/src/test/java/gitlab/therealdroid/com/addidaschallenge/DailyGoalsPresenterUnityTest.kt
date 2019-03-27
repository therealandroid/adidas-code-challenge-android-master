package gitlab.therealdroid.com.addidaschallenge

import android.content.Context
import android.support.test.runner.AndroidJUnit4
import gitlab.therealdroid.com.addidaschallenge.configuration.ProjectConfiguration
import gitlab.therealdroid.com.addidaschallenge.ui.goals.DailyGoalsListener
import gitlab.therealdroid.com.addidaschallenge.ui.goals.DailyGoalsPresenter
import gitlab.therealdroid.com.addidaschallenge.ui.goals.StepsListener
import io.realm.Realm
import io.realm.RealmConfiguration
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.*
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

@RunWith(AndroidJUnit4::class)
class DailyGoalsPresenterUnityTest {

    private var presenter: DailyGoalsPresenter? = null

    @Mock
    private var context: Context? = null

    @Mock
    private var stepsListener: StepsListener? = null

    @Mock
    private var dailyGoalsListener: DailyGoalsListener? = null

    @BeforeClass
    @Throws(Exception::class)
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        Mockito.verify(context, isNotNull())

        Realm.init(context)
        Realm.setDefaultConfiguration(
            RealmConfiguration.Builder()
                .name(ProjectConfiguration.DB_NAME)
                .deleteRealmIfMigrationNeeded()
                .schemaVersion(1)
                .build()
        )

        // Make presenter a mock while using mock repository and viewContract created above
        presenter = Mockito.spy<DailyGoalsPresenter>(DailyGoalsPresenter(context!!, stepsListener, dailyGoalsListener))

    }

    @Test
    fun test_realm_initialization_success() {
        var realm = Realm.getDefaultInstance()
        Mockito.verify(realm, isNotNull())
    }

}