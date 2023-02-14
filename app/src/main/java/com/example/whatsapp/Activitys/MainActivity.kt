package com.example.whatsapp.Activitys

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.viewpager.widget.ViewPager
import com.example.whatsapp.R
import com.example.whatsapp.ViewModels.AuthViewModel
import com.example.whatsapp.ViewModels.RouterViewModel
import com.example.whatsapp.fragments.ContatosFragment
import com.example.whatsapp.fragments.ConversasFragment
import com.ogaclejapan.smarttablayout.SmartTabLayout
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems


class MainActivity : AppCompatActivity() {
    private val authViewModel: AuthViewModel = AuthViewModel()
    private val routerViewModel: RouterViewModel = RouterViewModel(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbarPrincipal)
        toolbar.title = "WhatsApp"
        setSupportActionBar(toolbar)
        val adapter = FragmentPagerItemAdapter(
            supportFragmentManager, FragmentPagerItems.with(this)
                .add("Contatos", ContatosFragment::class.java)
                .add("Conversas", ConversasFragment::class.java)
                .create()
        )
        val viewPager = findViewById<View>(R.id.viewPager) as ViewPager
        viewPager.adapter = adapter

        val viewPagerTab = findViewById<View>(R.id.viewPagertab) as SmartTabLayout
        viewPagerTab.setViewPager(viewPager)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.sair -> {
                if (authViewModel.singOut()) {
                    finish()
                }
            }
            R.id.options -> {
                routerViewModel.goConfig()
            }
        }

        return super.onOptionsItemSelected(item)
    }
}