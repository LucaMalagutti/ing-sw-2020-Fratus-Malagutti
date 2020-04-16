package it.polimi.ingsw.PSP4.observer;

import java.util.ArrayList;
import java.util.List;

public interface Observable<T> {
    public void addObserver (Observer<T> o);

    public void removeObserver (Observer<T> o);

    public void notifyObservers(T message);
}
