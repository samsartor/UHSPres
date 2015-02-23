#version 330

layout(location = 0) in vec3 in_vertex;

uniform mat4 un_mvp;

void main()
{
    gl_Position = un_mvp * vec4(in_vertex, 1);
}