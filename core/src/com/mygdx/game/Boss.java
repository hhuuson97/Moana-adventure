package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;

import java.util.Random;

class Boss {

    static TextureRegion[][] boss_img;
    static TextureRegion[] boss2, boss3;
    private WorldBatch boss_batch;
    private Coordinate vt;
    private int ha;
    public int nv;
    boolean dung, visible;

    Boss() {
        boss_batch = new WorldBatch(1);
        boss_img = ImageProcessing.getImage("boss1.png").split(384, 335);
        boss2 = new TextureRegion[2];
        boss2[0] = ImageProcessing.getImage("boss2 (1).png");
        boss2[1] = ImageProcessing.getImage("boss2 (2).png");
        boss3 = new TextureRegion[2];
        boss3[0] = ImageProcessing.getImage("boss3 (1).png");
        boss3[1] = ImageProcessing.getImage("boss3 (2).png");
    }

    void set(Coordinate screen, Coordinate campos, int nv) {
        this.nv = nv;
        vt = new Coordinate(-1000,
                campos.y + screen.y * 3 + MyGdxGame.random.nextInt((int) screen.y));
        dung = false;
        visible = false;
    }

    void Draw(ModelBatch modelBatch, Environment environment, Coordinate campos, Coordinate hero_pos, Map map) {
        if (vt.y - hero_pos.y < ImageProcessing.p * 2.5 && visible) {
            if (vt.x == -1000) vt.x = hero_pos.x;
            boss_batch.clear();
            switch (map.stage) {
                case 1:
                    boss_batch.add(new Object(
                            new Sprite(boss_img[0][ha / 5 % 2]),
                            (float) (vt.x - campos.x) / ImageProcessing.p,
                            (float) (vt.y - campos.y) / ImageProcessing.p,
                            ImageProcessing.MINIMUM_VIEWPORT_SIZE / 3 + 0.04f,
                            ImageProcessing.UNIT_PIXEL * 1.4f,
                            ImageProcessing.UNIT_PIXEL * 1.4f,
                            90, 0, 0
                    ));
                    break;
                case 2:
                    boss_batch.add(new Object(
                            new Sprite(boss2[ha / 5 % 2]),
                            (float) (vt.x - campos.x) / ImageProcessing.p,
                            (float) (vt.y - campos.y) / ImageProcessing.p,
                            ImageProcessing.MINIMUM_VIEWPORT_SIZE / 3 + 0.04f,
                            ImageProcessing.UNIT_PIXEL * 1.4f,
                            ImageProcessing.UNIT_PIXEL * 1.4f,
                            90, 0, 0
                    ));
                    break;
                case 3:
                    boss_batch.add(new Object(
                            new Sprite(boss3[ha / 5 % 2]),
                            (float) (vt.x - campos.x) / ImageProcessing.p,
                            (float) (vt.y - campos.y) / ImageProcessing.p,
                            ImageProcessing.MINIMUM_VIEWPORT_SIZE / 3 + 0.04f,
                            ImageProcessing.UNIT_PIXEL * 1.4f,
                            ImageProcessing.UNIT_PIXEL * 1.4f,
                            90, 0, 0
                    ));
            }
            modelBatch.render(boss_batch, environment);
        }
    }

    void Control(Hero hero, Coordinate screen, Coordinate campos) {
        ha++;
        if (ha > 200) ha -= 200;
        if (vt.y - hero.coordinate.y < -ImageProcessing.p * 5 && visible) {
            vt = new Coordinate(-1000,
                    campos.y + screen.y * 3 + MyGdxGame.random.nextInt((int) screen.y));
            dung = false;
        }
        if (vt.x - ImageProcessing.p / 2 <= hero.coordinate.x && hero.coordinate.x <= vt.x + ImageProcessing.p / 2)
            if (vt.y - ImageProcessing.p / 2 <= hero.coordinate.y && hero.coordinate.y <= vt.y + ImageProcessing.p / 2)
                if (!dung) {
                    if (hero.hp > 0) {
                        hero.hp -= 2;
                        hero.eff[0].time = 2f;
                        hero.eff[0].val = 2;
                    }
                    hero.eff[0].time = 2f;
                    dung = true;
                }
    }
}
