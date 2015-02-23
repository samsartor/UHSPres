#version 330

layout(location = 0) in vec3 in_vertex;
layout(location = 1) in vec3 in_normal;
layout(location = 2) in vec2 in_uv;

out vec3 var_normal;
out vec2 var_uv;

uniform mat4 un_mvp;
uniform mat4 un_mv;

void main()
{
    gl_Position = un_mvp * vec4(in_vertex, 1);
	var_uv = in_uv;
	var_normal = (un_mv * vec4(in_normal, 0.0)).xyz;
}