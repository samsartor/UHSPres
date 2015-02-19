#version 140

in vec3 var_normal;
in vec4 var_color;

out vec4 out_diffuse; 
out vec3 out_normal;

void main()
{
	out_diffuse = var_color;
	out_normal = var_normal;
}