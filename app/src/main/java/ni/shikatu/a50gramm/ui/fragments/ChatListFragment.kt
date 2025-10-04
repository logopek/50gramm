package ni.shikatu.a50gramm.ui.fragments

import android.util.Log
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import ni.shikatu.a50gramm.BaseFragment
import ni.shikatu.a50gramm.BaseModel
import ni.shikatu.a50gramm.tdlib.Tdlib
import ni.shikatu.a50gramm.ui.components.ChatDoubleView
import ni.shikatu.a50gramm.utlis.copy
import org.drinkless.tdlib.Client
import org.drinkless.tdlib.TdApi

class ChatListFragment: BaseFragment(), Tdlib.ChatUpdateListener {
	private val viewModel = ChatListViewModel
	override fun onCreate() {
		viewModel.requestChats()
		requestActionBar()
	}

	@Composable
	override fun Present(paddingValues: PaddingValues) {
		actionBar.setTitle("Chat list")
		val state = rememberLazyListState()
		val chats = remember {
			derivedStateOf {
				viewModel.chats.value.sortedByDescending { chat -> chat.lastMessage?.date ?: 0 }
			}
		}

		LazyColumn(modifier = Modifier.padding(paddingValues), state = state) {
			items(chats.value){ chat ->
				ChatDoubleView(chat).create()
				HorizontalDivider(thickness = 1.dp)
			}
		}
	}

	override fun onChatLastMessageUpdate(update: TdApi.UpdateChatLastMessage) {
		runOnUIThread {
			Log.d("ChatListFragment", "Chat last message update: ${update.chatId}")
			val newList = viewModel.chats.value.toMutableList()
			val index = newList.indexOfFirst { it.id == update.chatId }
			if (index >= 0) {
				Log.d("ChatListFragment", "Updating chat: ${update.chatId}")
				newList[index] = newList[index].copy().apply { lastMessage = update.lastMessage }
				viewModel.chats.value = newList
			} else {
				Log.d("ChatListFragment", "Requesting chat: ${update.chatId}")
				viewModel.requestChat(update.chatId)
			}
		}
	}

	object ChatListViewModel: BaseModel() {
		val chats = mutableStateOf(listOf<TdApi.Chat>())

		fun requestChat(chatId: Long, handler: Client.ResultHandler? = null){
			Tdlib.send(TdApi.GetChat(chatId), handler)
		}
		fun requestChats(){
			runOnDispatchersThread {
				val chatIdsResponse = Tdlib.sendBlocking<TdApi.Chats>(TdApi.GetChats(TdApi.ChatListMain(), 1000))
				val deferredChats = chatIdsResponse?.chatIds?.map { chatId ->
					async {
						Tdlib.sendBlocking<TdApi.Chat>(TdApi.GetChat(chatId))
					}
				}
				val newChatList = deferredChats?.awaitAll()?.filterNotNull()
				runOnUIThread {
					chats.value = newChatList ?: listOf()
				}
			}

		}
	}
}