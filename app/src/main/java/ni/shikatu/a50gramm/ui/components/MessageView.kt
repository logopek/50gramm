package ni.shikatu.a50gramm.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import ni.shikatu.a50gramm.BaseComponent
import ni.shikatu.a50gramm.tdlib.Tdlib
import org.drinkless.tdlib.TdApi

class MessageView(val message: TdApi.Message): BaseComponent() {

	val senderName = mutableStateOf("")
	override fun newComponent() {
		setSenderName()
	}
	@Composable
	override fun Present() {
		Box(modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp)).background(MaterialTheme.colorScheme.surfaceContainerHigh).padding(8.dp)){
			Column {
				Text(senderName.value)
				MessageContentView(message).create()
			}
		}
	}

	fun setSenderName(){
		when(message.senderId){
			is TdApi.MessageSenderUser -> {
				runOnDispatchersThread {
					val userId = (message.senderId as TdApi.MessageSenderUser).userId
					val user = Tdlib.sendBlocking<TdApi.User>(TdApi.GetUser(userId))
					runOnUIThread {
						senderName.value = user?.firstName ?: "Unknown"
					}
				}
			}
			is TdApi.MessageSenderChat -> {
				runOnDispatchersThread {
					val chatId = (message.senderId as TdApi.MessageSenderChat).chatId
					val chat = Tdlib.sendBlocking<TdApi.Chat>(TdApi.GetChat(chatId))
					runOnUIThread {
						senderName.value = chat?.title ?: "Unknown"
					}
				}
			}
		}
	}
}