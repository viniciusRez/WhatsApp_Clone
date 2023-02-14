package com.example.whatsapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.whatsapp.Adapter.ChatAdapter
import com.example.whatsapp.Adapter.ContatosAdapter
import com.example.whatsapp.R
import com.example.whatsapp.ViewModels.ChatViewModels
import com.example.whatsapp.ViewModels.DatabaseViewModel
import com.example.whatsapp.ViewModels.RouterViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

class ConversasFragment: Fragment() {
    // TODO: Rename and change types of parameters
    private val chatViewModels:ChatViewModels = ChatViewModels()
    private lateinit var routerViewModel:RouterViewModel
    private val databaseViewModel:DatabaseViewModel = DatabaseViewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view: View = inflater.inflate(R.layout.fragment_contatos, container, false)
        this.routerViewModel = RouterViewModel(requireContext())
        val recyclerViewContatos: RecyclerView = view.findViewById(R.id.RecyclerViewContatos)

        this.chatViewModels.getAllChats{ result ->
            val adapter = ChatAdapter(result)
            recyclerViewContatos.adapter = adapter
            adapter.setonItemClickListener(object: ChatAdapter.onItemClickListener{
                override fun onItemClick(position: Int) {
                    databaseViewModel.getDadosBykey(result[position].id){
                        routerViewModel.goChat(it)
                    }
                }

            } )
        }

        return view
    }


}