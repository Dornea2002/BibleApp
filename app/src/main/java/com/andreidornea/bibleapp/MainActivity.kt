package com.andreidornea.bibleapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.andreidornea.bibleapp.model.AvailableTranslations
import com.andreidornea.bibleapp.model.Chapter
import com.andreidornea.bibleapp.model.TranslationBooks
import com.andreidornea.bibleapp.ui.theme.BibleAppTheme
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BibleAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }

        ApiClient.apiService.getAvailableTranslations()
            .enqueue(object : Callback<AvailableTranslations> {
                override fun onResponse(
                    call: Call<AvailableTranslations?>,
                    response: Response<AvailableTranslations?>
                ) {
                    val translations = response.body()?.translations
                        ?.map {
                            val name = it.name
                            val shortName = it.shortName
                            "$name    -----     $shortName"
                        }
                    var translationsString = ""
                    translations?.forEach { translationsString = "$translationsString $it \n" }
                    Log.d("Here", translationsString)
                }

                override fun onFailure(call: Call<AvailableTranslations?>, t: Throwable) {
                    Log.e("API", "Failed: ${t.message}")
                    Log.e("API", "Cause: ${t.cause}")
                    t.printStackTrace()
                }
            })

        ApiClient.apiService.getBookListByTranslation("BSB")
            .enqueue(object : Callback<TranslationBooks> {
                override fun onResponse(
                    call: Call<TranslationBooks?>,
                    response: Response<TranslationBooks?>
                ) {
                    val books = response.body()?.books
                        ?.map {
                            val book = it.name
                            val chapterNumbers = it.numberOfChapters
                            "$book  - $chapterNumbers"
                        }
                    var bookString = ""
                    books?.forEach { bookString = "$bookString $it \n" }
                    Log.d("Here", bookString)
                }

                override fun onFailure(call: Call<TranslationBooks?>, t: Throwable) {
                    Log.e("API", "Failed: ${t.message}")
                    Log.e("API", "Cause: ${t.cause}")
                    t.printStackTrace()
                }
            })
        ApiClient.apiService.getChapterByBookTranslation("BSB", "GEN", "1")
            .enqueue(object : Callback<Chapter> {
                override fun onResponse(
                    call: Call<Chapter?>,
                    response: Response<Chapter?>
                ) {
                    val chapter = response.body()?.chapter?.chapterContent
                        ?.filter { it["type"] == "verse" }
                        ?.map { verse ->
                            val number = (verse["number"] as? Double)?.toInt()
                            val contentList = verse["content"] as? List<*>
                            val text = contentList
                                ?.filterIsInstance<String>()
                                ?.joinToString("   ")
                            "$number: $text"
                        }
                    var chapterString = ""
                    chapter?.forEach { chapterString = "$chapterString $it \n" }
                    Log.d("Here", chapterString)
                }

                override fun onFailure(call: Call<Chapter?>, t: Throwable) {
                    Log.e("API", "Failed: ${t.message}")
                    Log.e("API", "Cause: ${t.cause}")
                    t.printStackTrace()
                }
            })
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    BibleAppTheme {
        Greeting("Android")
    }
}