#ifdef GL_ES
    precision mediump float;
#endif

varying vec2 v_texCoords;

uniform vec2 invScreenSize;
uniform float time;
uniform sampler2D u_texture;

float rand(vec2 co){
    return fract(sin(dot(co.xy ,vec2(12.9898,78.233))) * 43758.5453);
}

void main() {
    vec2 stepX = vec2(invScreenSize.x, 0);

    vec4 left = texture2D(u_texture, v_texCoords - stepX);
    vec4 right = texture2D(u_texture, v_texCoords + stepX);
    vec4 middle = texture2D(u_texture, v_texCoords);

    float ratio = 1.0 / 3.0;

    vec4 noise = vec4(rand(v_texCoords)) * 2.0 * time;

    gl_FragColor = noise * 0.1 + (left + middle + right) * ratio ;
    gl_FragColor *= vec4(1,1,1,0.95);

    gl_FragColor = vec4(1,time,1,1);
}