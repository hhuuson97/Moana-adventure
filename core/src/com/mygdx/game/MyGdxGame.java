package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.FloatAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.MathUtils;

import java.util.Random;

public class MyGdxGame extends ApplicationAdapter {

	private PerspectiveCamera camera; //máy ảnh trong game
	private ModelBatch modelBatch; //màn hình trong game
	SpriteBatch batch;
	private Model tableTopModel;
	private ModelInstance tableTop;
	private Environment environment;
	private Music sound;
	private BitmapFont font;//font chữ trong game
	static Random random = new Random();

	//Nhom tu dinh nghia
	private float distance;
	private Hero hero;//thuộc tính nhân vật
	private Menu menu;
	private Coordinate screen=new Coordinate(),campos=new Coordinate();
	private Khoi khoi;
	//Vị trí chuột và màn hình
	private double delta_time;//thời gian hệ thống = ping của ứng dụng
	private GroupMonsters monster;
	private Map map;
	private double score = 0;
	private boolean play = false;
	private double waiting_time = 0, count = 0;
	private boolean played = true;

	private void Read_file() {
		Monster.Load();
		People.Load_img();
		sound = Gdx.audio.newMusic(Gdx.files.internal("theme.mp3"));
		sound.play();
		sound.setLooping(true);
	}

	private void Check_event() {
		double tmp = hero.sensor;
		hero.sensor = (float)(Gdx.input.getAccelerometerX() + hero.kp) * 5 / 5f;
		if (tmp == hero.sensor) hero.last_update += Gdx.graphics.getDeltaTime();
		else hero.last_update = 0;
		if (tmp != hero.sensor && hero.last_update < 0.1) hero.kp = (hero.kp == 0)?0.1:0;
		if (Gdx.input.getAccelerometerX()>3.5) hero.tt=1;
		else if (Gdx.input.getAccelerometerX()<-3.5) hero.tt=2;
		else
			if (Gdx.input.getAccelerometerX() < 2.5 && Gdx.input.getAccelerometerY() > -2.5) hero.tt=0;
		hero.sensor = Gdx.input.getAccelerometerX();
		if (Gdx.input.isTouched() && hero.p > 0 && hero.eff[2].time == 0) {
			hero.eff[2].val = ImageProcessing.p*4;
			hero.eff[2].time = 4f;
			hero.p--;
		}
	}

	private void move_camera() {
		double tx = hero.coordinate.x;
		double ty = hero.coordinate.y + screen.y / 2 - screen.y * 2 / 5;
		double tmp = (screen.x - ImageProcessing.p) / 2;

		camera.translate((float)-(Math.abs(tx - tmp) - Math.abs(campos.x - tmp)) / ImageProcessing.p,
				0,
				(float)-(Math.abs(tx - tmp) - Math.abs(campos.x - tmp)) / ImageProcessing.p);
		camera.lookAt(0, 0, 0);
		campos.x = tx;
		campos.y = ty;
	}

	private void main_screen() {
		map.Draw_waves(modelBatch, environment, campos);
		map.Draw_boeys(modelBatch, environment,campos,screen);
		if (play) {
			move_camera();
			monster.Draw(modelBatch, environment, campos, hero.coordinate, map);
			hero.Draw(modelBatch, environment, campos, screen);
			khoi.draw(modelBatch, environment, campos);
		}
	}

	private void info(SpriteBatch batch, PerspectiveCamera camera) {
		font.setColor(189/255f, 154/255f, 109/255f, 0.8f);
		font.getData().setScale(1.5f);
		font.draw(batch, "Time: " + (int) score + "s", 10, camera.viewportHeight - 10);
		font.draw(batch, "Flash: " + (int) (hero.p), 10, camera.viewportHeight - 30);
		font.draw(batch, "Live: " + hero.hp, 10, camera.viewportHeight - 50);
		font.draw(batch, "Live: " + hero.hp, 10, camera.viewportHeight - 50);
		font.draw(batch, "Stage: " + map.stage, 10, camera.viewportHeight - 70);
		hero.Draw_info(batch, camera, font);
		map.Draw_Info(batch, camera, hero);
	}

	private void lose(SpriteBatch batch, PerspectiveCamera camera) {
		font.setColor(102/255f, 102/255f, 102/255f, 0.8f);
		font.getData().setScale(3f);
		GlyphLayout layout = new GlyphLayout(font,"LOSE");
		font.draw(batch,layout,(int)camera.viewportWidth / 2 - layout.width / 2,(int)camera.viewportHeight / 2 + layout.height / 2);
		waiting_time -= delta_time;
		if (waiting_time < 0) waiting_time = 0;
		if (waiting_time == 0) {
			font.setColor(102 / 255f, 102 / 255f, 102 / 255f, 0.8f);
			font.getData().setScale(1f);
			layout = new GlyphLayout(font, "<TOUCH>");
			font.draw(batch, layout, (int) camera.viewportWidth / 2 - layout.width / 2, (int) camera.viewportHeight / 4 + layout.height / 2);
			if (Gdx.input.isTouched()) play = false;
		}
	}

