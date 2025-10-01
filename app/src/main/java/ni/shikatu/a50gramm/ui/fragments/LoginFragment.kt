package ni.shikatu.a50gramm.ui.fragments

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.lifecycle.ViewModel
import ni.shikatu.a50gramm.BaseFragment
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import ni.shikatu.a50gramm.BaseModel
import ni.shikatu.a50gramm.MainActivity
import ni.shikatu.a50gramm.tdlib.Tdlib
import org.drinkless.tdlib.TdApi
import org.drinkless.tdlib.TdApi.*

class LoginFragment: BaseFragment(), Tdlib.AuthorizationListener {
	private val viewModel = LoginViewModel


	enum class LOGIN_STATE {
		PHONE, CODE, TWOFA, READY
	}
	@Composable
	override fun Present(paddingValues: PaddingValues) {
		val viewModel = LoginViewModel
		var code by viewModel.code
		var password by viewModel.password
		var phoneNumber by viewModel.phoneNumber
		var loginState by viewModel.loginState
		Row(modifier = Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically) {
			Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
				when (loginState) {
					LOGIN_STATE.PHONE -> {
						OutlinedTextField(phoneNumber, onValueChange = {phoneNumber = it}, label = {Text("Phone Number")})
					}
					LOGIN_STATE.CODE -> {
						OutlinedTextField(code, onValueChange = {code = it}, label = {Text("Code")})
					}
					LOGIN_STATE.TWOFA -> {
						OutlinedTextField(password, onValueChange = {password = it}, label = {Text("Password")})
					}
					LOGIN_STATE.READY -> {
						MainActivity.getInstance().presentFragment(ChatListFragment())
					}

				}
				Button(onClick = { viewModel.send() }) {
					Text("Отправить")
				}
			}
		}
	}

	override fun onAuthorizationState(update: TdApi.UpdateAuthorizationState) {
		Log.d("TDLib", "[LoginFragment]Authorization State Changed: ${update.authorizationState.javaClass.simpleName}")
		when(update.authorizationState){
			is TdApi.AuthorizationStateWaitPhoneNumber -> {
				viewModel.loginState.value = LOGIN_STATE.PHONE
			}
			is TdApi.AuthorizationStateWaitCode -> {
				viewModel.loginState.value = LOGIN_STATE.CODE
			}
			is TdApi.AuthorizationStateWaitPassword -> {
				viewModel.loginState.value = LOGIN_STATE.TWOFA
			}
			is TdApi.AuthorizationStateReady -> {
				viewModel.loginState.value = LOGIN_STATE.READY
			}
		}
	}

	object LoginViewModel: BaseModel() {
		public var loginState = mutableStateOf(LOGIN_STATE.PHONE)
		public val phoneNumber = mutableStateOf("")
		public val code = mutableStateOf("")
		public val password = mutableStateOf("")

		fun send(){
			when (loginState.value) {
				LOGIN_STATE.PHONE -> {
					Log.d("LoginViewModel", "Sending phone number: ${phoneNumber.value}")
					Tdlib.send(SetAuthenticationPhoneNumber(phoneNumber.value, null))
				}
				LOGIN_STATE.CODE -> {
					Tdlib.send(CheckAuthenticationCode(code.value))
				}
				LOGIN_STATE.TWOFA -> {
					Tdlib.send(CheckAuthenticationPassword(password.value))
				}
				LOGIN_STATE.READY -> {
					MainActivity.getInstance().presentFragment(ChatListFragment())
				}
			}
		}
	}
}