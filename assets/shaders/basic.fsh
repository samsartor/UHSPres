#version 330

uniform vec4 un_color;
uniform vec2 un_clip;

in float var_depth;
out vec4 out_diffuse;

void main()
{
	out_diffuse = un_color;
	out_diffuse.rgb *= (var_depth + un_clip.x) / (un_clip.y - un_clip.x);
}