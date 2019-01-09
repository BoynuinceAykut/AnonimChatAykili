package com.aykutboynuince.aykilisoft.anonimchat;

public class Users {
    String userName;
    String recordDate;
    String userId;
    String displayName;
    String profilPhotoUrl;

    public Users()
    {

    }

    public Users(String userName, String recordDate, String userId, String displayName, String profilPhotoUrl){
        this.userName = userName;
        this.recordDate = recordDate;
        this.userId = userId;
        this.displayName = displayName;
        this.profilPhotoUrl = profilPhotoUrl;
    }

    public String getProfilPhotoUrl(){return  profilPhotoUrl;}

    public void setProfilPhotoUrl(String profilPhotoUrl){ this.profilPhotoUrl = profilPhotoUrl;}

    public String getUserId() {return  userId;}

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public String getUserName(){
        return userName;
    }

    public void setUserName(String userName){
        this.userName= userName;
    }

    public String getRecordDate()
    {
        return recordDate;
    }

    public void setRecordDate(String recordDate)
    {
        this.recordDate = recordDate;
    }

    public String getDisplayName(){return displayName;}

    public void setDisplayName(String displayName) {this.displayName = displayName;}

}
