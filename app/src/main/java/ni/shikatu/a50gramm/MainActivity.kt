package ni.shikatu.a50gramm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.addCallback
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ni.shikatu.a50gramm.tdlib.Tdlib
import ni.shikatu.a50gramm.ui.components.ActionBarView
import ni.shikatu.a50gramm.ui.fragments.ChatListFragment
import ni.shikatu.a50gramm.ui.fragments.LoginFragment
import ni.shikatu.a50gramm.ui.theme._50grammTheme
import org.drinkless.tdlib.TdApi

class MainActivity : ComponentActivity() {
	private var lastFragment: BaseFragment? = null

	private val fragmentContainer = FragmentContainer()

	private val actionBarView = ActionBarView()
	companion object {
		private var instance: MainActivity? = null
		fun getInstance() = if(instance == null) MainActivity() else instance!!

		private val _fragmentStack = mutableStateListOf<BaseFragment>()
	}

	fun presentFragment(fragment: BaseFragment){
		_fragmentStack.add(fragment)
		if(fragment is Tdlib.UpdateListener){
			Tdlib.subscribe(fragment)
		}
		fragment.onCreate()
		lastFragment = fragment
	}

	fun getActionBarView(): ActionBarView{
		return actionBarView
	}

	fun onBackNavigate(){
		if(_fragmentStack.size > 1){
			_fragmentStack.removeAt(_fragmentStack.size - 1)
			if(lastFragment is Tdlib.UpdateListener){
				Tdlib.unsubscribe(lastFragment!! as Tdlib.UpdateListener)
			}
			lastFragment = _fragmentStack.lastOrNull()
		}
	}
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		instance = this
		enableEdgeToEdge()
		val dispatcher = onBackPressedDispatcher
		dispatcher.addCallback {
			if(!(lastFragment?.overrideBackPressed() ?: false)){
				onBackNavigate()
			}
		}
		setContent {
			val currentFragment by remember { derivedStateOf { _fragmentStack.lastOrNull() } }
			val coroutineScope = rememberCoroutineScope()
			_50grammTheme {
				LaunchedEffect(currentFragment == null) {
					coroutineScope.launch(Dispatchers.IO) {
						val t = Tdlib.sendBlocking<TdApi.AuthorizationState>(TdApi.GetAuthorizationState())
						when(t){
							is TdApi.AuthorizationStateReady -> {
								presentFragment(ChatListFragment())
							}
							else -> {
								presentFragment(LoginFragment())
							}
						}
					}
				}
				Scaffold(modifier = Modifier.fillMaxSize(), topBar = {actionBarView.create()}) { innerPadding ->
					currentFragment?.let {
						fragmentContainer.DrawFragment(currentFragment!!, innerPadding)
					}
				}
			}
		}
	}
}

