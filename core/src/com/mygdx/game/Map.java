package com.mygdx.game;

import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.utils.Array;

class Map {

    int width,height,stage;
    private int ha;
    private TextureRegion[] wave = new TextureRegion[2];
    static private TextureRegion icon;
    private TextureRegion[][] boey;
    private WorldBatch[] waves = new WorldBatch[2];
    private WorldBatch boyes;
    private double way, start;
    private Texture pline;

    Map() {
        stage = 1;
        ha = 0;
        way = ImageProcessing.p * 750;
        start = 0;
        icon =  ImageProcessing.getImage("moana_icon.png");
    }

    void reset() {
        stage = 1;
        ha = 0;
        way = ImageProcessing.p * 750;
        start = 0;
    }

    void Set(Coordinate screen, PerspectiveCamera camera) {
        width = (int)screen.x/ImageProcessing.p;
        height = (int)screen.y/ImageProcessing.p;
        boyes = new WorldBatch(height * 4 + 4);
        waves[0] = new WorldBatch(((height * 2 + 5) / 4) * ((width * 2 + 45) / 4));
        waves[1] = new WorldBatch(((height * 2 + 5) / 4) * ((width * 2 + 45) / 4));
        Pixmap myPixMap = new Pixmap(30, (int)camera.viewportHeight / 2 + 1, Pixmap.Format.RGBA4444);
        myPixMap.setColor(0, 0, 0, 1);
        myPixMap.fillRectangle(10, 10, 2, (int)camera.viewportHeight / 2 - 10);
        myPixMap.fillRectangle(5, 5, 10, 2);
        myPixMap.fillRectangle(5, (int)camera.viewportHeight / 2, 10, 2);
        pline = new Texture(myPixMap);
    }

    void UpLevel(Hero hero) {
        stage++;
        start = way;
        way += way * 1.2;
    }

    //Đầu vào thông tin nhân vật và hình ảnh trong game
    void Read() {
        wave[0] = ImageProcessing.getImage("background/waves.png");
        wave[1] = ImageProcessing.getImage("background/waves2.png");
        boey = ImageProcessing.getImage("boey/boey.png").split(ImageProcessing.p,ImageProcessing.p);
    }

    void Draw_waves(ModelBatch batch, Environment environment, Coordinate campos) {
        waves[0].clear();
        waves[1].clear();
        for(int i = -21; i < width * 2 + 21; i += 4)
            for (int j = (int)campos.y / ImageProcessing.p - height - 1; j < (int)campos.y / ImageProcessing.p + height + 1; j += 4) {
                waves[1].add(new Object(
                        new Sprite(wave[1]),
                        i - ((20 * ha) - (float)campos.x) / (float)ImageProcessing.p,
                        j - (float)campos.y / ImageProcessing.p,
                        - ImageProcessing.MINIMUM_VIEWPORT_SIZE / 3 + 0.01f,
                        ImageProcessing.UNIT_PIXEL * 4,
                        ImageProcessing.UNIT_PIXEL * 4,
                        0, 0, 0));
                waves[0].add(new Object(new Sprite(wave[0]),
                        i + ((20 * ha) - (float)campos.x) / (float)ImageProcessing.p,
                        j - (float)campos.y / ImageProcessing.p,
                        ImageProcessing.MINIMUM_VIEWPORT_SIZE / 3 + 0.01f,
                        ImageProcessing.UNIT_PIXEL * 4,
                        ImageProcessing.UNIT_PIXEL * 4,
                        0, 0, 0));
            }
        batch.render(waves[0], environment);
        batch.render(waves[1], environment);
    }

    void Draw_boeys(ModelBatch batch, Environment environment, Coordinate campos, Coordinate screen) {
        ha = (ha + 1) % ImageProcessing.p;
        boyes.clear();
        for (int j = (int)campos.y / ImageProcessing.p - height - 1; j < (int)campos.y / ImageProcessing.p + height + 1; ++j) {
            boyes.add(new Object(
                    new Sprite(boey[0][ha / 5 % 3]),
                    (float)(- campos.x) / ImageProcessing.p,
                    j - (float)campos.y / ImageProcessing.p,
                    ImageProcessing.MINIMUM_VIEWPORT_SIZE / 3 + 0.01f,
                    ImageProcessing.UNIT_PIXEL,
                    ImageProcessing.UNIT_PIXEL,
                    0, 0, 0
            ));
            boyes.add(new Object(
                    new Sprite(boey[0][ha / 5 % 3]),
                    (float)(screen.x - campos.x) / ImageProcessing.p - 1,
                    j - (float)campos.y / ImageProcessing.p,
                    ImageProcessing.MINIMUM_VIEWPORT_SIZE / 3 + 0.01f,
                    ImageProcessing.UNIT_PIXEL,
                    ImageProcessing.UNIT_PIXEL,
                    0, 0, 0
            ));
        }
        batch.render(boyes, environment);
    }

    void Update(Hero hero, Boss boss) {
        if (hero.coordinate.y > start + (way - start) * 3 / 4) boss.visible = true;
        else boss.visible = false;
        if (hero.coordinate.y > way) {
            UpLevel(hero);
            boss.nv = (boss.nv + 1) % 3;
        }
    }

    void Draw_Info(SpriteBatch batch, PerspectiveCamera camera, Hero hero) {
        batch.draw(pline, camera.viewportWidth / 20, camera.viewportWidth / 20);
        batch.draw(icon, 10, (float) (10 + (hero.coordinate.y - start) * (camera.viewportHeight / 2 - 10) / way), camera.viewportWidth / 10, camera.viewportWidth / 10);
        batch.draw(Boss.boss_img[0][0], 10, (float) (10 + (way - start) * 3 / 4 * (camera.viewportHeight / 2 - 10) / way), camera.viewportWidth / 10, camera.viewportWidth / 10);
    }

}
