package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;

class Heart {

    private TextureRegion heart;
    private WorldBatch batch;
    double x, tx;
    Coordinate vt;
    double time;

    Heart() {
        batch = new WorldBatch(1);
        heart = ImageProcessing.getImage("heart.png");
    }

    void set(Coordinate vt) {
        x = 0;
        tx = ImageProcessing.p / 3;
        this.vt = vt;
        vt.z = ImageProcessing.MINIMUM_VIEWPORT_SIZE / 3 + MyGdxGame.random.nextInt(400)/1000f;
        time = 20;
    }

    void Draw(ModelBatch modelBatch, Environment environment, Coordinate campos) {
        batch.clear();
        batch.add(new Object(
                new Sprite(heart),
                (float)(vt.x - campos.x + x) / ImageProcessing.p,
                (float)(vt.y - campos.y) / ImageProcessing.p,
                (float)vt.z,
                ImageProcessing.UNIT_PIXEL,
                ImageProcessing.UNIT_PIXEL,
                90, 0, 0
        ));
        modelBatch.render(batch, environment);
    }

    void Control(double delta_time) {
        x += delta_time * tx;
        if (x > ImageProcessing.p / 3 || x < -ImageProcessing.p / 3) tx = -tx;
        time -= delta_time;
    }
}