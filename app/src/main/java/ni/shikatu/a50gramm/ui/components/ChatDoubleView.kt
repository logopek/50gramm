package ni.shikatu.a50gramm.ui.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import ni.shikatu.a50gramm.BaseComponent
import ni.shikatu.a50gramm.tdlib.Tdlib
import ni.shikatu.a50gramm.ui.fragments.ChatScreenFragment
import org.drinkless.tdlib.TdApi

/**
 * Used in ChatListFragment, represents a single chat in list
 * @see ChatScreenFragment for full chat screen
 */
class ChatDoubleView(val chat: TdApi.Chat): BaseComponent() {

	private val title = mutableStateOf(chat.title)
	private val lastMessage = mutableStateOf(chat.lastMessage)

	@Composable
	override fun Present() {
		Row(modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.surfaceContainerLow).padding(8.dp).clickable{
			val chatFragment = ChatScreenFragment(chat)
			presentFragment(chatFragment)
		}) {
			Column {
				Text(title.value)
				lastMessage.value?.let {
					MessageCompactContent(lastMessage.value!!.content)
				}
			}

		}
	}
}