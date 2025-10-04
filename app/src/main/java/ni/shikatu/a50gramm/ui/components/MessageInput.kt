package ni.shikatu.a50gramm.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import ni.shikatu.a50gramm.BaseComponent
import ni.shikatu.a50gramm.R
import org.drinkless.tdlib.TdApi

class MessageInput(val chat: TdApi.Chat, val threadId: Long = 0, val onSend: (TdApi.InputMessageContent) -> Unit): BaseComponent() {
	val text = mutableStateOf("")
	override fun newComponent() {
		getDraft()
	}

	@Composable
	override fun Present() {
		val interactionSource = remember { MutableInteractionSource() }
		val onAttachClick = null


		Row(
			modifier = Modifier
				.fillMaxWidth()
				.padding(horizontal = 8.dp, vertical = 4.dp),
			verticalAlignment = Alignment.Bottom
		) {
			if (onAttachClick != null) {
				IconButton(
					onClick = onAttachClick,
					modifier = Modifier
						.size(48.dp)
						.padding(4.dp)
				) {
					Icon(
						painter = painterResource(R.drawable.attach_file_24dp),
						contentDescription = "Прикрепить файл",
						tint = MaterialTheme.colorScheme.primary
					)
				}
			}

			TextField(
				value = text.value,
				onValueChange = {text.value = it},
				modifier = Modifier
					.weight(1f)
					.heightIn(min = 48.dp, max = 120.dp),
				placeholder = {
					Text(
						text = "Message",
						style = MaterialTheme.typography.bodyLarge
					)
				},
				enabled = true,
				textStyle = MaterialTheme.typography.bodyLarge,
				colors = TextFieldDefaults.colors(
					focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
					unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
					disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
					focusedIndicatorColor = Color.Transparent,
					unfocusedIndicatorColor = Color.Transparent,
					disabledIndicatorColor = Color.Transparent
				),
				shape = RoundedCornerShape(24.dp),
				maxLines = 5,
				keyboardOptions = KeyboardOptions(
					imeAction = ImeAction.Send
				),
				interactionSource = interactionSource
			)

			Spacer(modifier = Modifier.width(4.dp))
			AnimatedContent(
				targetState = text.value.isNotBlank(),
				transitionSpec = {
					(scaleIn() + fadeIn()).togetherWith(scaleOut() + fadeOut())
				},
				modifier = Modifier.size(48.dp),
				label = "ButtonSwitch"
			) { hasText ->
				if (hasText) {
					IconButton(
						onClick = {onSend(generateInputContent()); text.value = ""},
						modifier = Modifier
							.fillMaxSize()
							.clip(CircleShape)
							.background(MaterialTheme.colorScheme.primary),
						enabled = text.value.isNotBlank()
					) {
						Icon(
							painter = painterResource(R.drawable.send_24dp),
							contentDescription = "Отправить",
							tint = MaterialTheme.colorScheme.onPrimary
						)
					}
				} else {
					IconButton(
						onClick = { /* TODO: Voice message */ },
						modifier = Modifier.fillMaxSize()
					) {
						Icon(
							painter = painterResource(R.drawable.mic_24dp),
							contentDescription = "Голосовое сообщение",
							tint = MaterialTheme.colorScheme.primary
						)
					}
				}
			}
		}

	}

	fun generateInputContent(): TdApi.InputMessageContent{
		return TdApi.InputMessageText(TdApi.FormattedText(text.value, null), null, true)
	}

	fun getDraft(){
		when(chat.draftMessage?.inputMessageText) {
			is TdApi.InputMessageText -> {
				text.value =
					(chat.draftMessage!!.inputMessageText as TdApi.InputMessageText).text.text
			}
		}
	}
}