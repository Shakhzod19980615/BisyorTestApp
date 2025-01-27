data class ChatModel(
    val hasNextPage: Boolean,
    val itemCount: Int,
    val users: List<UserChat>
)

data class UserChat(
    val user: User,
    val chatId: Int,
    val lastMessage: Message,
    val item: Item?
)

data class User(
    val id: Int,
    val avatarUrl: String,
    val fullName: String,
    val phoneNumber: String,
    val email: String,
    val isOnline: Boolean,
    val lastSeen: String
)

data class Message(
    val text: String,
    val createdAt: String?,
    val unreadCount: Int
)

data class Item(
    val id: Int?,
    val title: String?,
    val imageUrl: String?
)
