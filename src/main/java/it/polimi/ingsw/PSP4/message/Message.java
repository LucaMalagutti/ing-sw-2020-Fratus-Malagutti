package it.polimi.ingsw.PSP4.message;

public class Message {
    //Classe creata come placeholder per i generics di Observable e Observer
    String payload;

    public String getPayload() {return this.payload;}

    public Message(String payload) {
        this.payload = payload;
    }
}
