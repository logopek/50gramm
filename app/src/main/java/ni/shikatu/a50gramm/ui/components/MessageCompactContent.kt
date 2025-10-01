package ni.shikatu.a50gramm.ui.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import org.drinkless.tdlib.TdApi

@Composable
fun StyledSmallText(text: String, overflow: TextOverflow? = null){
	Text(text, fontSize = 12.sp, overflow = overflow ?: TextOverflow.Clip, maxLines = 1)
}
@Composable
fun MessageCompactContent(content: TdApi.MessageContent) {
	when(content){
		is TdApi.MessageText -> {
			StyledSmallText("${content.text.text}", overflow = TextOverflow.Ellipsis)
		}
		is TdApi.MessagePhoto -> {
			StyledSmallText("Photo")
		}
		is TdApi.MessageVideo -> {
			StyledSmallText("Video")
		}
		is TdApi.MessageAnimation -> {
			StyledSmallText("Animation")
		}
		is TdApi.MessageAudio -> {
			StyledSmallText("Audio")
		}
		is TdApi.MessageDocument -> {
			StyledSmallText("Document")
		}
		is TdApi.MessageContact -> {
			StyledSmallText("Contact")
		}
		is TdApi.MessageLocation -> {
			StyledSmallText("Location")
		}
		is TdApi.MessageVenue -> {
			StyledSmallText("Venue")
		}
		is TdApi.MessageInvoice -> {
			StyledSmallText("Invoice")
		}
		is TdApi.MessageGame -> {
			StyledSmallText("Game")
		}

	}
}