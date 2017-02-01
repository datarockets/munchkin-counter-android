import com.datarockets.mnchkn.data.DataManager
import com.datarockets.mnchkn.data.local.DatabaseHelper
import com.datarockets.mnchkn.data.local.PreferencesHelper
import org.junit.Before
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.runners.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class DataManagerTest {

    @Mock lateinit var mMockPreferencesHelper: PreferencesHelper
    @Mock lateinit var mMockDatabaseHelper: DatabaseHelper
    private lateinit var mDataManager: DataManager

    @Before
    fun setUp() {
        mDataManager = DataManager(mMockDatabaseHelper, mMockPreferencesHelper)
    }

}