package com.reise.hessportsportal;

public class GameModel {

    private String gameName;
    private String gameHtmlLink;
    private String startingTime;
    private String gameThumbnailLink;

    public GameModel(String gameName, String gameHtmlLink, String startingTime, String gameThumbnailLink) {
        this.gameName = gameName;
        this.gameHtmlLink = gameHtmlLink;
        this.startingTime = startingTime;
        this.gameThumbnailLink = gameThumbnailLink;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getGameHtmlLink() {
        return gameHtmlLink;
    }

    public void setGameHtmlLink(String gameHtmlLink) {
        this.gameHtmlLink = gameHtmlLink;
    }

    public String getStartingTime() {
        return startingTime;
    }

    public void setStartingTime(String startingTime) {
        this.startingTime = startingTime;
    }

    public String getGameThumbnailLink() {
        return gameThumbnailLink;
    }

    public void setGameThumbnailLink(String gameThumbnailLink) {
        this.gameThumbnailLink = gameThumbnailLink;
    }
}
