package net.eekysam.uhspres.game;

import net.eekysam.uhspres.Config;
import net.eekysam.uhspres.Presentation;
import net.eekysam.uhspres.utils.geo.Point;
import net.eekysam.uhspres.utils.geo.Ray;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

public class CameraView
{
	public float lastXPos;
	public float lastYPos;
	public boolean lastValid = false;
	public Ray cameraRay = null;
	
	public CameraView()
	{
		
	}
	
	public Ray update(Matrix4f veiw, Vector3f pivot)
	{
		float speed = 1.0F;
		if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))
		{
			speed = 5.0F;
		}
		if (!Mouse.isInsideWindow())
		{
			this.lastValid = false;
			return this.cameraRay;
		}
		float x = Mouse.getX() / (float) Presentation.width();
		float y = Mouse.getY() / (float) Presentation.height();
		x *= 2;
		x -= 1;
		y *= 2;
		y -= 1;
		if (!this.lastValid)
		{
			this.lastXPos = x;
			this.lastYPos = y;
			this.lastValid = true;
			return this.cameraRay;
		}
		
		Matrix4f inverse = Matrix4f.invert(veiw, null);
		Vector3f cam0 = this.from4f(Matrix4f.transform(inverse, new Vector4f(0.0F, 0.0F, 0.0F, 1.0F), null));
		Vector3f camx = this.from4f(Matrix4f.transform(inverse, new Vector4f(1.0F, 0.0F, 0.0F, 1.0F), null));
		Vector3f camy = this.from4f(Matrix4f.transform(inverse, new Vector4f(0.0F, 1.0F, 0.0F, 1.0F), null));
		Vector3f camz = this.from4f(Matrix4f.transform(inverse, new Vector4f(0.0F, 0.0F, -1.0F, 1.0F), null));
		
		Vector3f vecx = Vector3f.sub(camx, cam0, null);
		Vector3f vecy = Vector3f.sub(camy, cam0, null);
		Vector3f vecz = Vector3f.sub(camz, cam0, null);
		
		float dx = x - this.lastXPos;
		float dy = y - this.lastYPos;
		float dz = Mouse.getDWheel() * 0.005F;
		if (Mouse.isButtonDown(1))
		{
			Vector3f point = this.from4f(Matrix4f.transform(inverse, new Vector4f(pivot.x, pivot.y, pivot.z, 1.0F), null));
			veiw.translate(point);
			if ((vecz.y > -0.9F || dy > 0) && (vecz.y < 0.2F || dy < 0))
			{
				veiw.rotate(-dy * Config.lookSpeed * Config.lookSpeedY, (new Vector3f(-vecz.z, 0.0F, vecz.x)).normalise(null));
			}
			veiw.rotate(dx * Config.lookSpeed * Config.lookSpeedX, new Vector3f(0.0F, 1.0F, 0.0F));
			veiw.translate(point.negate(null));
		}
		else if (Mouse.isButtonDown(0))
		{
			veiw.translate((Vector3f) (new Vector3f(vecx)).scale(dx * Config.panSpeedX * Config.panSpeed * speed));
			veiw.translate((Vector3f) (new Vector3f(vecy)).scale(dy * Config.panSpeedY * Config.panSpeed * speed));
		}
		veiw.translate((Vector3f) (new Vector3f(vecz)).scale(-dz * Config.panSpeedZ * Config.zoomSpeed));
		this.lastXPos = x;
		this.lastYPos = y;
		this.cameraRay = Ray.getRay(Point.getPoint(cam0.x, cam0.y, cam0.z), Point.getPoint(camz.x, camz.y, camz.z));
		return this.cameraRay;
	}
	
	private Vector3f from4f(Vector4f vec)
	{
		return new Vector3f(vec.x, vec.y, vec.z);
	}
}
