/*
 * Copyright 2020. Huawei Technologies Co., Ltd. All rights reserved.

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
 */
package com.huawei.codelab.sitecodelab

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.huawei.hms.site.api.SearchResultListener
import com.huawei.hms.site.api.SearchService
import com.huawei.hms.site.api.SearchServiceFactory
import com.huawei.hms.site.api.model.*


class MainActivity : AppCompatActivity() {
    companion object {
        private const val TAG = "MainActivity"
    }

    private var searchService: SearchService? = null
    private lateinit var resultTextView: TextView
    private lateinit var queryInput: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Fixme: Please replace "API key" with your API KEY
        searchService = SearchServiceFactory.create(this,
                "API key")
        queryInput = findViewById(R.id.edit_text_text_search_query)
        resultTextView = findViewById(R.id.response_text_search)
    }

    fun search(view: View?) {
        val textSearchRequest = TextSearchRequest()
        textSearchRequest.query = queryInput.text.toString()
        searchService?.textSearch(
            textSearchRequest,
            object : SearchResultListener<TextSearchResponse> {
                override fun onSearchResult(textSearchResponse: TextSearchResponse?) {
                    val siteList: List<Site>? = textSearchResponse?.getSites()
                    if (textSearchResponse == null || textSearchResponse.getTotalCount() <= 0 || siteList.isNullOrEmpty()) {
                        resultTextView.text = "Result is Empty!"
                        return
                    }
                    val response = StringBuilder("\nsuccess\n")
                    var addressDetail: AddressDetail?

                    textSearchResponse.sites.forEachIndexed {index, site ->
                        addressDetail = site.address
                        response.append("[${index + 1}]  name: ${site.name}, formatAddress: ${site.formatAddress}, country: ${addressDetail?.country ?: ""}, countryCode: ${addressDetail?.countryCode ?: ""} \r\n")

                    }

                    Log.d(TAG, "search result is : $response")
                    resultTextView.text = response.toString()
                }

                override fun onSearchError(searchStatus: SearchStatus) {
                    Log.e(TAG, "onSearchError is: " + searchStatus.errorCode)
                    resultTextView.text = "Error : ${searchStatus.getErrorCode()}  ${searchStatus.getErrorMessage()}"
                }
            })
    }
}
