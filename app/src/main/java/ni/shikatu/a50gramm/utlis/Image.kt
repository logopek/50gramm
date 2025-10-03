package ni.shikatu.a50gramm.utlis

import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.drinkless.tdlib.TdApi

@Composable
fun rememberConstrainedPhotoSize(photo: TdApi.MessagePhoto): Pair<Dp, Dp> {
	val MAX_SIZE_DP = 320.dp
	val lastSize = photo.photo.sizes.last()
	val density = LocalDensity.current
	val constrainedSizes by remember(lastSize, density) {
		derivedStateOf {
			val originalWidthDp = with(density) { lastSize.width.toDp() }
			val originalHeightDp = with(density) { lastSize.height.toDp() }

			var constrainedWidth = originalWidthDp
			var constrainedHeight = originalHeightDp

			val widthRatio = if (originalWidthDp > MAX_SIZE_DP) MAX_SIZE_DP.value / originalWidthDp.value else 1f
			val heightRatio = if (originalHeightDp > MAX_SIZE_DP) MAX_SIZE_DP.value / originalHeightDp.value else 1f

			val scaleFactor = minOf(widthRatio, heightRatio)

			if (scaleFactor < 1f) {
				constrainedWidth = (originalWidthDp.value * scaleFactor).dp
				constrainedHeight = (originalHeightDp.value * scaleFactor).dp
			}
			Pair(constrainedWidth, constrainedHeight)
		}
	}
	return constrainedSizes
}