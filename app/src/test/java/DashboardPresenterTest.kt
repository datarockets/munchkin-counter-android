import com.datarockets.mnchknlvlcntr.MockModelFabric
import com.datarockets.mnchknlvlcntr.data.DataManager
import com.datarockets.mnchknlvlcntr.data.utils.SortType
import com.datarockets.mnchknlvlcntr.ui.dashboard.DashboardPresenter
import com.datarockets.mnchknlvlcntr.ui.dashboard.DashboardView
import io.reactivex.Observable
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.doReturn
import org.mockito.Mockito.verify
import org.mockito.runners.MockitoJUnitRunner
import util.RxSchedulersOverrideRule

@RunWith(MockitoJUnitRunner::class)
class DashboardPresenterTest {

    @Mock lateinit var mMockDashboardView: DashboardView
    @Mock lateinit var mMockDataManager: DataManager
    private lateinit var mPresenter: DashboardPresenter

    @Rule @JvmField val mOverrideSchedulersRule = RxSchedulersOverrideRule()

    @Before
    fun setUp() {
        mPresenter = DashboardPresenter(mMockDataManager)
        mPresenter.attachView(mMockDashboardView)
    }

    @After
    fun tearDown() {
        mPresenter.detachView()
    }

    @Test
    fun loadPlayingPlayers() {
        val playingPlayers = MockModelFabric.newPlayersList(20)
        stubLoadPlayingPlayers(Observable.just(playingPlayers))

        mPresenter.getPlayingPlayers()
        verify(mMockDashboardView).setPlayers(playingPlayers)
    }

    fun stubLoadPlayingPlayers(observable: Observable<Any>) {
        doReturn(observable)
                .`when`(mMockDataManager)
                .getPlayers(SortType.POSITION)
    }

}