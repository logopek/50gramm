package ni.shikatu.a50gramm

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable

class FragmentContainer {
	@Composable
	fun DrawFragment(fragment: BaseFragment, paddingValues: PaddingValues){
		fragment.Present(paddingValues)
	}
}