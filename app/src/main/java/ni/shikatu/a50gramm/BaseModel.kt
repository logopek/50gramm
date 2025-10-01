package ni.shikatu.a50gramm

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

abstract class BaseModel {
	private val ioThread = CoroutineScope(Dispatchers.IO + SupervisorJob())

	private val mainThread = CoroutineScope(Dispatchers.Main)

	fun runOnDispatchersThread(block: suspend CoroutineScope.() -> Unit) {
		ioThread.launch(block = block)
	}

	fun runOnUIThread(f: CoroutineScope.() -> Unit){
		mainThread.launch {
			f()
		}
	}
}