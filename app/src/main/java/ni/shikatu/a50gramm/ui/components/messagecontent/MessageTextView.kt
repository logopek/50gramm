package ni.shikatu.a50gramm.ui.components.messagecontent

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import ni.shikatu.a50gramm.BaseComponent
import ni.shikatu.a50gramm.utlis.toAnnotatedString
import org.drinkless.tdlib.TdApi

class MessageTextView(val text: TdApi.MessageText): BaseComponent() {
	@Composable
	override fun Present() {
		Text(text.text.toAnnotatedString())
	}
}