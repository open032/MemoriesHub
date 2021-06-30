package lex.neuron.memorieshub.util

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import lex.neuron.memorieshub.permission.internet.NetworkManager

lateinit var AUTH: FirebaseAuth

lateinit var REF_DATABASE_ROOT: DatabaseReference

const val DIR = "dir"
const val TITLE = "title"
const val MEMO_TWO_COLUMNS = "memotwocolumns"


