#version 330

varying vec3 var_normal;

void main()
{
    gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex;
	gl_FrontColor = gl_Color;
	gl_BackColor = gl_Color;
	var_normal = normalize(gl_NormalMatrix * gl_Normal);
}