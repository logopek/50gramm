package ni.shikatu.a50gramm.ui.components.types

import androidx.compose.runtime.Immutable
import org.drinkless.tdlib.TdApi

@Immutable
data class MessageWrapper(
	val message: TdApi.Message,
	val bySelf: Boolean,
	val senderName: String,
) {
	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (other !is MessageWrapper) return false
		return message.id == other.message.id
	}

	override fun hashCode(): Int {
		return message.id.hashCode()
	}
}
