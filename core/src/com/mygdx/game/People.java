package com.mygdx.game;

import java.util.Random;

class People {

    Coordinate coordinate;//Toạ độ
    Coordinate ace;//Vận tốc
    int ha=0,tt,nv;//Tốc độ ảnh, hướng
    double skill_cd;//thời gian hồi skill
    SelfEff[] eff;//hiệu ứng đang có
    static Coordinate screen;

    People() {
        coordinate=new Coordinate();
        ace=new Coordinate();
        skill_cd=0;
        eff=new SelfEff[10];
        for (int i=0;i<10;i++)
            eff[i]=new SelfEff();
        ace.x=0;
        ace.y=0;
    }

    People(Coordinate c) {
        coordinate=new Coordinate(c.x,c.y);
        ace=new Coordinate();
        skill_cd=0;
        eff=new SelfEff[10];
        for (int i=0;i<10;i++)
            eff[i]=new SelfEff();
        ace.x=0;
        ace.y=0;
    }

    static void GetScreen(Coordinate scr) {
        screen = scr;
    }

    static void Load_img() {
    }

    void Qmove() {
        if (coordinate.x<ImageProcessing.p) coordinate.x = ImageProcessing.p;
        if (coordinate.x>screen.x-ImageProcessing.p*2) coordinate.x = screen.x - ImageProcessing.p * 2;
    }

    void SetSelfEff(double delta_time) {
        for (int i = 0; i <= 9; i++) {
            if (eff[i].time>0) {
                eff[i].time-= delta_time;
                if (eff[i].time<0) {
                    eff[i].time=0;
                    eff[i].val=0;
                }
            }
        }
    }

}
