#version 330

uniform sampler2D samp_norm;
uniform sampler2D samp_depth;

uniform vec3 un_light_pos;
uniform vec4 un_light_color;

uniform float un_light_att_con;
uniform float un_light_att_lin;
uniform float un_light_att_quad;

uniform mat4 un_mv;
uniform mat4 un_inv_pmat;

uniform vec2 un_screen_size;

out vec4 out_diffuse;

void main() 
{
	if (gl_FrontFacing)
	{
		discard;
	}
	
	vec2 coord = gl_FragCoord.xy / un_screen_size;
	float depth = texture(samp_depth, coord).r;
	
	vec3 normal = texture(samp_norm, coord).rgb;
	
	vec4 pos = vec4(coord * 2 - 1, depth, 1.0);
	pos = un_inv_pmat * pos;
	pos /= pos.w;
	
	vec3 lpos = (un_mv * vec4(un_light_pos, 1)).xyz;
	
	vec3 lightDir = lpos - pos.xyz;
	float dist = length(lightDir);
	
	float dotl = dot(normal, lightDir / dist);
	
	if (dotl < 0)
	{
		discard;
	}
	
	float att = 1.0 / (un_light_att_con + un_light_att_lin * dist + un_light_att_quad * dist * dist);
    out_diffuse = att * un_light_color * dotl / att;
}