package com.javarxjavaroomexample;
public class Therory {
    /*

    1.Completable  – If you use it with @insert, @update, or @delete query then it will be called the onCompleted method in the success case of insertion, update, or deletion data from your table respectively.
    Ex : @Insert
Completable insert(User user);


2. Single<Long> or Single<List<Long>> or Maybe<Long> or Maybe<List<Long>> – If you use it with @insert, @update, or @delete query then it will be called the success method(RxJava observable callback method) in the success case with effected table rows id.

Ex : @Insert
Single<Integer> delete(User user);

3.Flowable –  It is syncing with the database at runtime, which means whenever any changes updated in the database then it will be fetched and updated on UI. The result will receive in onNext().
@Query("SELECT name FROM table_name")
    Flowable<List<String>> getAllName();

     */
}
