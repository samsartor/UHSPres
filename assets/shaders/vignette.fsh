#version 330

in vec2 var_texCoord;
out vec4 out_diffuse;

uniform float un_outside;
uniform float un_inside;
uniform vec4 un_color;

void main() 
{
	vec2 pos = var_texCoord * 2 - 1;
	float len = length(pos);
	out_diffuse = un_color;
	out_diffuse.a *= smoothstep(un_inside, un_outside, len);
}