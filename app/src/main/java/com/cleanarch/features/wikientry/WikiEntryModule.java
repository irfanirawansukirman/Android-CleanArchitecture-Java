/*
 * Copyright 2017  Naresh Gowd Idiga
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cleanarch.features.wikientry;

import com.cleanarch.app.AppDatabase;
import com.cleanarch.features.wikientry.data.WikiEntryRepoImpl;
import com.cleanarch.features.wikientry.data.local.WikiEntryDao;
import com.cleanarch.features.wikientry.data.remote.WikiApiService;
import com.cleanarch.features.wikientry.usecases.WikiEntryRepo;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class WikiEntryModule {

    @WikiEntryScope
    @Provides
    WikiEntryDao provideWikiEntryDao(AppDatabase db) {
        return db.wikiEntryDao();
    }

    @WikiEntryScope
    @Provides
    WikiApiService provideWikiApiService() {
        return new Retrofit.Builder()
                .baseUrl("https://en.wikipedia.org/")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(WikiApiService.class);
    }

    @WikiEntryScope
    @Provides
    WikiEntryRepo provideWikiEntryRepo(AppDatabase appDatabase, WikiApiService apiService) {
        return new WikiEntryRepoImpl(appDatabase, apiService);
    }
}