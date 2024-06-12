import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponse(
    @SerialName("name")
    val name: String,
    @SerialName("message")
    val message: String,
    @SerialName("code")
    val code: Int,
    @SerialName("status")
    val status: Int,
    @SerialName("previous")
    val previous: PreviousError? = null
)

@Serializable
data class PreviousError(
    @SerialName("name")
    val name: String,
    @SerialName("message")
    val message: String,
    @SerialName("code")
    val code: Int
)
