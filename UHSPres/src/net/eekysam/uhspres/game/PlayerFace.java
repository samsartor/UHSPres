package net.eekysam.uhspres.game;

import net.eekysam.uhspres.Config;
import net.eekysam.uhspres.utils.geo.Point;
import net.eekysam.uhspres.utils.geo.Ray;

import org.lwjgl.input.Mouse;
import org.lwjgl.util.glu.GLU;

public class PlayerFace
{
	public final static float hdamp = 0.9F;
	
	protected float camPitch = 30.0F;
	protected float camYaw = 45.0F;
	protected float camDist = 16.0F;
	
	protected Point camFocus;
	protected Point camLoc;
	
	private int mouselastx;
	private int mouselasty;
	private float aspect;
	private float znear = 1.0F;
	private float fovy = 60.0F;
	
	public PlayerFace()
	{
		this.reset();
		this.camFocus = Point.getPoint(0.0D, 128.0D, 0.0D);
		this.camLoc = Point.getPoint(0.0D, 0.0D, 0.0D);
		this.doFocus();
		this.updatePars();
	}
	
	public void updatePars()
	{
		float width = Config.getWidth();
		float height = Config.getHeight();
		this.aspect = width / height;
	}
	
	public float getFOVY()
	{
		return this.fovy;
	}
	
	public float getZNear()
	{
		return this.znear;
	}
	
	public float getAspect()
	{
		return this.aspect;
	}
	
	public void doGLULook()
	{
		GLU.gluLookAt((float) this.camLoc.xCoord, (float) this.camLoc.yCoord, (float) this.camLoc.zCoord, (float) this.camFocus.xCoord, (float) this.camFocus.yCoord, (float) this.camFocus.zCoord, 0.0F, 1.0F, 0.0F);
	}
	
	public void reset()
	{
		this.mouselastx = Mouse.getX();
		this.mouselasty = Mouse.getY();
	}
	
	public Point getFocus()
	{
		return this.camFocus;
	}
	
	public Point getLocation()
	{
		return this.camLoc;
	}
	
	public Ray pickCamera()
	{
		return Ray.getRay(this.camFocus, this.camLoc);
	}
	
	public void tickCamera()
	{
		int wm = Mouse.getDWheel();
		if (Mouse.isButtonDown(2))
		{
			this.camYaw -= (Mouse.getX() - this.mouselastx) * Config.mouseSpeedX;
			this.camPitch -= (Mouse.getY() - this.mouselasty) * Config.mouseSpeedY;
			if (this.camPitch < 10.0F)
			{
				this.camPitch = 10.0F;
			}
			if (this.camPitch > 90.0F)
			{
				this.camPitch = 90.0F;
			}
			this.camYaw %= 360.0F;
		}
		else if (wm != 0)
		{
			this.camDist -= wm * Config.scrollSpeed;
			if (this.camDist < Config.camDistMin)
			{
				this.camDist = Config.camDistMin;
			}
			if (this.camDist > Config.camDistMax)
			{
				this.camDist = Config.camDistMax;
			}
		}
		if (Mouse.isButtonDown(0))
		{
			float dx = (Mouse.getX() - this.mouselastx) * 0.002F * this.camDist;
			float dy = (Mouse.getY() - this.mouselasty) * 0.002F * this.camDist;
			float angrad = this.camYaw / 180.0F * 3.14F;
			float cos = (float) Math.cos(angrad);
			float sin = (float) Math.sin(angrad);
			double dX = 0.0D;
			double dZ = 0.0D;
			dX -= dx * cos;
			dX += dy * sin;
			dZ += dy * cos;
			dZ += dx * sin;
			this.camFocus.moveDelta(dX, 0.0D, dZ);
		}
		this.doFocus();
		this.mouselastx = Mouse.getX();
		this.mouselasty = Mouse.getY();
	}
	
	public void reverseFocus()
	{
		double dx = this.camLoc.xCoord - this.camFocus.xCoord;
		double dy = this.camLoc.yCoord - this.camFocus.yCoord;
		double dz = this.camLoc.zCoord - this.camFocus.zCoord;
		double dist = Math.sqrt(dx * dx + dy * dy + dz * dz);
		this.camDist = (float) dist;
		dx /= this.camDist;
		dy /= this.camDist;
		dz /= this.camDist;
		float pitchrad = (float) Math.asin(dy);
		this.camPitch = (pitchrad / 3.14F) * 180.0F;
		double scalev = Math.cos(pitchrad);
		dx /= scalev;
		dz /= scalev;
		float yawrad = (float) Math.asin(dx);
		this.camYaw = (yawrad / 3.14F) * 180.0F;
	}
	
	public void setFocus(Point point)
	{
		this.camFocus = point;
		this.doFocus();
	}
	
	public void doFocus()
	{
		float yawrad = this.camYaw / 180.0F * 3.14F;
		float pitchrad = this.camPitch / 180.0F * 3.14F;
		
		double scalev = Math.cos(pitchrad);
		
		double dx = this.camDist * Math.sin(yawrad) * scalev;
		double dz = this.camDist * Math.cos(yawrad) * scalev;
		
		double dy = this.camDist * Math.sin(pitchrad);
		
		dx += this.camFocus.xCoord;
		dy += this.camFocus.yCoord;
		dz += this.camFocus.zCoord;
		
		this.camLoc.setPoint(dx, dy, dz);
	}
	
	public final float getCamPitch()
	{
		return this.camPitch;
	}
	
	public final void setCamPitch(float camPitch)
	{
		this.camPitch = camPitch;
	}
	
	public final float getCamYaw()
	{
		return this.camYaw;
	}
	
	public final void setCamYaw(float camYaw)
	{
		this.camYaw = camYaw;
	}
	
	public final float getCamDist()
	{
		return this.camDist;
	}
	
	public final void setCamDist(float camDist)
	{
		this.camDist = camDist;
	}
}
