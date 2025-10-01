package ni.shikatu.a50gramm

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

abstract class BaseFragment {
	private val ioThread = CoroutineScope(Dispatchers.IO + SupervisorJob())

	private val mainThread = CoroutineScope(Dispatchers.Main)
	open fun onCreate() {}
	@Composable
	abstract fun Present(paddingValues: PaddingValues)

	fun runOnDispatchersThread(block: suspend CoroutineScope.() -> Unit) {
		ioThread.launch(block = block)
	}

	fun runOnUIThread(f: CoroutineScope.() -> Unit){
		mainThread.launch {
			f()
		}
	}

	fun overrideBackPressed(): Boolean { return false; }
}