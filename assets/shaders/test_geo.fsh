#version 330

uniform sampler2D in_diffuse;
uniform sampler2D in_normal;
uniform sampler2D in_depth;
uniform vec2 clipRange;

varying vec2 var_texCoord;

void main()
{
	float depth = texture2D(in_depth, var_texCoord);
	gl_FragColor = vec4(depth);
	gl_FragColor.w = 1.0;
}