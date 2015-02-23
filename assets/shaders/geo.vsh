#version 330

layout(location = 0) in vec3 in_vertex;
layout(location = 1) in vec3 in_normal;

out vec3 var_normal;
out vec4 var_color;

uniform mat4 un_mvp;
uniform mat4 un_mv;

void main()
{
    gl_Position = un_mvp * vec4(in_vertex, 1);
	var_color = vec4(1, 1, 1, 1);
	var_normal = (un_mv * vec4(in_normal, 0.0)).xyz;
}