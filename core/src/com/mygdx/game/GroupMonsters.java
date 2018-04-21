package com.mygdx.game;

import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.utils.Array;

class GroupMonsters {

    private Array<Monster> shark, wave, vangdau;
    private WorldBatch sharks, waves, vangdaus;
    private Heart heart;
    private Flash flash;
    Boss boss;

    GroupMonsters() {
        shark = new Array<Monster>();
        wave = new Array<Monster>();
        vangdau = new Array<Monster>();
        sharks = new WorldBatch(5);
        waves = new WorldBatch(10);
        vangdaus = new WorldBatch(5);
        heart = new Heart();
        flash = new Flash();
        boss = new Boss();
    }

    void Load(Coordinate screen, Coordinate campos) {
        shark.clear();
        vangdau.clear();
        wave.clear();
        for (int i = 0; i < 5; ++i) {
            shark.add(new Monster(new Coordinate(ImageProcessing.p * 3 + MyGdxGame.random.nextInt((int)screen.x - ImageProcessing.p * 3),
                    MyGdxGame.random.nextInt((int)screen.y - ImageProcessing.p))));
            vangdau.add(new Monster(new Coordinate(ImageProcessing.p + MyGdxGame.random.nextInt((int)screen.x - ImageProcessing.p * 3),
                    MyGdxGame.random.nextInt((int)screen.y - ImageProcessing.p))));
            vangdau.get(i).nv = 2;
        }
        for (int i=0;i<10;i++) {
            wave.add(new Monster(new Coordinate(ImageProcessing.p + MyGdxGame.random.nextInt((int)screen.x - ImageProcessing.p * 3),
                    - MyGdxGame.random.nextInt((int)screen.y - ImageProcessing.p))));
            Monster tmp = wave.get(i);
            tmp.nv = 1;
            tmp.ace.y = MyGdxGame.random.nextInt(ImageProcessing.p * 2);
            tmp.coordinate.y -= screen.y;
            tmp.time = 5 + MyGdxGame.random.nextInt(5);
        }
        heart.set(new Coordinate(ImageProcessing.p * 3 + MyGdxGame.random.nextInt((int)screen.x - ImageProcessing.p * 3),
                screen.y * 3 + MyGdxGame.random.nextInt((int)screen.y - ImageProcessing.p)));
        flash.set(new Coordinate(ImageProcessing.p * 3 + MyGdxGame.random.nextInt((int)screen.x - ImageProcessing.p * 3),
                screen.y * 3 + MyGdxGame.random.nextInt((int)screen.y - ImageProcessing.p)));
        boss.set(screen, campos, 1);
    }

    void Auto_move(Hero hero,double delta_time,Coordinate screen,Coordinate campos) {
        for (Monster aShark:shark) {
            if (aShark.coordinate.y < campos.y - screen.y / 2)
                aShark.Refresh(MyGdxGame.random, campos);
            aShark.Auto_move(hero, delta_time);
        }
        for (Monster aVangdau:vangdau) {
            if (aVangdau.coordinate.y < campos.y - screen.y / 2)
                aVangdau.coordinate = new Coordinate(ImageProcessing.p + MyGdxGame.random.nextInt((int) screen.x - ImageProcessing.p * 3),
                        campos.y + screen.y + MyGdxGame.random.nextInt((int) screen.y));
            aVangdau.Auto_move(hero, delta_time);
        }
        for (Monster aWave:wave) {
            if (aWave.time < 0) {
                aWave.coordinate = new Coordinate(ImageProcessing.p + MyGdxGame.random.nextInt((int) screen.x - ImageProcessing.p * 3),
                    campos.y - screen.y / 2 - MyGdxGame.random.nextInt((int) screen.y));
                aWave.ace.y = MyGdxGame.random.nextInt(ImageProcessing.p * 2);
                aWave.time = 5 + MyGdxGame.random.nextInt(5);
            }
            else aWave.time -= delta_time;
            aWave.Auto_move(hero, delta_time);
        }
        heart.Control(delta_time);
        if (heart.time < 0) {
            heart.set(new Coordinate(ImageProcessing.p + MyGdxGame.random.nextInt((int) screen.x - ImageProcessing.p * 3),
                    campos.y + screen.y * 3 + MyGdxGame.random.nextInt((int) screen.y)));
        }
        if (heart.vt.x - ImageProcessing.p / 2 <= hero.coordinate.x && hero.coordinate.x <= heart.vt.x + ImageProcessing.p / 2)
            if (heart.vt.y - ImageProcessing.p / 2 <= hero.coordinate.y && hero.coordinate.y <= heart.vt.y + ImageProcessing.p / 2) {
                heart.set(new Coordinate(ImageProcessing.p + MyGdxGame.random.nextInt((int) screen.x - ImageProcessing.p * 3),
                        campos.y + screen.y * 3 + MyGdxGame.random.nextInt((int) screen.y)));
                if (hero.hp < 3)
                    hero.hp++;
            }
        flash.Control(delta_time);
        if (flash.time < 0) {
            flash.set(new Coordinate(ImageProcessing.p + MyGdxGame.random.nextInt((int) screen.x - ImageProcessing.p * 3),
                    campos.y + screen.y * 3 + MyGdxGame.random.nextInt((int) screen.y)));
        }
        if (flash.vt.x - ImageProcessing.p / 2 <= hero.coordinate.x && hero.coordinate.x <= flash.vt.x + ImageProcessing.p / 2)
            if (flash.vt.y - ImageProcessing.p / 2 <= hero.coordinate.y && hero.coordinate.y <= flash.vt.y + ImageProcessing.p / 2) {
                flash.set(new Coordinate(ImageProcessing.p + MyGdxGame.random.nextInt((int) screen.x - ImageProcessing.p * 3),
                        campos.y + screen.y * 3 + MyGdxGame.random.nextInt((int) screen.y)));
                hero.p += 2;
                hero.p = hero.p >= 6 ? 6 : hero.p;
            }
        boss.Control(hero, screen, campos);
    }

    void Draw(ModelBatch modelBatch, Environment environment, Coordinate campos, Coordinate hero_pos, Map map) {
        sharks.clear();
        for (Monster aShark : shark)
            aShark.Draw(sharks, campos);
        modelBatch.render(sharks, environment);
        waves.clear();
        for (Monster aWave : wave)
            aWave.Draw(waves, campos);
        modelBatch.render(waves, environment);
        vangdaus.clear();
        for (Monster aVangdau : vangdau)
            aVangdau.Draw(vangdaus, campos);
        modelBatch.render(vangdaus, environment);
        heart.Draw(modelBatch, environment, campos);
        flash.Draw(modelBatch, environment, campos);
        boss.Draw(modelBatch, environment, campos, hero_pos, map);
    }
}
