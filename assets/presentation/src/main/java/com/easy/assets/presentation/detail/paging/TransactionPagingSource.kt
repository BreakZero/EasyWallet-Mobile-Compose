package com.easy.assets.presentation.detail.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.easy.assets.domain.model.AssetInfo
import com.easy.assets.domain.model.Transaction
import com.easy.assets.domain.use_case.AssetTransactions

class TransactionPagingSource(
    private val assetTransactions: AssetTransactions,
    private val asset: AssetInfo
) : PagingSource<Int, Transaction>() {

    override fun getRefreshKey(state: PagingState<Int, Transaction>): Int? {
        return state.anchorPosition?.let {
            val anchorPage = state.closestPageToPosition(it)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Transaction> {
        val nextPage = params.key ?: 1
        val result = assetTransactions(asset.slug, 20, nextPage, asset.contractAddress)
        return if (result.isFailure) {
            LoadResult.Error(result.exceptionOrNull() ?: RuntimeException("loading paging error"))
        } else {
            val txs = result.getOrNull() ?: emptyList()
            LoadResult.Page(
                data = result.getOrNull() ?: emptyList(),
                prevKey = if (nextPage == 1) null else nextPage.minus(1),
                nextKey = if (txs.isEmpty()) null else nextPage.plus(1)
            )
        }
    }
}