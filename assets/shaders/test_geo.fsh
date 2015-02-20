#version 330

uniform sampler2D samp_diffuse;
uniform sampler2D samp_normal;
uniform sampler2D samp_depth;

in vec2 var_texCoord;

out vec4 out_diffuse;

void main()
{
	out_diffuse = texture2D(samp_diffuse, var_texCoord);
}