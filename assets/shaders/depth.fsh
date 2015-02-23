#version 330

out float out_value;

void main()
{
	out_value = gl_FragCoord.z;
}