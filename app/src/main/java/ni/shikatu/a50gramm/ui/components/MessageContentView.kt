package ni.shikatu.a50gramm.ui.components

import androidx.compose.runtime.Composable
import ni.shikatu.a50gramm.BaseComponent
import ni.shikatu.a50gramm.ui.components.messagecontent.MessagePhotoView
import ni.shikatu.a50gramm.ui.components.messagecontent.MessageTextView
import ni.shikatu.a50gramm.ui.components.messagecontent.MessageVideoView
import org.drinkless.tdlib.TdApi

class MessageContentView(val message: TdApi.Message): BaseComponent() {

	@Composable
	override fun Present() {
		when(message.content){
			is TdApi.MessageText -> {
				MessageTextView(message.content as TdApi.MessageText).create()
			}
			is TdApi.MessagePhoto -> {
				MessagePhotoView(message.content as TdApi.MessagePhoto).create()
			}
			is TdApi.MessageVideo -> {
				MessageVideoView(message.content as TdApi.MessageVideo).create()
			}
		}

	}

}