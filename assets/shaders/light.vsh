#version 330

layout(location = 0) in vec3 in_vertex;

uniform mat4 un_mvp;
uniform vec3 un_light_pos;
uniform float un_area_scale;

void main()
{
	vec3 vert = in_vertex;
	vert *= un_area_scale;
	vert += un_light_pos;
    vec4 pos = un_mvp * vec4(vert, 1);
	gl_Position = pos;
}