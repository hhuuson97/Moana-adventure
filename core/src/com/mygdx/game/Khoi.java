package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.utils.Array;

class Khoi {

    class Khoi_nho {

        Coordinate c, huong;
        double kt;
        int ha;

        Khoi_nho(Coordinate c, Coordinate huong) {
            this.c = c;
            this.huong = huong;
            kt = ImageProcessing.UNIT_PIXEL / 1.5;
            ha = 0;
        }
    }

    Array<Khoi_nho> tap_khoi;
    private WorldBatch[] khois;
    private TextureRegion[][] anh_khoi;

    Khoi() {
        tap_khoi = new Array<Khoi_nho>();
        anh_khoi = ImageProcessing.getImage("khoi.png").split(128, 128);
        khois = new WorldBatch[10];
        for (int i = 0; i < 10; i++)
            khois[i] = new WorldBatch(100);
    }

    void draw(ModelBatch modelBatch, Environment environment, Coordinate campos)  {
        for (int i = 0; i < 10; i++)
            khois[i].clear();
        for (int i = 0; i < tap_khoi.size; i++) {
            Khoi_nho k = tap_khoi.get(i);
            khois[k.ha / 5 % 10].add(new Object(
                    new Sprite(anh_khoi[0][k.ha / 5 % 10]),
                    (float)(k.c.x - campos.x) / ImageProcessing.p,
                    (float)(k.c.y - campos.y) / ImageProcessing.p,
                    ImageProcessing.MINIMUM_VIEWPORT_SIZE / 3 + 0.02f,
                    (float)k.kt,
                    (float)k.kt,
                    0, 0, 0
            ));
        }
        for (int i = 0; i < 10; i++)
        if (khois[i].size > 0)
            modelBatch.render(khois[i], environment);
    }

    void Add(Coordinate c, Coordinate huong) {
        tap_khoi.add(new Khoi_nho(c, huong));
    }

    void Control(double delta_time) {
        for (int i = 0; i < tap_khoi.size; i++) {
            Khoi_nho k = tap_khoi.get(i);
            k.c.x += k.huong.x * delta_time;
            k.c.y += k.huong.y * delta_time;
            k.kt *= 0.9;
            k.ha++;
            k.ha %= 200;
            if (k.kt < ImageProcessing.p / 500) {
                tap_khoi.removeIndex(i);
                i--;
            }
        }
    }

}