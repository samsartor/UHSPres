package net.eekysam.uhspres.render.lights;

import net.eekysam.uhspres.render.Transform;
import net.eekysam.uhspres.render.Transform.MatrixMode;
import net.eekysam.uhspres.render.shader.Program;
import net.eekysam.uhspres.render.shader.ShaderUniform;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

public class PointLight extends Light
{
	public Vector3f position;
	public Vector4f color;
	public Vector4f spec;
	public float power;
	public float shin;
	
	public float attCon;
	public float attLin;
	public float attQuad;
	
	public PointLight(Vector3f position, Vector4f color, float spec, float power, float shin)
	{
		this.position = position;
		this.color = color;
		this.spec = this.color;
		this.spec.scale(spec);
		this.spec.z = this.color.z;
		this.power = power;
		this.shin = shin;
	}
	
	public PointLight(Vector3f position, Vector4f color, Vector4f spec, float power, float shin)
	{
		super();
		this.position = position;
		this.spec = spec;
		this.color = color;
		this.power = power;
		this.shin = shin;
	}
	
	public void uploadUniforms(Program program)
	{
		ShaderUniform un = new ShaderUniform();
		un.setVector(this.position);
		un.upload(program, "un_light_pos");
		un.setVector(this.color);
		un.upload(program, "un_light_color");
		un.setVector(this.spec);
		un.upload(program, "un_light_spec");
		un.setFloat(this.power);
		un.upload(program, "un_light_power");
		un.setFloat(this.shin);
		un.upload(program, "un_shin");
	}
	
	public static void uploadCommonUniforms(Transform view, Program program)
	{
		ShaderUniform un = new ShaderUniform();
		un.setMatrix(Matrix4f.invert(view.get(MatrixMode.PROJECT), null));
		un.upload(program, "un_inv_pmat");
		un.setMatrix(view.getResult(0b011));
		un.upload(program, "un_mv");
	}
}
