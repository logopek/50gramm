package ni.shikatu.a50gramm.ui.components

import androidx.compose.foundation.layout.height
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ni.shikatu.a50gramm.BaseComponent

class ActionBarView: BaseComponent() {
	private val title = mutableStateOf("")
	private val subtitle = mutableStateOf("")
	private val avatar = mutableStateOf("")
	override fun newComponent() {

	}

	fun setTitle(title: String){
		this.title.value = title
	}
	fun setSubtitle(subtitle: String){
		this.subtitle.value = subtitle
	}
	fun setAvatar(avatar: String){
		this.avatar.value = avatar
	}

	@OptIn(ExperimentalMaterial3Api::class)
	@Composable
	override fun Present() {
		val title by this.title
		val subtitle by this.subtitle
		val avatar by this.avatar
		TopAppBar(modifier = Modifier.height(84.dp),
			title = {
				Text(title)
			},
		)
	}
}