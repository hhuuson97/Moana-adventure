package com.mygdx.game;

import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;

import java.util.Random;

class Hero extends People {

    static private TextureRegion[][][] image = new TextureRegion[2][][];
    private WorldBatch heros;
    float p;
    int hp;
    double sensor;
    double last_update, kp = 0;
    String name;
    //static final String[] random_name = new String[]{"D.B.Kien", "N.H.D.Duc", "H.H.Son"};

    Hero(int nv) {
        p = 0;
        hp = 3;
        this.nv = nv;
        last_update = 0;
        kp = 0;
        heros = new WorldBatch(1);
    }

    void reset(int nv) {
        p = 0;
        hp = 3;
        this.nv = nv;
        last_update = 0;
        kp = 0;
        coordinate = new Coordinate();
        coordinate.x = (screen.x - ImageProcessing.p) / 2;
        coordinate.y = (screen.y) / 5;
        eff=new SelfEff[10];
        for (int i=0;i<10;i++)
            eff[i]=new SelfEff();
        //name = random_name[random.nextInt(random_name.length)];
    }

    void Load(Coordinate screen) {
        image[0] = ImageProcessing.getImage("surfer/main.png").split(ImageProcessing.p,ImageProcessing.p);//Nhân vật
        image[1] = ImageProcessing.getImage("surfer/other.png").split(ImageProcessing.p,ImageProcessing.p);
        coordinate.x = (screen.x - ImageProcessing.p) / 2;
        coordinate.y = (screen.y) / 5;
    }

    void Control(double delta_time, Khoi khoi) {
        if (hp > 0) {
            int s = (eff[6].time == 0) ? (int)(ImageProcessing.p * 3.2f + eff[5].val + eff[2].val - eff[1].val) : 0;
            ha++;
            if (ha > 200) {
                ha -= 200;
            }
            khoi.Add(new Coordinate(coordinate.x, coordinate.y),
                    new Coordinate((MyGdxGame.random.nextInt() % 100 / 100.0 - 0.5) * ImageProcessing.p, -MyGdxGame.random.nextInt() % 100 / 1000.0 * ImageProcessing.p));
            coordinate.x -= s * delta_time * sensor / 10;
            coordinate.y += s * delta_time;
            Qmove();
            SetSelfEff(delta_time);
        }
    }

    void Draw(ModelBatch modelBatch, Environment environment, Coordinate campos, Coordinate screen) {
        heros.clear();
        switch (tt) {
            case 0:
                heros.add(new Object(
                    new Sprite(image[nv][0][ha / 5 % 3]),
                    (float) (coordinate.x - campos.x) / ImageProcessing.p,
                    (float) (coordinate.y - campos.y) / ImageProcessing.p,
                    ImageProcessing.MINIMUM_VIEWPORT_SIZE / 3 + 0.03f,
                    ImageProcessing.UNIT_PIXEL * 1.5f,
                    ImageProcessing.UNIT_PIXEL * 1.5f,
                    0, 0, (float) (sensor / 3.5) * 40
                ));
                break;
            case 1:
                heros.add(new Object(
                        new Sprite(image[nv][1][ha / 5 % 3]),
                        (float) (coordinate.x - campos.x) / ImageProcessing.p,
                        (float) (coordinate.y - campos.y) / ImageProcessing.p,
                        ImageProcessing.MINIMUM_VIEWPORT_SIZE / 3 + 0.03f,
                        ImageProcessing.UNIT_PIXEL * 1.5f,
                        ImageProcessing.UNIT_PIXEL * 1.5f,
                        0, 0, (float) - (sensor - 2.5) / 7.5f * 40
                ));
                break;
            case 2:
                heros.add(new Object(
                        new Sprite(image[nv][2][ha / 5 % 3]),
                        (float) (coordinate.x - campos.x) / ImageProcessing.p,
                        (float) (coordinate.y - campos.y) / ImageProcessing.p,
                        ImageProcessing.MINIMUM_VIEWPORT_SIZE / 3 + 0.03f,
                        ImageProcessing.UNIT_PIXEL * 1.5f,
                        ImageProcessing.UNIT_PIXEL * 1.5f,
                        0, 0, (float) - (sensor + 2.5) / 7.5f * 40
                ));
                break;
        }
        modelBatch.render(heros, environment);
    }

    void Draw_info(SpriteBatch batch, PerspectiveCamera camera, BitmapFont font) {
        if (eff[0].time > 0) {
            font.setColor(189/255f, 154/255f, 109/255f, (float) (eff[0].time / 2f));
            font.getData().setScale(4f);
            font.draw(batch, "-" + eff[0].val, camera.viewportWidth / 2, camera.viewportHeight * 3 / 4);
        }
        //font.draw(batch, name, )
    }

}
