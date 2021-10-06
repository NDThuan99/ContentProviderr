package com.example.contentproviderr;

public interface IMDSInterface {
    void clearPlaylist();
    void addSongPlaylist(String song );
    void playFile(int position );
    void pause();
    void stop();
    void skipForward();
    void skipBack();
}
