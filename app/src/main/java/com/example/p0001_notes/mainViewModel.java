package com.example.p0001_notes;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Action;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class mainViewModel extends AndroidViewModel {

    private NoteDatabase noteDatabase;
    private MutableLiveData<Note> note = new MutableLiveData<>();
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public mainViewModel(@NonNull Application application) {
        super(application);
        noteDatabase = NoteDatabase.getInstance(application);
    }

    public LiveData<List<Note>> getNotes() {
        return noteDatabase.notesDao().getNotes();
    }

    public void add(Note note) {
        Disposable disposable = noteDatabase.notesDao().add(note)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action() {
                    @Override
                    public void run() throws Throwable {
                        noteDatabase.notesDao().add(note);
                    }
                });
        compositeDisposable.add(disposable);
    }

    public void remove(Note note) {
        Disposable disposable = noteDatabase.notesDao().remove(note.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action() {
                    @Override
                    public void run() throws Throwable {
                        noteDatabase.notesDao().remove(note.getId());
                    }
                });
        compositeDisposable.add(disposable);
    }

    public MutableLiveData<Note> getNote(int id) {
        Disposable disposable = getNoteRx(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Note>() {
                    @Override
                    public void accept(Note noteFromDb) throws Throwable {
                        note.setValue(noteFromDb);
                        //note = noteFromDb;
                        Log.d("TAG", noteFromDb.getName());
                    }
                });
        compositeDisposable.add(disposable);
        return note;
    }

    private Single<Note> getNoteRx(int id) {
        return Single.fromCallable(new Callable<Note>() {
            @Override
            public Note call() throws Exception {
                return noteDatabase.notesDao().getNote(id);
            }
        });
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.dispose();
    }
}
