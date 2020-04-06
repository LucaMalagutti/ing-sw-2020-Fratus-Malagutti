package it.polimi.ingsw.PSP4.client;

public interface ServerObserver
{
    void didReceiveConvertedString(String oldStr, String newStr);
}
