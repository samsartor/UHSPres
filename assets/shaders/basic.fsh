#version 330

uniform vec4 un_color;
uniform vec4 un_clip;

in float var_depth;
in vec3 var_normal;
out vec4 out_diffuse;

void main()
{
	out_diffuse = un_color;
	if (gl_FragCoord.x < 1 * un_clip.z / 4)
	{
		out_diffuse.rgb *= dot(var_normal, vec3(0.0, 0.0, 1.0));
		return;
	}
	if (gl_FragCoord.x < 2 * un_clip.z / 4)
	{
		out_diffuse.rgb = (var_depth - un_clip.x) / (un_clip.y - un_clip.x);
		return;
	}
	if (gl_FragCoord.x < 3 * un_clip.z / 4)
	{
		out_diffuse.rgb = (var_normal + 1) / 2;
		return;
	}
}