package ni.shikatu.a50gramm.tdlib


import android.content.Context
import android.os.Build
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import ni.shikatu.a50gramm.BuildConfig
import org.drinkless.tdlib.Client
import org.drinkless.tdlib.TdApi
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

object Tdlib {
	private val apiId = BuildConfig.API_ID
	private val apiHash = BuildConfig.API_HASH

	private var client: Client? = null
	private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
	private val listeners = mutableListOf<UpdateListener>()

	fun subscribe(listener: UpdateListener){
		Log.d("TDLib", "Subscribing listener: ${listener.javaClass.simpleName}")
		listeners.add(listener)
	}
	fun unsubscribe(listener: UpdateListener){
		Log.d("TDLib", "Unsubscribing listener: ${listener.javaClass.simpleName}")
		listeners.remove(listener)
	}

	private val _updates = MutableSharedFlow<TdApi.Object>()
	val updatesFlow = _updates.asSharedFlow()

	fun startClient(context: Context) {
		if (client == null) {
			Log.d("TDLib", "Starting client initialization...")
			client = Client.create({ obj ->
				//Log.d("TDLib", "Received update: ${obj.javaClass.simpleName}")
				scope.launch { _updates.emit(obj) }
				if(obj is TdApi.Update){
					//Log.d("TDLib", "Received update: ${obj.javaClass.simpleName}")
					for(listener in listeners){
						//Log.d("TDLib", "Notifying listener: ${listener.javaClass.simpleName}")
						listener.onUpdate(obj)
					}
				}

				if (obj is TdApi.UpdateAuthorizationState) {
					Log.d("TDLib", "Authorization State Changed: ${obj.authorizationState.javaClass.simpleName}")
				}
			}, null, null)

			send(TdApi.SetLogStream(TdApi.LogStreamFile(context.filesDir.absolutePath + "/tdlog.txt", 1024 * 1024 * 5, false))) {
				Log.d("TDLib", "SetLogStream result: $it")
			}
			send(TdApi.SetLogVerbosityLevel(1)) {
				Log.d("TDLib", "SetLogVerbosityLevel result: $it")
			}
			send(
				TdApi.SetTdlibParameters(
					false,
					context.filesDir.absolutePath + "/tdlib",
					context.filesDir.absolutePath + "/tdlib",
					null,
					true,
					true,
					true,
					false,
					apiId,
					apiHash,
					"en",
					"${Build.MANUFACTURER} ${Build.MODEL}",
					Build.VERSION.RELEASE,
					"1.0",
				)
			) {
				Log.d("TDLib", "SetTdlibParameters result: $it")
				if (it is TdApi.Ok) {
					Log.d("TDLib", "TDLib parameters successfully set. Ready for authentication.")
				} else {
					Log.e("TDLib", "Failed to set TDLib parameters: $it")
				}
			}

		}
	}

	fun send(query: TdApi.Function<*>, handler: Client.ResultHandler? = null) {
		scope.launch {
			Log.d("TDLib", "Sending query: ${query.javaClass.simpleName}")
			client?.send(query, handler ?: Client.ResultHandler { result ->
				Log.v("TDLib", "Received default handler result for ${query.javaClass.simpleName}: $result")
			})
		}
	}

	@OptIn(ExperimentalCoroutinesApi::class)
	suspend fun <T : TdApi.Object> sendBlocking(query: TdApi.Function<*>): T =
		suspendCancellableCoroutine { cont ->
			client?.send(query, { result ->
				if (!cont.isCompleted) {
					@Suppress("UNCHECKED_CAST")
					cont.resume(result as T)
				}
			}, { error ->
				if (!cont.isCompleted) {
					cont.resumeWithException(RuntimeException(error.message))
				}
			})
		}

	interface UpdateListener {
		fun onUpdate(update: TdApi.Update)
	}

	interface AuthorizationListener: UpdateListener {
		fun onAuthorizationState(update: TdApi.UpdateAuthorizationState) {}
		override fun onUpdate(update: TdApi.Update) {
			if(update is TdApi.UpdateAuthorizationState){
				onAuthorizationState(update)
			}
		}
	}

	interface ChatUpdateListener: UpdateListener{
		fun onChatUpdate(update: TdApi.UpdateNewChat) {}
		fun onChatLastMessageUpdate(update: TdApi.UpdateChatLastMessage) {}
		fun onChatNewMessageUpdate(update: TdApi.UpdateNewMessage) {}
		fun onChatTitleUpdate(update: TdApi.UpdateChatTitle) {}
		override fun onUpdate(update: TdApi.Update) {
			when(update){
				is TdApi.UpdateNewChat -> onChatUpdate(update)
				is TdApi.UpdateChatLastMessage -> onChatLastMessageUpdate(update)
				is TdApi.UpdateNewMessage -> onChatNewMessageUpdate(update)
				is TdApi.UpdateChatTitle -> onChatTitleUpdate(update)
			}
		}

	}


}