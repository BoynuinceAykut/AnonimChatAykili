package com.aykutboynuince.aykilisoft.anonimchat;

public class Message {
    String mesajText;
    String gonderici;
    String zaman;
    String displayName;

    public Message()
    {

    }

    public Message(String mesajText, String gonderici, String zaman, String displayName){
        this.mesajText = mesajText;
        this.gonderici = gonderici;
        this.zaman = zaman;
        this.displayName = displayName;
    }

    public String getMesajText(){
        return mesajText;
    }

    public void setMesajText(String mesajText){
        this.mesajText = mesajText;
    }

    public String getGonderici(){
        return gonderici;
    }

    public void setGonderici(String gonderici){
        this.gonderici = gonderici;
    }

    public String getZaman() {
        return zaman;
    }

    public void setZaman(String zaman) {
        this.zaman = zaman;
    }

    public String getDisplayName(){return displayName;}

    public void setDisplayName(){this.displayName = displayName;}
}
