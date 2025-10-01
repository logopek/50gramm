package ni.shikatu.a50gramm.ui.components

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import ni.shikatu.a50gramm.BaseComponent
import ni.shikatu.a50gramm.tdlib.Tdlib
import org.drinkless.tdlib.TdApi

/**
 * Used in ChatListFragment, represents a single chat in list
 * @see ChatScreenFragment for full chat screen
 */
class ChatDoubleView(val chat: TdApi.Chat): BaseComponent() {
	private val chatId = chat.id

	private val title = mutableStateOf(chat.title)
	private val lastMessage = mutableStateOf(chat.lastMessage)
	override fun newComponent() {}

	@Composable
	override fun Present() {
		Row {
			Column {
				Text(title.value)
				lastMessage.value?.let {
					MessageCompactContent(lastMessage.value!!.content)
				}
			}

		}
	}
}