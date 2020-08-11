package com.mark.alphavantage.mvvm

import android.app.Application
import androidx.lifecycle.*
import org.koin.core.KoinComponent
import rx.Subscription
import rx.subjects.BehaviorSubject
import rx.subscriptions.CompositeSubscription
import timber.log.Timber

abstract class BaseViewModel<T, R>(application: Application) : AndroidViewModel(application),
    LifecycleObserver, KoinComponent {

    protected val subscriptions = CompositeSubscription()
    private var eventSubscription: Subscription? = null
    private val eventsStream: BehaviorSubject<R> = BehaviorSubject.create()

    private val state = MutableLiveData<LiveState<T>>().apply {
        value = Init
    }
    private val data = MutableLiveData<ViewModelHolder<T>>().apply {
        value = ViewModelHolder(
            null,
            State.INIT
        ) //Initializing the ViewModel
    }


    val stateStream: LiveData<LiveState<T>>
        get() = state
    val dataStream: LiveData<ViewModelHolder<T>>
        get() = data

    init {
        initViewEventsSubject()
    }

    protected fun publish(items: T? = null, state: State? = null, throwable: Throwable? = null) {
        data.value?.update(items, state, throwable)
        data.value = data.value
    }

    protected fun publish(nextState: LiveState<T>){
        //TODO - change this to set value with handler in the future.
        state.postValue(nextState)
    }

    private fun initViewEventsSubject() {
        eventSubscription?.let {
            if (!it.isUnsubscribed) {
                it.unsubscribe()
            }
            eventSubscription = null
        }
        eventSubscription = eventsStream.asObservable().filter { it != null }.doOnNext {
            clearEventStream() //Clear the event stream when emitted
        }.subscribe { t -> handleScreenEvents(t) }
    }

    private fun clearEventStream() {
        eventsStream.onNext(null)
    }

    override fun onCleared() {
        super.onCleared()
        dispose()
    }

    fun dispatchInputEvent(event: R){
        eventsStream.onNext(event)
    }

    private fun dispose() {
        Timber.i("dispose: ")
        eventSubscription?.unsubscribe()
        eventSubscription = null
        subscriptions.clear()
    }

    abstract fun handleScreenEvents(event: R)
}

sealed class LiveState<out T>
data class Next<T>(val result: T? = null) : LiveState<T>()
object Init : LiveState<Nothing>()
object Loading : LiveState<Nothing>()
object Completed : LiveState<Nothing>()
data class Error<T>(val result: T? = null, val throwable: Throwable?) : LiveState<T>()