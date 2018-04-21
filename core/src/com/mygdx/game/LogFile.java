package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

class LogFile {

    private static LogFile ourInstance = new LogFile();
    private FileHandle f= Gdx.files.external("surfers/log.txt");

    static LogFile getInstance() {
        return ourInstance;
    }

    private LogFile() {
    }

    void NewFile() {
        f.writeString("",false);
    }

    void SaveLog(long x) {
        f.writeString(x+" ",true);
    }

    void SaveLog(String x) { f.writeString(x+" ",true); }

    void NextLine() {
        f.writeString(System.getProperty("line.separator"),true);
    }

}
