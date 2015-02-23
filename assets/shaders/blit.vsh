#version 330

layout(location = 0) in vec3 in_vertex;
out vec2 var_texCoord;

void main()
{
	gl_Position = vec4(in_vertex, 1.0);
	var_texCoord.x = (in_vertex.x + 1.0) / 2.0;
	var_texCoord.y = (in_vertex.y + 1.0) / 2.0;
}