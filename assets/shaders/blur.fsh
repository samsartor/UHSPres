#version 330

uniform sampler2D samp_diffuse;
uniform vec2 un_dir;
uniform float un_radius;
uniform vec2 un_texel;
uniform float un_radImpact;

in vec2 var_texCoord;
out vec4 out_diffuse;

void main() 
{
    vec4 sum = vec4(0.0);
    float strength = 0.0;
    for (float r = -un_radius; r <= un_radius; r += 1.0) 
	{
        vec4 sample = texture(samp_diffuse, var_texCoord + un_texel * r * un_dir);
        float s = 1.0 - abs(r / un_radius) * un_radImpact;
        strength += s;
        sum += sample * s;
    }
    out_diffuse = sum / strength;
}