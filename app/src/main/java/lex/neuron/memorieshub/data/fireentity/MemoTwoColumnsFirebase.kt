package lex.neuron.memorieshub.data.fireentity

data class MemoTwoColumnsFirebase(
    val id: Long,
    val name: String,
    val description: String,
    val created: String,
    val idParent: Int
)
