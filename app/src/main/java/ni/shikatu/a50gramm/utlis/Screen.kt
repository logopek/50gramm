package ni.shikatu.a50gramm.utlis

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
@Composable
fun Int.pxToDp(): Dp {
	val density = LocalDensity.current
	return with(density) {
		this@pxToDp.toFloat().toDp()
	}
}