	private void Login(SpriteBatch batch, PerspectiveCamera camera) {
		if (!menu.logined)
			menu.login(batch, camera, delta_time, hero);
		else play = menu.begin(batch, camera, delta_time, font);
		if (play) {
			screen.y = ImageProcessing.p * 12;
			screen.x = ImageProcessing.p * 7;
			campos.x = (screen.x - ImageProcessing.p) / 2;
			campos.y = screen.y / 2;
			menu.logined = false;
			map.reset();
			hero.reset(0); // nhan vat chinh = 0, phu = 1
			menu.reset();
			monster.Load(screen, campos);
			score = 0;
		}
	}

	public void create() {
		batch = new SpriteBatch();
		modelBatch = new ModelBatch();
		ModelBuilder builder = new ModelBuilder();

		builder.begin();
		builder.node().id = "ground";
		builder.part("ground", GL20.GL_TRIANGLES, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal,
				new Material(ColorAttribute.createDiffuse(new Color(109/255f, 189/255f, 154/255f, 1f))))
				.box(0f, 0f, -3.5f, 60f, 60f, 1f);

		Material material;
		material = new Material(
				new BlendingAttribute(0.6f),
				new FloatAttribute(FloatAttribute.AlphaTest, 0.5f));
		builder.node().id = "sea";
		builder.part("sea", GL20.GL_TRIANGLES, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal,
				material)
				.box(0f, 0f, 0f, 60f, 60f, 2.98f);

		ColorAttribute colorAttr = new ColorAttribute(ColorAttribute.Diffuse, new Color(109/255f, 189/255f, 154/255f, 1f));
		tableTopModel = builder.end();
		tableTop = new ModelInstance(tableTopModel);
		tableTop.materials.get(1).set(colorAttr);
		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.7f, 0.7f, 0.7f, 1.f));
		environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -.4f, -.4f, -.4f));

		map = new Map();
		menu = new Menu();
		hero = new Hero(0);
		khoi = new Khoi();
		monster = new GroupMonsters();

		font = new BitmapFont();
		map.Read();
		Read_file();

		if (played) {
			sound.play();
			played = false;
		}

		camera=new PerspectiveCamera();
		camera.position.set(0, 0, 10);
		camera.lookAt(0,0,0);
	}

	public void cam_reset() {
		camera.position.set(0, - ImageProcessing.MINIMUM_VIEWPORT_SIZE * 0.6f, distance);
		camera.lookAt(0, 0, 0);
		camera.update();
	}

	public void resize(int width, int height) {
		screen.y = ImageProcessing.p * 12;
		screen.x = ImageProcessing.p * 7;
		campos.x = (screen.x - ImageProcessing.p) / 2;
		campos.y = screen.y / 2;
		hero.Load(screen);
		People.GetScreen(screen);

		//create camera
		float halfHeight = ImageProcessing.MINIMUM_VIEWPORT_SIZE * 0.5f;
		if (height > width)
			halfHeight *= (float)height / (float)width;
		float halfFovRadians = MathUtils.degreesToRadians * camera.fieldOfView * 0.5f;
		distance = halfHeight / (float)Math.tan(halfFovRadians) + ImageProcessing.MINIMUM_VIEWPORT_SIZE / 3 + 0.03f;
		camera.viewportWidth = (float)width;
		camera.viewportHeight = (float)height;
		map.Set(screen, camera);
		cam_reset();
	}

	public void render() {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		delta_time = Gdx.graphics.getDeltaTime();
		count += delta_time;

		if (play) {
			Check_event();
			camera.update();

			// 3d processing
			modelBatch.begin(camera);
			modelBatch.render(tableTop, environment);
			if (map.stage == 2)
				tableTop.materials.get(1).set(new ColorAttribute(ColorAttribute.Diffuse, new Color(255/255f, 189/255f, 154/255f, 1f)));

			score += delta_time;
			hero.Control(delta_time, khoi);
			khoi.Control(delta_time);
			map.Update(hero, monster.boss);
			monster.Auto_move(hero, delta_time, screen, campos);
			main_screen();
			modelBatch.end();

			// 2d processing
			Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
			batch.begin();
			info(batch, camera);
			if (hero.hp <= 0) {
				lose(batch, camera);
			}
			batch.end();
			Gdx.gl.glDisable(GL20.GL_DEPTH_TEST);
		}
		else{
				modelBatch.begin(camera);
				modelBatch.render(tableTop, environment);
				main_screen();
				modelBatch.end();
				Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
				batch.begin();
				Login(batch, camera);
				batch.end();
				Gdx.gl.glDisable(GL20.GL_DEPTH_TEST);
			}
	}

	public void dispose() {
		font.dispose();
		modelBatch.dispose();
		batch.dispose();
		tableTopModel.dispose();
	}
}