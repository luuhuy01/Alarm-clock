package com.luuhuy.btl_donghobaothuc.adapter;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Timer;

public class ItemTime implements Serializable {
    private int id;
    private String timer;
    private String title;
    private String nameMusic;
    private String [] repeat;
    private boolean notiRepeat;
    private boolean checked;

    public ItemTime() {
        this.checked = true;
    }

    public ItemTime(int id, String timer, String title, String nameMusic, String[] repeat, boolean notiRepeat, boolean checked) {
        this.id = id;
        this.timer = timer;
        this.title = title;
        this.nameMusic = nameMusic;
        this.repeat = repeat;
        this.notiRepeat = notiRepeat;
        this.checked = checked;
    }

    public ItemTime(String timer, String title, String nameMusic, String[] repeat, boolean notiRepeat, boolean checked) {
        this.timer = timer;
        this.title = title;
        this.nameMusic = nameMusic;
        this.repeat = repeat;
        this.notiRepeat = notiRepeat;
        this.checked = checked;
    }

    public ItemTime(String timer, String title, boolean checked) {
        this.timer = timer;
        this.title = title;
        this.checked = checked;
    }

    public ItemTime(String timer, String title) {
        this.timer = timer;
        this.title = title;
        this.checked = true;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTimer() {
        return timer;
    }

    public void setTimer(String timer) {
        this.timer = timer;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getNameMusic() {
        return nameMusic;
    }

    public void setNameMusic(String nameMusic) {
        this.nameMusic = nameMusic;
    }

    public String[] getRepeat() {
        return repeat;
    }

    public void setRepeat(String[] repeat) {
        this.repeat = repeat;
    }

    public boolean isNotiRepeat() {
        return notiRepeat;
    }

    public void setNotiRepeat(boolean notiRepeat) {
        this.notiRepeat = notiRepeat;
    }

    @Override
    public String toString() {
        return "ItemTime{" +
                "id=" + id +
                ", timer='" + timer + '\'' +
                ", title='" + title + '\'' +
                ", nameMusic='" + nameMusic + '\'' +
                ", repeat=" + Arrays.toString(repeat) +
                ", notiRepeat=" + notiRepeat +
                ", checked=" + checked +
                '}';
    }
}
