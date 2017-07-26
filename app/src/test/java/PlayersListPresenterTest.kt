import com.datarockets.mnchkn.MockModelFabric
import com.datarockets.mnchkn.data.DataManager
import com.datarockets.mnchkn.data.models.Player
import com.datarockets.mnchkn.data.utils.SortType
import com.datarockets.mnchkn.ui.players.PlayersListPresenter
import com.datarockets.mnchkn.ui.players.PlayersListView
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
class PlayersListPresenterTest {

    @Mock lateinit var mMockPlayersListView: PlayersListView
    @Mock lateinit var mMockDataManager: DataManager
    private lateinit var mPresenter: PlayersListPresenter

    @Rule @JvmField
    val mOverrideSchedulersRule = RxSchedulersOverrideRule()

    @Before
    fun setUp() {
        mPresenter = PlayersListPresenter(mMockDataManager)
        mPresenter.attachView(mMockPlayersListView)
    }

    @After
    fun tearDown() {
        mPresenter.detachView()
    }

    @Test
    fun loadPlayers() {
        val players: List<Player> = MockModelFabric.newPlayersList(20)
        stubDataManagerGetPlayers(Observable.just(players))

        mPresenter.getPlayersList()
        verify(mMockPlayersListView).setPlayersList(players)
    }

    fun stubDataManagerGetPlayers(observable: Observable<Any>) {
        doReturn(observable)
                .`when`(mMockDataManager)
                .getPlayers(SortType.NONE)
    }

}