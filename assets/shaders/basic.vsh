#version 330

uniform mat4 un_mvp;

layout(location = 0) in vec3 in_vertex;
out float var_depth;

void main()
{
    gl_Position = un_mvp * vec4(in_vertex, 1);
	var_depth = gl_Position.z;
}