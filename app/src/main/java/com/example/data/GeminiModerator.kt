package com.example.data

import android.util.Log
import com.example.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.util.concurrent.TimeUnit

object GeminiModerator {
    private const val TAG = "GeminiModerator"
    
    private val client = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .writeTimeout(15, TimeUnit.SECONDS)
        .build()

    /**
     * Scans content (chat/posts) to check if it violates community standards or Kannada cultural spirit.
     * Returns a pair: (isFlagged: Boolean, reason: String)
     */
    suspend fun auditContent(text: String): Pair<Boolean, String> = withContext(Dispatchers.IO) {
        val apiKey = try {
            BuildConfig.GEMINI_API_KEY
        } catch (e: Exception) {
            ""
        }

        // Fallback or rule-based moderation if no valid key is provided
        val isPlaceholder = apiKey.isEmpty() || apiKey == "MY_GEMINI_API_KEY" || apiKey.startsWith("placeholder")
        if (isPlaceholder) {
            Log.w(TAG, "No real GEMINI_API_KEY provided or using placeholder. Running offline smart rules.")
            return@withContext runLocalHeuristics(text)
        }

        val prompt = """
            You are the Official Automated Community Moderator for the 'Suverna Karnataka Rakshana Vedike' social forum.
            This is a forum promoting Karnataka, beautiful Kannada cultural heritage, literature, history, and community harmony.
            
            Analyze this user-submitted text for any of the following:
            1. Hate speech, abusive language, or profanities.
            2. Insults directed at the Kannada culture, regional pride, historic monuments (like Hampi, Badami), or renowned icons.
            3. Advertisements of illegal items, gambling, spam, or scams.
            
            Text to analyze: "$text"
            
            You MUST return a JSON object with this exact structure, with no extra formatting, markdown wraps, or code tags:
            {
               "isFlagged": true/false,
               "confidence": 0.0 to 1.0,
               "violationReason": "Brief explanation in English stating why it was flagged or why it complies safely."
            }
        """.trimIndent()

        val url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash:generateContent?key=$apiKey"
        
        val jsonPayload = JSONObject().apply {
            put("contents", org.json.JSONArray().apply {
                put(JSONObject().apply {
                    put("parts", org.json.JSONArray().apply {
                        put(JSONObject().apply {
                            put("text", prompt)
                        })
                    })
                })
            })
            put("generationConfig", JSONObject().apply {
                put("responseMimeType", "application/json") // Ensure JSON return
                put("temperature", 0.1)
            })
        }

        val requestBody = jsonPayload.toString().toRequestBody("application/json; charset=utf-8".toMediaType())
        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .header("Content-Type", "application/json")
            .build()

        try {
            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    Log.e(TAG, "Gemini API error code: ${response.code}. Executing database fallbacks.")
                    return@withContext runLocalHeuristics(text)
                }

                val bodyStr = response.body?.string() ?: ""
                val responseJson = JSONObject(bodyStr)
                val candidates = responseJson.getJSONArray("candidates")
                if (candidates.length() > 0) {
                    val parts = candidates.getJSONObject(0)
                        .getJSONObject("content")
                        .getJSONArray("parts")
                    if (parts.length() > 0) {
                        val textResponse = parts.getJSONObject(0).getString("text").trim()
                        val resultJson = JSONObject(textResponse)
                        val isFlagged = resultJson.optBoolean("isFlagged", false)
                        val reason = resultJson.optString("violationReason", "Audited successfully via Gemini AI.")
                        return@withContext Pair(isFlagged, reason)
                    }
                }
                return@withContext runLocalHeuristics(text)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Exception calling Gemini, falling back to offline audit rules", e)
            return@withContext runLocalHeuristics(text)
        }
    }

    /**
     * Local heuristic moderation to model safety and provide instant, beautiful interactions instantly!
     */
    private fun runLocalHeuristics(text: String): Pair<Boolean, String> {
        val lower = text.lowercase().trim()
        
        // Define some cultural-sensitive triggers or standard spam
        if (lower.contains("spam") || lower.contains("casino") || lower.contains("lottery") || lower.contains("buy drugs") || lower.contains("bitcoin double")) {
            return Pair(true, "Flagged: External advertisement/financial scam detected.")
        }
        if (lower.contains("abuse") || lower.contains("idiot") || lower.contains("hate kannada") || lower.contains("useless heritage")) {
            return Pair(true, "Flagged: Violates respectful code of conduct toward culture and peer members.")
        }
        if (lower.length < 3) {
            return Pair(false, "Approved: Text is too short to violate standards.")
        }
        
        // Positive cultural checks to highlight standard community safety
        if (lower.contains("hampi") || lower.contains("kannada") || lower.contains("heritage") || lower.contains("ಕನ್ನಡ") || lower.contains("ಪ್ರವಾಸಿ")) {
            return Pair(false, "Approved: Celebrates and promotes regional tourist places/heritage constructively.")
        }

        return Pair(false, "Approved: Complies with community standards.")
    }
}
