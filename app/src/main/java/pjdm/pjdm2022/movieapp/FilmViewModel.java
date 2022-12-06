package pjdm.pjdm2022.movieapp;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class FilmViewModel extends ViewModel {
    private final MutableLiveData<String> titolo = new MutableLiveData<>();
    public FilmViewModel(){
        titolo.setValue("");
    }
    public void setText(String titolo){
        this.titolo.setValue(titolo);
    }
    public LiveData<String> getText(){
        return titolo;
    }
}
