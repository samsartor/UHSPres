#version 330

uniform sampler2D samp_cam_depth;
uniform sampler2D samp_shad_depth;
uniform mat4 un_camToShad;

in vec2 var_texCoord;
layout(location = 0) out float out_occlusion;

void main()
{
	vec4 pos = vec4(var_texCoord, texture(samp_cam_depth, var_texCoord).r, 1);
	vec4 spos = un_camToShad * pos;
	vec3 scoord = (spos.xyz + 1) / 2;
	float occlusion = 0.0;
	float sdepth = texture(samp_shad_depth, scoord.xy);
	if (sdepth < scoord.z)
	{
		occlusion += 1;
	}
	out_occlusion = 1 - occlusion;
}