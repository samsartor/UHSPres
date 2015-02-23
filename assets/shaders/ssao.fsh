#version 330

uniform sampler2D samp_norm;
uniform sampler2D samp_depth;
uniform sampler2D samp_noise;
uniform vec2 u_noise_scale;
const int MAX_SAMPLE_SIZE = 128;
uniform vec3[MAX_SAMPLE_SIZE] un_samples;
uniform int un_samp_size;
uniform float un_radius;
uniform mat4 un_pmat;
uniform mat4 un_inv_pmat;

in vec2 var_texCoord;
out float out_value;

void main() 
{
	float depth = texture(samp_depth, var_texCoord).r;
	
	if (depth == 1.0)
	{
		discard;
	}
	
	vec3 normal = texture(samp_norm, var_texCoord).rgb;
	
	vec4 origin = vec4(var_texCoord * 2 - 1, depth, 1.0);
	origin = un_inv_pmat * origin;
	origin /= origin.w;
	
	float occlusion = 0.0;
	float div = 0;
	
	vec2 rvec = texture(samp_noise, var_texCoord * u_noise_scale).rg * 2.0 - 1.0;
	vec2 rone = vec2(rvec.x);
	rone.x = sqrt(1 - rone.y * rone.y);
	vec2 rtwo = vec2(rvec.y);
	rtwo.x = sqrt(1 - rtwo.y * rtwo.y);
	
	
	mat3 tbn = mat3(vec3(1, 0, 0), vec3(0, 1, 0), vec3(0, 0, 1));
	tbn *= mat3(vec3(1, 0, 0), vec3(0, rone.x, -rone.y), vec3(0, rone.y, rone.x));
	tbn *= mat3(vec3(rtwo.x, -rtwo.y, 0), vec3(rtwo.y, rtwo.x, 0), vec3(0, 0, 1));
	
	for (int i = 0; i < un_samp_size; ++i) 
	{
		// get sample position:
		vec3 sample = tbn * un_samples[i];
		
		if (dot(sample, normal) > 0)
		{
			div += 1;
			sample = sample * un_radius + origin.xyz;
			
			// project sample position:
			vec4 offset = vec4(sample, 1.0);
			offset = un_pmat * offset;
			offset.xy /= offset.w;

			vec4 pick =  un_inv_pmat * vec4(offset.xy, texture(samp_depth, offset.xy * 0.5 + 0.5).r, 1.0);
			pick /= pick.w;
			 
			// range check & accumulate:
			float rangeCheck = abs(origin.z - pick.z) < un_radius ? 1.0 : 0.0;
			occlusion += (pick.z >= sample.z ? 1.0 : 0.0) * rangeCheck;
		}
	}
	
	out_value = 1.0 - (occlusion / div);
}