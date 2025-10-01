package ni.shikatu.a50gramm.utlis

import org.drinkless.tdlib.TdApi
/**
 * A chat. (Can be a private chat, basic group, supergroup, or secret chat.)
 *
 * @param id Chat unique identifier.
 * @param type Type of the chat.
 * @param title Chat title.
 * @param photo Chat photo; may be null.
 * @param accentColorId Identifier of the accent color for message sender name, and backgrounds of chat photo, reply header, and link preview.
 * @param backgroundCustomEmojiId Identifier of a custom emoji to be shown on the reply header and link preview background for messages sent by the chat; 0 if none.
 * @param profileAccentColorId Identifier of the profile accent color for the chat's profile; -1 if none.
 * @param profileBackgroundCustomEmojiId Identifier of a custom emoji to be shown on the background of the chat's profile; 0 if none.
 * @param permissions Actions that non-administrator chat members are allowed to take in the chat.
 * @param lastMessage Last message in the chat; may be null if none or unknown.
 * @param positions Positions of the chat in chat lists.
 * @param chatLists Chat lists to which the chat belongs. A chat can have a non-zero position in a chat list even if it doesn't belong to the chat list and have no position in a chat list even if it belongs to the chat list.
 * @param messageSenderId Identifier of a user or chat that is selected to send messages in the chat; may be null if the user can't change message sender.
 * @param blockList Block list to which the chat is added; may be null if none.
 * @param hasProtectedContent True, if chat content can't be saved locally, forwarded, or copied.
 * @param isTranslatable True, if translation of all messages in the chat must be suggested to the user.
 * @param isMarkedAsUnread True, if the chat is marked as unread.
 * @param viewAsTopics True, if the chat is a forum supergroup that must be shown in the &quot;View as topics&quot; mode, or Saved Messages chat that must be shown in the &quot;View as chats&quot;.
 * @param hasScheduledMessages True, if the chat has scheduled messages.
 * @param canBeDeletedOnlyForSelf True, if the chat messages can be deleted only for the current user while other users will continue to see the messages.
 * @param canBeDeletedForAllUsers True, if the chat messages can be deleted for all users.
 * @param canBeReported True, if the chat can be reported to Telegram moderators through reportChat or reportChatPhoto.
 * @param defaultDisableNotification Default value of the disableNotification parameter, used when a message is sent to the chat.
 * @param unreadCount Number of unread messages in the chat.
 * @param lastReadInboxMessageId Identifier of the last read incoming message.
 * @param lastReadOutboxMessageId Identifier of the last read outgoing message.
 * @param unreadMentionCount Number of unread messages with a mention/reply in the chat.
 * @param unreadReactionCount Number of messages with unread reactions in the chat.
 * @param notificationSettings Notification settings for the chat.
 * @param availableReactions Types of reaction, available in the chat.
 * @param messageAutoDeleteTime Current message auto-delete or self-destruct timer setting for the chat, in seconds; 0 if disabled. Self-destruct timer in secret chats starts after the message or its content is viewed. Auto-delete timer in other chats starts from the send date.
 * @param emojiStatus Emoji status to be shown along with chat title; may be null.
 * @param background Background set for the chat; may be null if none.
 * @param themeName If non-empty, name of a theme, set for the chat.
 * @param actionBar Information about actions which must be possible to do through the chat action bar; may be null if none.
 * @param businessBotManageBar Information about bar for managing a business bot in the chat; may be null if none.
 * @param videoChat Information about video chat of the chat.
 * @param pendingJoinRequests Information about pending join requests; may be null if none.
 * @param replyMarkupMessageId Identifier of the message from which reply markup needs to be used; 0 if there is no default custom reply markup in the chat.
 * @param draftMessage A draft of a message in the chat; may be null if none.
 * @param clientData Application-specific data associated with the chat. (For example, the chat scroll position or local chat notification settings can be stored here.) Persistent if the message database is used.
 */
fun TdApi.Chat.copy(): TdApi.Chat {
	return TdApi.Chat(
		this.id,
		this.type,
		this.title,
		this.photo,
		this.accentColorId,
		this.backgroundCustomEmojiId,
		this.profileAccentColorId,
		this.profileBackgroundCustomEmojiId,
		this.permissions,
		this.lastMessage,
		this.positions,
		this.chatLists,
		this.messageSenderId,
		this.blockList,
		this.hasProtectedContent,
		this.isTranslatable,
		this.isMarkedAsUnread,
		this.viewAsTopics,
		this.hasScheduledMessages,
		this.canBeDeletedOnlyForSelf,
		this.canBeDeletedForAllUsers,
		this.canBeReported,
		this.defaultDisableNotification,
		this.unreadCount,
		this.lastReadInboxMessageId,
		this.lastReadOutboxMessageId,
		this.unreadMentionCount,
		this.unreadReactionCount,
		this.notificationSettings,
		this.availableReactions,
		this.messageAutoDeleteTime,
		this.emojiStatus,
		this.background,
		this.themeName,
		this.actionBar,
		this.businessBotManageBar,
		this.videoChat,
		this.pendingJoinRequests,
		this.replyMarkupMessageId,
		this.draftMessage,
		this.clientData
	)
}