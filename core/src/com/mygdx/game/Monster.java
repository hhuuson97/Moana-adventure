package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.Random;

class Monster extends People {

    static private TextureRegion[][][] image=new TextureRegion[3][][];

    double time;
    boolean dung;

    Monster(Coordinate c) {
        super(c);
        time = 0;
        dung = false;
        coordinate.z = ImageProcessing.MINIMUM_VIEWPORT_SIZE / 3 + MyGdxGame.random.nextInt(200)/1000f;
    }

    static void Load() {
        image[0] = ImageProcessing.getImage("shark/shark.png").split(ImageProcessing.p,ImageProcessing.p);
        image[1] = ImageProcessing.getImage("wave/wave.png").split(ImageProcessing.p,ImageProcessing.p);
        image[2] = ImageProcessing.getImage("vangdau.png").split(ImageProcessing.p * 3,ImageProcessing.p);
    }

    private void Control(double delta_time) {
        ha++;
        if (ha > 200) ha -= 200;
        switch (nv) {
            case 1:
                coordinate.y += (ImageProcessing.p * 2 + ace.y) * delta_time;
                Qmove();
        }
    }

    void Auto_move(Hero hero, double delta_time) {
        Control(delta_time);
        switch (nv) {
            case 1:
                if (coordinate.x - ImageProcessing.p / 2 <= hero.coordinate.x && hero.coordinate.x <= coordinate.x + ImageProcessing.p / 2)
                    if (coordinate.y - ImageProcessing.p / 4 <= hero.coordinate.y && hero.coordinate.y <= coordinate.y) {
                        hero.eff[5].val = ImageProcessing.p + (int) ace.y;
                        hero.eff[5].time = 0.5f;
                    }
                break;
            case 0:
                if (coordinate.x - ImageProcessing.p / 2 <= hero.coordinate.x && hero.coordinate.x <= coordinate.x + ImageProcessing.p / 2)
                    if (coordinate.y - ImageProcessing.p / 2 <= hero.coordinate.y && hero.coordinate.y <= coordinate.y + ImageProcessing.p / 2)
                        if (!dung) {
                            if (hero.hp > 0) {
                                hero.hp--;
                            }
                            hero.eff[0].time = 2f;
                            hero.eff[0].val = 1;
                            dung = true;
                        }
                break;
            case 2:
                if (coordinate.x - ImageProcessing.p * 3 / 4 <= hero.coordinate.x && hero.coordinate.x <= coordinate.x + ImageProcessing.p * 3 / 4)
                    if (coordinate.y - ImageProcessing.p / 2 <= hero.coordinate.y && hero.coordinate.y <= coordinate.y + ImageProcessing.p / 2)
                        if (hero.eff[1].time == 0) {
                            hero.eff[1].val = ImageProcessing.p * 5 / 2;
                            hero.eff[1].time = 3f;
                        }
                break;
        }
    }

    void Refresh(Random random, Coordinate campos) {
        coordinate = new Coordinate(ImageProcessing.p + random.nextInt((int) screen.x - ImageProcessing.p * 3),
                campos.y + screen.y + random.nextInt((int) screen.y));
        dung = false;
    }

    void Draw(WorldBatch monsters, Coordinate campos) {
        switch (nv) {
            case 0:
                monsters.add(new Object(
                        new Sprite(image[0][1][(ha / 5) % 3]),
                        (float)(coordinate.x - campos.x) / ImageProcessing.p,
                        (float)(coordinate.y - campos.y) / ImageProcessing.p,
                        (float)coordinate.z,
                        ImageProcessing.UNIT_PIXEL,
                        ImageProcessing.UNIT_PIXEL,
                        0, 0, 0
                ));
                break;
            case 2:
                monsters.add(new Object(
                        new Sprite(image[2][(ha / 5) % 3][0]),
                        (float)(coordinate.x - campos.x) / ImageProcessing.p,
                        (float)(coordinate.y - campos.y) / ImageProcessing.p,
                        (float)coordinate.z,
                        ImageProcessing.UNIT_PIXEL * 1.5f,
                        ImageProcessing.UNIT_PIXEL,
                        0, 0, 0, 0.6f
                ));
                break;
            default:
                monsters.add(new Object(
                        new Sprite(image[1][0][(ha / 5) % 3]),
                        (float)(coordinate.x - campos.x) / ImageProcessing.p,
                        (float)(coordinate.y - campos.y) / ImageProcessing.p,
                        (float)coordinate.z,
                        ImageProcessing.UNIT_PIXEL,
                        ImageProcessing.UNIT_PIXEL,
                        0, 0, 0
                ));
        }
    }

    void Draw_info(Batch batch) {
    }

}
