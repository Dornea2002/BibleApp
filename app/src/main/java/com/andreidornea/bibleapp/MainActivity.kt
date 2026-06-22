package com.andreidornea.bibleapp

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.graphics.Color
import androidx.core.view.WindowCompat
import androidx.fragment.app.add
import androidx.fragment.app.commit
import com.andreidornea.bibleapp.model.AvailableTranslations
import com.andreidornea.bibleapp.model.Chapter
import com.andreidornea.bibleapp.model.TranslationBooks
import com.andreidornea.bibleapp.screen.home.HomeFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.statusBarColor = Color.TRANSPARENT
        window.navigationBarColor = Color.TRANSPARENT

        setContentView(R.layout.activity_main)

        if(savedInstanceState == null){
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                add<HomeFragment>(R.id.fragment_container_view)
            }
        }
        testAPIs()
    }

    private fun testAPIs(){
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