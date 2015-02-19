#version 140

layout(location = 0) in vec3 in_vertex;
layout(location = 3) in vec3 in_normal;
layout(location = 6) in vec4 in_color;

out vec3 var_normal;
out vec4 var_color;

uniform mat4 un_mvp;

void main()
{
    gl_Position = un_mvp * vec4(in_vertex, 1);
	var_color = in_color;
	var_normal = normalize(gl_NormalMatrix * gl_Normal);
}