#version 330

uniform sampler2D samp_norm;
uniform sampler2D samp_depth;

uniform vec3 un_light_pos;
uniform vec4 un_light_color;
uniform vec4 un_light_spec;
uniform float un_light_power;
uniform float un_shin;

uniform mat4 un_mv;
uniform mat4 un_inv_pmat;

out vec4 out_diffuse;
in vec2 var_texCoord;

void main() 
{	
	float depth = texture(samp_depth, var_texCoord).r;
	
	vec3 normal = texture(samp_norm, var_texCoord).rgb;
	
	vec4 pos = vec4(var_texCoord * 2 - 1, depth, 1.0);
	pos = un_inv_pmat * pos;
	pos /= pos.w;
	
	vec3 lpos = (un_mv * vec4(un_light_pos, 1)).xyz;
	
	vec3 lightDir = lpos - pos.xyz;
	float dist = length(lightDir);
	
	float dotl = dot(normal, lightDir / dist);
	
	vec3 view = normalize(pos.xyz);
	
	if (dotl > 0.0)
	{
		float dotr = max(dot(reflect(lightDir / dist, normal), view), 0);
		out_diffuse = un_light_color * un_light_power * dotl + un_light_spec * un_light_power * pow(dotr , un_shin);
	}
}