#version 330

layout(location = 0) in vec3 in_vertex;

uniform mat4 un_mvp;

out float var_depth;

void main()
{
    gl_Position = vec4(in_vertex, 1);
	gl_Position.x = gl_Position.x / 3;
	gl_Position.y = gl_Position.y / 3;
	gl_Position.z = gl_Position.z / 3;
	var_depth = gl_Position.z;
}