package com.example.kotlindictionary

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.widget.AdapterView
import android.widget.ListView
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.RecyclerView

import kotlinx.android.synthetic.main.activity_main.*
import java.lang.ref.WeakReference

class MainActivity : AppCompatActivity() {

    companion object {
        val TAG = "DictionaryActivity"
        val LAST_SEARCH_WORD: String = "LAST_SEARCH_WORD"
    }
    var mDbHelper: DatabaseHelper? = null
    private var mSearchListAdapter: SearchListAdapter? = null
    private var mSearchQuery: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mSearchQuery = savedInstanceState?.getString(LAST_SEARCH_WORD) ?: ""
        mDbHelper = DatabaseHelper(applicationContext)

        if(!isDbLoaded()) {
            showLoadingUI()
            LoadViewTask(this).execute()
        } else {
            showDictUI()
        }

    }
    private fun isDbLoaded(): Boolean {
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(this)
        return sharedPref.getBoolean(DatabaseHelper.DB_CREATED, false)
    }

    private fun showLoadingUI() {
        setContentView(R.layout.activity_main)
    }

    private fun showDictUI() {
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        supportActionBar?.setIcon(R.mipmap.ic_launcher)

        mSearchListAdapter =
            SearchListAdapter(applicationContext, mDbHelper!!.getWords(mSearchQuery))
        val lstWords = (findViewById<ListView>(R.id.lstWords))
        lstWords.adapter = mSearchListAdapter
        lstWords.onItemClickListener = AdapterView.OnItemClickListener { _, _, _, id ->
            val wordDetailIntent = Intent(applicationContext, WordDetailActivity::class.java)
            wordDetailIntent.putExtra(WordDetailActivity.WORD_ID, "$id")
            startActivity(wordDetailIntent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)

        val searchView: SearchView? = menu.findItem(R.id.action_search).actionView as? SearchView
        val searchManager: SearchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView?.setSearchableInfo(searchManager.getSearchableInfo(componentName))

        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                updateListByQuery(newText ?: "")
                return true
            }
        })

        return true
    }

    private fun updateListByQuery(searchQuery: String) {
        mSearchQuery = searchQuery
        mSearchListAdapter?.changeCursor(mDbHelper!!.getWords(searchQuery))
    }

    private class LoadViewTask(activity: DictionaryActivity) : AsyncTask<Void, Void, Void>() {
        private var mActivity = WeakReference<DictionaryActivity>(activity)

        override fun doInBackground(vararg params: Void): Void? {
            if(getActivityInstance()?.mDbHelper?.readableDatabase?.isOpen == true) {
                Log.d(TAG, "Db is OK.")
            }
            return null
        }

        override fun onPostExecute(result: Void?) {
            if(getActivityInstance()?.isDbLoaded() == true) {
                getActivityInstance()?.showDictUI()
            }
        }

        private fun getActivityInstance() = mActivity.get()
    }
}
