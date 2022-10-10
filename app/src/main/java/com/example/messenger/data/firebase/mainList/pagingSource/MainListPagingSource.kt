package com.example.messenger.data.firebase.mainList.pagingSource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.messenger.data.firebase.mainList.model.MainListItem
import com.example.messenger.utils.FirebaseConstants
import com.example.messenger.utils.FirebaseConstants.CHILD_DATE_TIME
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.tasks.await

const val PAGE_SIZE = 10

class MainListPagingSource(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
):PagingSource<QuerySnapshot, MainListItem>() {

    private fun docRef(): Query {

        val userId = auth.currentUser?.uid!!

        return firestore
            .collection(FirebaseConstants.COLLECTION_USERS)
            .document(userId)
            .collection(FirebaseConstants.COLLECTION_MAIN_LIST)
            .orderBy(CHILD_DATE_TIME, Query.Direction.DESCENDING)
            .limit(PAGE_SIZE.toLong())
    }

    override suspend fun load(params: LoadParams<QuerySnapshot>): LoadResult<QuerySnapshot, MainListItem> {
        return try {

            val currentPage = params.key ?: docRef()
                .get()
                .await()

            if (currentPage.size() == 0)
                return LoadResult.Page(
                    data = emptyList(),
                    prevKey = null,
                    nextKey = null
                )

            val lastDocumentSnapshot = currentPage.documents[currentPage.size() - 1]

            val nextPage = docRef()
                .startAfter(lastDocumentSnapshot)
                .get()
                .await()

            LoadResult.Page(
                data = currentPage.toObjects(MainListItem::class.java),
                prevKey = null,
                nextKey = nextPage
            )

        }catch (e:Exception){
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<QuerySnapshot, MainListItem>): QuerySnapshot? {
        return null
    }
}