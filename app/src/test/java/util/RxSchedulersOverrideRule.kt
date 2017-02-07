package util

import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement
import rx.Scheduler
import rx.android.plugins.RxAndroidPlugins
import rx.android.plugins.RxAndroidSchedulersHook
import rx.plugins.RxJavaPlugins
import rx.plugins.RxJavaSchedulersHook
import rx.schedulers.Schedulers
import java.lang.reflect.InvocationTargetException

class RxSchedulersOverrideRule : TestRule {

    private val mRxJavaSchedulersHook = object : RxJavaSchedulersHook() {
        override fun getIOScheduler(): Scheduler {
            return Schedulers.immediate()
        }

        override fun getNewThreadScheduler(): Scheduler {
            return Schedulers.immediate()
        }
    }

    private val mRxAndroidSchedulersHook = object : RxAndroidSchedulersHook() {
        override fun getMainThreadScheduler(): Scheduler {
            return Schedulers.immediate()
        }
    }

    @Throws(InvocationTargetException::class, IllegalAccessException::class, NoSuchMethodException::class)
    private fun callResetViaReflectionIn(rxJavaPlugins: RxJavaPlugins) {
        val method = rxJavaPlugins.javaClass.getDeclaredMethod("reset")
        method.isAccessible = true
        method.invoke(rxJavaPlugins)
    }

    override fun apply(base: Statement, description: Description): Statement {
        return object : Statement() {
            @Throws(Throwable::class)
            override fun evaluate() {
                RxAndroidPlugins.getInstance().reset()
                RxAndroidPlugins.getInstance().registerSchedulersHook(mRxAndroidSchedulersHook)
                callResetViaReflectionIn(RxJavaPlugins.getInstance())
                RxJavaPlugins.getInstance().registerSchedulersHook(mRxJavaSchedulersHook)

                base.evaluate()

                RxAndroidPlugins.getInstance().reset()
                callResetViaReflectionIn(RxJavaPlugins.getInstance())
            }
        }
    }


}