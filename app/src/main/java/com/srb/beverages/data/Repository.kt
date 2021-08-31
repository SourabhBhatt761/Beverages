package com.srb.beverages.data

import com.srb.beverages.data.source.LocalDataSource
import com.srb.beverages.data.source.RemoteDataSource
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject


@ActivityRetainedScoped
class Repository @Inject constructor(
    remoteDataSource: RemoteDataSource,
    localDataSource: LocalDataSource
    ) {

    val remote = remoteDataSource
    val local = localDataSource

}