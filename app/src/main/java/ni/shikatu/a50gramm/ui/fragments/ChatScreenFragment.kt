package ni.shikatu.a50gramm.ui.fragments

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ni.shikatu.a50gramm.BaseFragment
import ni.shikatu.a50gramm.BaseModel
import ni.shikatu.a50gramm.tdlib.Tdlib
import ni.shikatu.a50gramm.ui.components.MessageInput
import ni.shikatu.a50gramm.ui.components.MessageView
import ni.shikatu.a50gramm.ui.components.types.MessageWrapper
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
		Column(modifier = Modifier.padding(paddingValues)) {
			LazyColumn(state = state, modifier = Modifier.weight(1f).fillMaxSize(), verticalArrangement = Arrangement.spacedBy(4.dp), reverseLayout = true) {
				items(viewModel.messages, key = { it.message.id }, contentType = {"message"}) {
					val messageView = remember(it.message.id) {
						MessageView(it).createWithoutPresent()
					}
					Box(Modifier.animateItem()){
						messageView.create()
					}
				}
			}
			MessageInput(chat){
				viewModel.sendMessage(chat, it)
			}.create()
		}


	}



	override fun onChatNewMessageUpdate(update: TdApi.UpdateNewMessage) {
		update.message.ensureType<TdApi.Message> { message ->
			runOnDispatchersThread {
				viewModel.addMessage(message, chat.id)
			}

		}
	}

	object ChatScreenViewModel: BaseModel(){
		val messages = mutableStateListOf<MessageWrapper>()

		fun addMessage(message: TdApi.Message, chatId: Long){
			runOnDispatchersThread {
				if (message.chatId != chatId) return@runOnDispatchersThread
				if (messages.any { it.message.id == message.id }) return@runOnDispatchersThread

				val senderName = when (message.senderId) {
					is TdApi.MessageSenderUser -> {
						Tdlib.sendBlocking<TdApi.User>(
							TdApi.GetUser((message.senderId as TdApi.MessageSenderUser).userId)
						)?.firstName ?: ""
					}
					is TdApi.MessageSenderChat -> {
						Tdlib.sendBlocking<TdApi.Chat>(
							TdApi.GetChat((message.senderId as TdApi.MessageSenderChat).chatId)
						)?.title ?: ""
					}
					else -> ""
				}
				val bySelf = message.senderId is TdApi.MessageSenderUser &&
						(message.senderId as TdApi.MessageSenderUser).userId == Tdlib.me?.id

				runOnUIThread {
					messages.add(0, MessageWrapper(message, bySelf, senderName))
				}
			}
		}
		fun requestMessages(chatId: Long, limit: Int = 100){
			messages.clear()
			runOnDispatchersThread {
				val messagesNew = Tdlib.sendBlocking<TdApi.Messages>(
					TdApi.GetChatHistory(chatId, 0, 0, limit, false)
				)
				messagesNew.ensureType<TdApi.Messages> { messagesResult ->
					val messageWrappers = messagesResult.messages.map { msg ->
						val senderName = when (msg.senderId) {
							is TdApi.MessageSenderUser -> {
								Tdlib.sendBlocking<TdApi.User>(
									TdApi.GetUser((msg.senderId as TdApi.MessageSenderUser).userId)
								)?.firstName ?: ""
							}
							is TdApi.MessageSenderChat -> {
								Tdlib.sendBlocking<TdApi.Chat>(
									TdApi.GetChat((msg.senderId as TdApi.MessageSenderChat).chatId)
								)?.title ?: ""
							}
							else -> ""
						}
						val bySelf = msg.senderId is TdApi.MessageSenderUser &&
								(msg.senderId as TdApi.MessageSenderUser).userId == Tdlib.me?.id
						MessageWrapper(msg, bySelf, senderName)
					}.sortedByDescending { it.message.date }

					runOnUIThread {
						messages.addAll(messageWrappers)
					}
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