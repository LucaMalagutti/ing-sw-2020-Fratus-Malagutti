package it.polimi.ingsw.PSP4.observer;

public interface Observer<T> {
    void update(T message);
}
