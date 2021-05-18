package com.srb.beverages.data.network

import com.srb.beverages.data.datasource.RemoteDataSource
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject


@ActivityRetainedScoped
class Repository @Inject constructor(
    remoteDataSource: RemoteDataSource,

    ) {

    val remote = remoteDataSource

}