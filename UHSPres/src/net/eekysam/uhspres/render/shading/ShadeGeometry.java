package net.eekysam.uhspres.render.shading;

import net.eekysam.uhspres.render.RenderEngine;
import net.eekysam.uhspres.render.fbo.EnumDrawBufferLocs;
import net.eekysam.uhspres.render.fbo.GeometryFBO;
import net.eekysam.uhspres.render.shader.Program;
import net.eekysam.uhspres.render.shader.ShaderUniform;

import org.lwjgl.util.vector.Matrix4f;

public class ShadeGeometry extends ShadePass
{
	public final Program geo;
	public final ShaderUniform mvMatrix;
	public final ShaderUniform mvpMatrix;
	
	public ShadeGeometry(RenderEngine engine)
	{
		super(engine);
		this.geo = new Program(EnumDrawBufferLocs.DIFFUSE, EnumDrawBufferLocs.NORMAL);
		this.mvMatrix = new ShaderUniform("un_mv");
		this.mvpMatrix = new ShaderUniform("un_mvp");
	}
	
	@Override
	public void doCreate()
	{
		this.geo.create();
		this.shaders.geoV.attach(this.geo);
		this.shaders.geoF.attach(this.geo);
		this.engine.linkProgram(this.geo, this.shaders.geoA.file);
	}
	
	@Override
	public void doDelete()
	{
		this.shaders.geoV.detach(this.geo);
		this.shaders.geoF.detach(this.geo);
		this.geo.delete();
	}
	
	public void start(GeometryFBO fbo)
	{
		this.geo.bind();
		fbo.bind();
	}
	
	public void uploadMV(Matrix4f mat)
	{
		this.mvMatrix.setMatrix(mat);
		this.uploadMV();
	}
	
	public void uploadMV()
	{
		this.mvMatrix.upload(this.geo);
	}
	
	public void uploadMVP(Matrix4f mat)
	{
		this.mvpMatrix.setMatrix(mat);
		this.uploadMVP();
	}
	
	public void uploadMVP()
	{
		this.mvpMatrix.upload(this.geo);
	}
	
	public void end(GeometryFBO fbo)
	{
		this.geo.unbind();
		fbo.unbind();
	}
}
