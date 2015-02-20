#version 330

uniform vec4 un_color;

out vec4 out_diffuse;

in float var_depth;

void main()
{
	out_diffuse = un_color;
	out_diffuse.rgb *= var_depth;
}