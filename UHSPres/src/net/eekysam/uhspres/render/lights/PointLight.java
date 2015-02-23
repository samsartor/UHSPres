package net.eekysam.uhspres.render.lights;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import net.eekysam.uhspres.Presentation;
import net.eekysam.uhspres.asset.GameAsset;
import net.eekysam.uhspres.asset.OBJLoader;
import net.eekysam.uhspres.render.Transform;
import net.eekysam.uhspres.render.Transform.MatrixMode;
import net.eekysam.uhspres.render.shader.Program;
import net.eekysam.uhspres.render.shader.ShaderUniform;
import net.eekysam.uhspres.render.verts.VertexArray;
import net.eekysam.uhspres.render.verts.VertexBuffer;
import net.eekysam.uhspres.utils.graphics.GLUtils;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

public class PointLight extends Light
{
	public static VertexArray icosVAO;
	public static VertexBuffer icosPosBuf;
	public static VertexBuffer icosIndBuf;
	
	public Vector3f position;
	public Vector4f color;
	public float areaScale;
	
	public float attCon;
	public float attLin;
	public float attQuad;
	
	public PointLight(Vector3f position, Vector4f color, float areaScale, float attCon, float attLin, float attQuad)
	{
		super();
		this.position = position;
		this.color = color;
		this.areaScale = areaScale;
		this.attCon = attCon;
		this.attLin = attLin;
		this.attQuad = attQuad;
	}
	
	public void uploadUniforms(Program program)
	{
		ShaderUniform un = new ShaderUniform();
		un.setVector(this.position);
		un.upload(program, "un_light_pos");
		un.setVector(this.color);
		un.upload(program, "un_light_color");
		un.setFloat(this.areaScale);
		un.upload(program, "un_area_scale");
		un.setFloat(this.attCon);
		un.upload(program, "un_light_att_con");
		un.setFloat(this.attLin);
		un.upload(program, "un_light_att_lin");
		un.setFloat(this.attQuad);
		un.upload(program, "un_light_att_quad");
		un.setFloats(Presentation.width(), Presentation.height());
		un.upload(program, "un_screen_size");
	}
	
	public static void uploadCommonUniforms(Transform view, Program program)
	{
		ShaderUniform un = new ShaderUniform();
		un.setMatrix(view.getMVP());
		un.upload(program, "un_mvp");
		un.setMatrix(Matrix4f.invert(view.get(MatrixMode.PROJECT), null));
		un.upload(program, "un_inv_pmat");
		un.setMatrix(view.getResult(0b011));
		un.upload(program, "un_mv");
	}
	
	public static void createIcos()
	{
		GameAsset testAsset = new GameAsset("icos.obj");
		ArrayList<Float> testPos = new ArrayList<Float>();
		ArrayList<Integer> testInd = new ArrayList<Integer>();
		OBJLoader loader = new OBJLoader(testAsset, testInd, testPos, 3);
		loader.load();
		float[] pos = GLUtils.floatArray(testPos);
		int[] ind = GLUtils.intArray(testInd);
		FloatBuffer testPosBufVals = GLUtils.bufferFloats(pos);
		IntBuffer testIndBufVals = GLUtils.bufferInts(ind);
		icosVAO = new VertexArray();
		icosVAO.create();
		icosPosBuf = new VertexBuffer(false);
		icosPosBuf.create();
		icosIndBuf = new VertexBuffer(true);
		icosIndBuf.create();
		icosPosBuf.vertexData(testPosBufVals);
		icosIndBuf.vertexData(testIndBufVals);
		icosVAO.bind();
		VertexArray.enableAttrib(0);
		icosPosBuf.attribPointer(0, 3, GL11.GL_FLOAT, false, 0, 0);
		icosVAO.unbind();
	}
}
