package com.datarockets.mnchkn.data.local

import android.content.Context
import android.content.Intent
import com.datarockets.mnchkn.injection.ApplicationContext
import io.branch.indexing.BranchUniversalObject
import io.branch.referral.util.LinkProperties
import io.reactivex.Observable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SharingHelper
@Inject constructor(@ApplicationContext val context: Context) {

    fun generateShareableIntent(): Observable<Intent> {
        return Observable.create { subscriber ->
            val link = "https://play.google.com/store/apps/details?id=com.datarockets.mnchkn"
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT, link)
            subscriber.onNext(intent)
            subscriber.onComplete()
        }
    }

    fun generateShareableIntent(sharingOptions: SHARE_WITH): Observable<Intent> {
        val linkProperties = LinkProperties().apply {
            feature = "sharing"
            when (sharingOptions) {
                SHARE_WITH.FACEBOOK -> channel = "facebook"
                SHARE_WITH.GMAIL -> channel = "email"
                SHARE_WITH.LINK -> channel = "link"
                SHARE_WITH.TWITTER -> channel = "twitter"
            }
        }
        return Observable.create { subscriber ->
            BranchUniversalObject().generateShortUrl(context, linkProperties) { url, error ->
                val packageManager = context.packageManager
                try {
                    subscriber.onNext(Intent())
                } finally {
                    subscriber.onComplete()
                }
            }
        }
    }

    enum class SHARE_WITH constructor(key: String) {
        FACEBOOK("com.facebook.katana"),
        TWITTER("com.twitter.android"),
        GMAIL("com.google.android.gm"),
        LINK(".mms");

        var appName = ""

        init {
            appName = key
        }

        override fun toString(): String {
            return this.appName
        }
    }
}
