package com.mygdx.game;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.RenderableProvider;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.FloatAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.utils.MeshBuilder;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ObjectSet;
import com.badlogic.gdx.utils.Pool;

class Object extends Renderable {

    float[] vertices;
    short[] indices;

    public Object(Sprite obj, float x, float y, float z, float width, float height, float angX,float angY, float angZ) {

        material = new Material(
                TextureAttribute.createDiffuse(obj.getTexture()),
                new BlendingAttribute(false, 1f),
                FloatAttribute.createAlphaTest(0.7f)
        );

        obj.setSize(width, height);
        obj.setPosition(-obj.getWidth() * 0.5f, -obj.getHeight() * 0.5f);

        vertices = convert(obj.getVertices());
        indices = new short[]{0, 1, 2, 2, 3, 0};

        worldTransform.trn(x, y, z);
        worldTransform.rotate(Vector3.X, angX);
        worldTransform.rotate(Vector3.Y, angY);
        worldTransform.rotate(Vector3.Z, angZ);
    }

    public Object(Sprite obj, float x, float y, float z, float width, float height, float angX,float angY, float angZ, float trans) {

        material = new Material(
                TextureAttribute.createDiffuse(obj.getTexture()),
                new BlendingAttribute(trans),
                FloatAttribute.createAlphaTest(0f)
        );

        obj.setSize(width, height);
        obj.setPosition(-obj.getWidth() * 0.5f, -obj.getHeight() * 0.5f);

        vertices = convert(obj.getVertices());
        indices = new short[]{0, 1, 2, 2, 3, 0};

        worldTransform.trn(x, y, z);
        worldTransform.rotate(Vector3.X, angX);
        worldTransform.rotate(Vector3.Y, angY);
        worldTransform.rotate(Vector3.Z, angZ);
    }

    private static float[] convert(float[] obj) {
        return new float[]{
                obj[Batch.X2], obj[Batch.Y2], 0, 0, 0, 1, obj[Batch.U2], obj[Batch.V2],
                obj[Batch.X1], obj[Batch.Y1], 0, 0, 0, 1, obj[Batch.U1], obj[Batch.V1],
                obj[Batch.X4], obj[Batch.Y4], 0, 0, 0, 1, obj[Batch.U4], obj[Batch.V4],
                obj[Batch.X3], obj[Batch.Y3], 0, 0, 0, 1, obj[Batch.U3], obj[Batch.V3]
        };
    }
}

class WorldBatch extends ObjectSet<Object> implements RenderableProvider, Disposable {

    Renderable renderable;
    Mesh mesh;
    MeshBuilder meshBuilder;
    int maxNumberOfObjects;

    public WorldBatch(int maxNumberOfObjects) {
        this.maxNumberOfObjects = maxNumberOfObjects;
        meshBuilder = new MeshBuilder();
        renderable = new Renderable();
        int maxNumberOfVertices = maxNumberOfObjects * 4;
        int maxNumberOfIndices = maxNumberOfObjects * 6;
        mesh = new Mesh(false, maxNumberOfVertices, maxNumberOfIndices,
                VertexAttribute.Position(), VertexAttribute.Normal(), VertexAttribute.TexCoords(0));
    }

    @Override
    public void getRenderables(Array<Renderable> renderables, Pool<Renderable> pool) {
        meshBuilder.begin(this.mesh.getVertexAttributes());
        meshBuilder.part("objects", GL20.GL_TRIANGLES, renderable.meshPart);
        int i = 0;
        for (Object obj : this) {
            meshBuilder.setVertexTransform(obj.worldTransform);
            meshBuilder.addMesh(obj.vertices, obj.indices);
            if (++i == maxNumberOfObjects) break;
        }

        renderable.material = first().material;

        meshBuilder.end(this.mesh);
        renderables.add(renderable);
    }

    @Override
    public void dispose() {
        mesh.dispose();
    }
}
