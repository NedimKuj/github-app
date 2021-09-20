package com.nedkuj.github.di.fragment


import android.content.Intent
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import javax.inject.Inject

class NavigatorImpl @Inject constructor(private val fragment: Fragment) : Navigator {
  override fun getNavController(): NavController = fragment.findNavController()
  override fun getParentNavController(): NavController? = fragment.parentFragment?.findNavController()
  override fun startActivityIntent(intent: Intent) { fragment.context?.startActivity(intent) }
}
