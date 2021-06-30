package lex.neuron.memorieshub.permission.internet

import android.app.Service
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import lex.neuron.memorieshub.R
import lex.neuron.memorieshub.databinding.AddEditDirBinding

class CheckNet() : Fragment(R.layout.add_edit_dir) {


    fun checkNet() : Boolean {
        var info : NetworkInfo? = null
        var connectivity = requireActivity().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if ( connectivity != null)
        {
            info = connectivity!!.activeNetworkInfo

            if (info != null)
            {
                if (info!!.state == NetworkInfo.State.CONNECTED)
                {
                    return true
                }
            }
            else
            {
                return false
            }
        }
        return false
    }



}