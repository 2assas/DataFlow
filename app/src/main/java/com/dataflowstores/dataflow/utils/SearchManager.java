package com.dataflowstores.dataflow.utils;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.subjects.PublishSubject;

public class SearchManager {
    private final PublishSubject<String> searchSubject = PublishSubject.create();

    // Method to trigger search based on user input
    public void setSearchQuery(String query) {
        searchSubject.onNext(query);
    }

    // Get the search Observable
    public Observable<String> getSearchObservable() {
        return searchSubject.hide(); // Hide the identity of the subject
    }
}
