#version 330

uniform sampler2D samp_value;
uniform vec2 un_dir;
uniform float un_radius;
uniform vec2 un_texel;
uniform float un_radImpact;

in vec2 var_texCoord;
out float out_value;

void main() 
{
    float sum = vec4(0.0);
    float strength = 0.0;
    for (float r = -un_radius; r <= un_radius; r += 1.0) 
	{
        float sample = texture(samp_value, var_texCoord + un_texel * r * un_dir);
        float s = 1.0 - abs(r / un_radius) * un_radImpact;
        strength += s;
        sum += sample * s;
    }
    out_value = sum / strength;
}