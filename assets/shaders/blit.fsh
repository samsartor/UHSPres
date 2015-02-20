#version 330

uniform sampler2D samp_diffuse;

in vec2 var_texCoord;

out vec4 out_diffuse;

void main()
{
	out_diffuse = texture(samp_diffuse, var_texCoord);
}