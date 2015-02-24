#version 330

uniform sampler2D samp_texture;

in vec3 var_normal;
in vec2 var_uv;

out vec4 out_diffuse; 
out vec3 out_normal;

void main()
{
	out_diffuse = texture(samp_texture, var_uv);
	//out_diffuse.rgb = var_normal * 0.5 + 0.5;
	out_normal = var_normal;
}