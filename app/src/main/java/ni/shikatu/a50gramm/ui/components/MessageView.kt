package ni.shikatu.a50gramm.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import ni.shikatu.a50gramm.BaseComponent
import ni.shikatu.a50gramm.ui.components.types.MessageWrapper

class MessageView(val messageWrapper: MessageWrapper): BaseComponent() {

	override fun newComponent() {

	}
	@Composable
	override fun Present() {
		val selfColor = MaterialTheme.colorScheme.primaryContainer
		val otherColor =  MaterialTheme.colorScheme.surfaceContainerHigh
		val background = if(messageWrapper.bySelf) selfColor else otherColor
		Box(modifier = Modifier.fillMaxWidth(), contentAlignment = if(messageWrapper.bySelf) Alignment.CenterEnd else Alignment.CenterStart){
			Column(modifier = Modifier.clip(RoundedCornerShape(16.dp)).background(background).padding(8.dp).widthIn(max = 280.dp)) {
				Text(messageWrapper.senderName)
				MessageContentView(messageWrapper.message).create()
			}
		}
	}

}