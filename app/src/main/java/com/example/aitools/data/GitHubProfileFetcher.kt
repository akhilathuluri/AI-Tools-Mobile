package com.example.aitools.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.URL

data class DeveloperProfile(
    val name: String = "",
    val bio: String = "",
    val avatarUrl: String = "",
    val location: String = "",
    val followers: Int = 0,
    val following: Int = 0,
    val publicRepos: Int = 0,
    val profileUrl: String = ""
)

object GitHubProfileFetcher {
    private const val GITHUB_API = "https://api.github.com/users/"
    
    suspend fun fetchProfile(username: String): DeveloperProfile {
        return withContext(Dispatchers.IO) {
            try {
                val url = URL(GITHUB_API + username)
                val connection = url.openConnection()
                connection.setRequestProperty("Accept", "application/vnd.github.v3+json")
                
                val response = connection.getInputStream().bufferedReader().use { it.readText() }
                val json = JSONObject(response)
                
                DeveloperProfile(
                    name = json.optString("name", ""),
                    bio = json.optString("bio", ""),
                    avatarUrl = json.optString("avatar_url", ""),
                    location = json.optString("location", ""),
                    followers = json.optInt("followers", 0),
                    following = json.optInt("following", 0),
                    publicRepos = json.optInt("public_repos", 0),
                    profileUrl = json.optString("html_url", "")
                )
            } catch (e: Exception) {
                e.printStackTrace()
                DeveloperProfile()
            }
        }
    }
} 