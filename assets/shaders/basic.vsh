#version 330

uniform mat4 un_mvp;
uniform mat4 un_mv;

layout(location = 0) in vec3 in_vertex;
layout(location = 1) in vec3 in_normal;
out float var_depth;
out vec3 var_normal;

void main()
{
    gl_Position = un_mvp * vec4(in_vertex, 1);
	var_depth = gl_Position.z;
	var_normal = un_mv * vec4(in_normal, 0.0);
}