package edu.ucsc.retap.retap.messages.presenter

import edu.ucsc.retap.retap.conversations.view.ConversationsViewModule
import edu.ucsc.retap.retap.messages.adapter.MessagesAdapter
import edu.ucsc.retap.retap.messages.interactor.MessagesInteractor
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class ConversationsPresenter(
        private val conversationsViewModule: ConversationsViewModule,
        private val messagesAdapter: MessagesAdapter,
        private val messagesInteractor: MessagesInteractor) {

    private val compositeDisposable = CompositeDisposable()

    fun loadMessages() {
        conversationsViewModule.showLoading()
        compositeDisposable.add(
            messagesInteractor.getSMSMessages()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSuccess {
                    val messages = it
                    val items = messages
                            .distinctBy {
                                it.sender
                            }

                    messagesAdapter.items = items
                    conversationsViewModule.hideLoading()
                }
                .subscribe()
        )

        compositeDisposable.add(
                messagesAdapter.observeItemClick()
                        .doOnNext {
                           setItemIndex(it)
                        }
                        .subscribe()
        )
    }

    private fun setItemIndex(index: Int) {
        messagesAdapter.selectedItemIndex = index
    }

    fun cleanUp() {
        compositeDisposable.clear()
    }
}