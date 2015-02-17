#version 330

varying vec2 var_texCoord;

void main()
{
	gl_Position = gl_Vertex;
	var_texCoord.x = (gl_Vertex.x + 1) / 2;
	var_texCoord.y = (gl_Vertex.y + 1) / 2;
}