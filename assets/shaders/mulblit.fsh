#version 330

uniform sampler2D samp_value;
uniform sampler2D samp_diffuse;

uniform float un_strength = 1;

in vec2 var_texCoord;
out vec4 out_diffuse;

void main() 
{
	out_diffuse = texture(samp_diffuse, var_texCoord);
	out_diffuse.rgb *= (1 - un_strength) + texture(samp_value, var_texCoord).r * un_strength;
}