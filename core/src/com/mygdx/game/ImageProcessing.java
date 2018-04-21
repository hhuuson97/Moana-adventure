package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;


class ImageProcessing {

    static final int p = 256;
    final static float MINIMUM_VIEWPORT_SIZE = 5f;
    static  final float UNIT_PIXEL = 1f;

    static TextureRegion getImage(String source) {
        Texture texture = new Texture(Gdx.files.internal(source));
        return new TextureRegion(texture);
    }

}
