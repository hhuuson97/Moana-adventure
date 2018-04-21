package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;

class Menu {

    private TextureRegion check, logo;
    private double ha;
    boolean logined;

    Menu() {
        check = ImageProcessing.getImage("menu/check.png");
        logo = ImageProcessing.getImage("menu/logo.png");
        reset();
    }

    void reset() {
        ha = 0;
        logined = false;
    }

    void draw(SpriteBatch batch, PerspectiveCamera camera, double delta_time) {
        ha += delta_time * camera.viewportHeight / 10;
        if (ha > camera.viewportHeight / 2) ha = camera.viewportHeight / 2;
        Sprite tmp = new Sprite(logo);
        tmp.setScale(camera.viewportWidth / logo.getRegionWidth(), camera.viewportHeight / 4 / logo.getRegionHeight());
        tmp.setPosition((camera.viewportWidth - logo.getRegionWidth()) / 2,
                (camera.viewportHeight * 3 / 2 - logo.getRegionHeight()) / 2 + camera.viewportHeight / 2 - (int)ha);
        tmp.draw(batch);
    }

    void login(SpriteBatch batch, PerspectiveCamera camera, double delta_time, Hero hero) {
        draw(batch, camera, delta_time);
        /*TextField textField = new TextField(hero.name, new Skin());
        textField.setSize((int)camera.viewportWidth * 3 / 4, 50);
        textField.setPosition(camera.viewportWidth / 8, camera.viewportHeight / 2);
        textField.setTextFieldListener(new TextField.TextFieldListener()
        {
            @Override
            public void keyTyped(TextField textField, char key)
            {
                if (key == '\n')
                {
                    textField.getOnscreenKeyboard().show(false);
                }
            }
        });*/
        if (Gdx.input.isTouched()) logined = true;
    }

    boolean begin(SpriteBatch batch, PerspectiveCamera camera, double delta_time, BitmapFont font) {
        draw(batch, camera, delta_time);
        return Gdx.input.isTouched();
    }

}
