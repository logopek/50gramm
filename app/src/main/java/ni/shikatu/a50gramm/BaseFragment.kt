package ni.shikatu.a50gramm

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import ni.shikatu.a50gramm.ui.components.ActionBarView

abstract class BaseFragment: BaseModel() {
	var actionBar = MainActivity.getInstance().getActionBarView()

	fun requestActionBar(): ActionBarView? {
		actionBar = MainActivity.getInstance().getActionBarView()
		return actionBar
	}
	open fun onCreate() {}
	@Composable
	abstract fun Present(paddingValues: PaddingValues)

	fun overrideBackPressed(): Boolean { return false; }

}