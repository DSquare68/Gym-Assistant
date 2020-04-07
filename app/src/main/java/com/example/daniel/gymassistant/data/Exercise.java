package com.example.daniel.gymassistant.data;

public class Exercise {
    public int _ID;
    public String name;
    public String language;

    public Exercise(int _ID, String name, String language) {
        this._ID = _ID;
        this.name = name;
        this.language = language;
    }

    public int get_ID() {
        return _ID;
    }

    public void set_ID(int _ID) {
        this._ID = _ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}
