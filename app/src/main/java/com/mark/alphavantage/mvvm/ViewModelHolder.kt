package com.mark.alphavantage.mvvm

enum class State{
    INIT, LOADING, NEXT, COMPLETE, ERROR
}

class ViewModelHolder<T> (var data : T?, var state : State, var throwable: Throwable? = null){
    fun copyWith(data: T?, state: State? = State.COMPLETE, throwable: Throwable? = null) : ViewModelHolder<T> {
        return ViewModelHolder<T>(
            data ?: this.data,
            state ?: this.state,
            throwable ?: this.throwable
        )
    }

    fun update(data : T? = null, state : State? = null, throwable: Throwable? = null){
       this.data = data ?: this.data
       this.state = state ?: this.state
       this.throwable = throwable ?: this.throwable
    }
}