data class ChatListResponse(
    val hasNextPage: Boolean,
    val itemCount: Int,
    val usersList: List<ChatResponse>
)

data class ChatResponse(
    val to_user: ChatUserResponse,
    val chat_id: Int,
    val message: ChatMessageResponse,
    val items: ItemsResponse?
)

data class ChatUserResponse(
    val userId: Int,
    val avatar: String,
    val userFIO: String,
    val phone: String,
    val email: String,
    val statusOnline: Boolean,
    val lastSeen: String
)

data class ChatMessageResponse(
    val last_message: String,
    val date_cr: String,
    val count: Int
)

data class ItemsResponse(
    val item_id: Int?,
    val item_title: String?,
    val item_image: String?
)
fun ChatListResponse.toChatModel() = ChatModel(
    hasNextPage = hasNextPage,
    itemCount = itemCount,
    users = usersList.map { it.toUserChat() }
)
fun ChatResponse.toUserChat() = UserChat(
    user = to_user.toUser(),
    chatId = chat_id,
    lastMessage = message.toMessage(),
    item = items?.toItem()
)
fun ChatUserResponse.toUser() = User(
    id = userId,
    avatarUrl = avatar,
    fullName = userFIO,
    phoneNumber = phone,
    email = email,
    isOnline = statusOnline,
    lastSeen = lastSeen
)
fun ChatMessageResponse.toMessage() = Message(
    text = last_message,
    createdAt = date_cr,
    unreadCount = count
)
fun ItemsResponse.toItem() = Item(
    id = item_id,
    title = item_title,
    imageUrl = item_image
)
