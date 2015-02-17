#version 330

varying vec3 var_normal;

out vec4 out_diffuse; 
out vec3 out_normal;

void main()
{
	out_diffuse = gl_Color;
	out_normal = var_normal;
}