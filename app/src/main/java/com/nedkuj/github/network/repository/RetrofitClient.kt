package com.nedkuj.github.network.repository
import com.nedkuj.github.network.api.*

interface RetrofitClient {
  fun getGitHubAPI(): GitHubAPI
  fun getGitHubAuthAPI(): GitHubAuthAPI
}
