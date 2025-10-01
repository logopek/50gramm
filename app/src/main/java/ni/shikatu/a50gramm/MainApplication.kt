package ni.shikatu.a50gramm

import android.app.Application
import ni.shikatu.a50gramm.tdlib.Tdlib

class MainApplication: Application() {
	override fun onCreate() {
		Tdlib.startClient(applicationContext)
		super.onCreate()
	}
}