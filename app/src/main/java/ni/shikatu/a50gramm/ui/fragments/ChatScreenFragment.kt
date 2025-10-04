package ni.shikatu.a50gramm.ui.fragments

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

import ni.shikatu.a50gramm.BaseFragment
import ni.shikatu.a50gramm.BaseModel
import ni.shikatu.a50gramm.tdlib.Tdlib
import ni.shikatu.a50gramm.ui.components.MessageInput
import ni.shikatu.a50gramm.ui.components.MessageView
import ni.shikatu.a50gramm.utlis.ensureType
import org.drinkless.tdlib.TdApi

class ChatScreenFragment(val chat: TdApi.Chat): BaseFragment(), Tdlib.ChatUpdateListener {
	val viewModel = ChatScreenViewModel
	override fun onCreate() {
		viewModel.requestMessages(chat.id)
		requestActionBar()?.setTitle(chat.title)
	}
	@Composable
	override fun Present(paddingValues: PaddingValues) {
		val state = rememberLazyListState()
		val messages = remember {
			derivedStateOf {
				viewModel.messages.value.sortedByDescending { message -> message.date }
			}
		}
		Column(modifier = Modifier.padding(paddingValues)) {
			LazyColumn(state = state, modifier = Modifier.weight(1f).fillMaxSize(), verticalArrangement = Arrangement.spacedBy(4.dp), reverseLayout = true) {
				items(messages.value, key = { it.id }) {
					MessageView(it).create()
				}
			}
			MessageInput(chat){
				viewModel.sendMessage(chat, it)
			}.create()
		}


	}

	override fun onChatNewMessageUpdate(update: TdApi.UpdateNewMessage) {
		if (update.message.chatId == chat.id) {
			viewModel.messages.value = (viewModel.messages.value + update.message)
				.distinctBy { it.id }
		}
	}

	object ChatScreenViewModel: BaseModel(){
		val messages = mutableStateOf(listOf<TdApi.Message>())

		fun requestMessages(chatId: Long, limit: Int = 100, offset: Int = 0){
			messages.value = listOf()
			runOnDispatchersThread {
				//TODO: тдлиб не хочет возвращать больше одного сообщения на первый запрос
				val messagesNew = Tdlib.sendBlocking<TdApi.Messages>(TdApi.GetChatHistory(chatId, 0, 0, limit, false))
				messagesNew.ensureType<TdApi.Messages> { it ->
					messages.value = messages.value + it.messages
				}
			}
		}

		fun sendMessage(chat: TdApi.Chat, content: TdApi.InputMessageContent){
			Tdlib.send(
				TdApi.SendMessage(chat.id, 0, null, null, null, content)
			)
		}
	}
}