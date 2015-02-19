#version 140

layout(location = 0) in vec3 in_vertex;

out vec2 var_texCoord;

void main()
{
	gl_Position = vec4(in_vertex, 1.0);
	var_texCoord.x = (gl_Vertex.x + 1.0) / 2.0;
	var_texCoord.y = (gl_Vertex.y + 1.0) / 2.0;
}