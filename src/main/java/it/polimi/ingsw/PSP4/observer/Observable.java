package it.polimi.ingsw.PSP4.observer;

public interface Observable<T> {
    void addObserver(Observer<T> o);

    void removeObserver(Observer<T> o);

    void notifyObservers(T message);
}
