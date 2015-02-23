#version 330

uniform sampler2D samp_value;
uniform vec4 un_base;

in vec2 var_texCoord;
out vec4 out_diffuse;

void main()
{
	out_diffuse = un_base;
	out_diffuse.rgb *= texture(samp_value, var_texCoord).r;
}