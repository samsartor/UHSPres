#version 330

in vec3 var_normal;
in vec4 var_color;

layout(location = 0) out vec4 out_diffuse; 
layout(location = 1) out vec3 out_normal;

void main()
{
	out_diffuse = var_color;
	//out_diffuse.rgb = var_normal * 0.5 + 0.5;
	out_normal = var_normal;
}