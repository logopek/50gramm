package ni.shikatu.a50gramm.utlis

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.sp
import org.drinkless.tdlib.TdApi

fun TdApi.FormattedText.toAnnotatedString(): AnnotatedString = buildAnnotatedString {
	val text = this@toAnnotatedString.text
	val entities = this@toAnnotatedString.entities.sortedBy { it.offset }
	var currentOffset = 0

	for (entity in entities) {
		val start = entity.offset
		val end = entity.offset + entity.length
		if (start > currentOffset) {
			append(text.substring(currentOffset, start))
		}

		val entityText = text.substring(start, end)

		val annotationStart = length

		append(entityText)

		val annotationEnd = length

		when (entity.type.constructor) {
			TdApi.TextEntityTypeBold.CONSTRUCTOR -> {
				addStyle(SpanStyle(fontWeight = FontWeight.Bold), annotationStart, annotationEnd)
			}
			TdApi.TextEntityTypeItalic.CONSTRUCTOR -> {
				addStyle(SpanStyle(fontStyle = FontStyle.Italic), annotationStart, annotationEnd)
			}
			TdApi.TextEntityTypeUnderline.CONSTRUCTOR -> {
				addStyle(SpanStyle(textDecoration = TextDecoration.Underline), annotationStart, annotationEnd)
			}
			TdApi.TextEntityTypeStrikethrough.CONSTRUCTOR -> {
				addStyle(SpanStyle(textDecoration = TextDecoration.LineThrough), annotationStart, annotationEnd)
			}
			TdApi.TextEntityTypeSpoiler.CONSTRUCTOR -> {
				addStyle(SpanStyle(background = Color.LightGray), annotationStart, annotationEnd)
			}
			TdApi.TextEntityTypeCode.CONSTRUCTOR,
			TdApi.TextEntityTypePre.CONSTRUCTOR,
			TdApi.TextEntityTypePreCode.CONSTRUCTOR -> {
				addStyle(SpanStyle(fontSize = 14.sp), annotationStart, annotationEnd)
			}
			TdApi.TextEntityTypeBlockQuote.CONSTRUCTOR,
			TdApi.TextEntityTypeExpandableBlockQuote.CONSTRUCTOR -> {}

			TdApi.TextEntityTypeTextUrl.CONSTRUCTOR -> {
				val url = (entity.type as TdApi.TextEntityTypeTextUrl).url
				addStringAnnotation("URL", url, annotationStart, annotationEnd)
				addLink(
					LinkAnnotation.Url(
						url,
						TextLinkStyles(style = SpanStyle(color = Color(0xFF42AAFF))),
					), annotationStart, annotationEnd
				)
			}
			TdApi.TextEntityTypeUrl.CONSTRUCTOR -> {
				addStringAnnotation("URL", entityText, annotationStart, annotationEnd)
				addLink(
					LinkAnnotation.Url(
						entityText,
						TextLinkStyles(style = SpanStyle(color = Color(0xFF42AAFF))),
					), annotationStart, annotationEnd
				)
			}
			TdApi.TextEntityTypeEmailAddress.CONSTRUCTOR -> {
				addStringAnnotation("EMAIL", entityText, annotationStart, annotationEnd)
			}
			TdApi.TextEntityTypePhoneNumber.CONSTRUCTOR -> {
				addStringAnnotation("PHONE", entityText, annotationStart, annotationEnd)
			}
			TdApi.TextEntityTypeBankCardNumber.CONSTRUCTOR -> {
				addStringAnnotation("BANK_CARD", entityText, annotationStart, annotationEnd)
			}

			TdApi.TextEntityTypeMention.CONSTRUCTOR -> {
				addStringAnnotation("MENTION", entityText, annotationStart, annotationEnd)
			}
			TdApi.TextEntityTypeHashtag.CONSTRUCTOR -> {
				addStringAnnotation("HASHTAG", entityText.substring(1), annotationStart, annotationEnd)
			}
			TdApi.TextEntityTypeCashtag.CONSTRUCTOR -> {
				addStringAnnotation("CASHTAG", entityText.substring(1), annotationStart, annotationEnd)
			}
			TdApi.TextEntityTypeBotCommand.CONSTRUCTOR -> {
				addStringAnnotation("BOT_COMMAND", entityText, annotationStart, annotationEnd)
			}


			TdApi.TextEntityTypeMentionName.CONSTRUCTOR -> {
				val userId = (entity.type as TdApi.TextEntityTypeMentionName).userId
				addStringAnnotation("USER_MENTION", userId.toString(), annotationStart, annotationEnd)
				addStyle(SpanStyle(color = Color.Blue), annotationStart, annotationEnd)
			}
			TdApi.TextEntityTypeCustomEmoji.CONSTRUCTOR -> {}
			TdApi.TextEntityTypeMediaTimestamp.CONSTRUCTOR -> {
				val timestamp = (entity.type as TdApi.TextEntityTypeMediaTimestamp).mediaTimestamp
				addStringAnnotation("TIMESTAMP", timestamp.toString(), annotationStart, annotationEnd)
				addStyle(SpanStyle(color = Color.Blue, textDecoration = TextDecoration.Underline), annotationStart, annotationEnd)
			}
			else -> {}
		}
		currentOffset = end
	}

	if (currentOffset < text.length) {
		append(text.substring(currentOffset))
	}
}