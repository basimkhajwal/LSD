#ifdef GL_ES
    precision mediump float;
#endif

varying vec2 v_texCoords;

uniform vec2 screenSize;
uniform vec2 invScreenSize;
uniform float time;
uniform sampler2D u_texture;

//Psuedo-random number generator
float rand(vec2 co){
    return fract(sin(dot(co.xy ,vec2(12.9898,78.233))) * 43758.5453);
}

void main() {
    //1. APPLY BLUR
    vec2 stepX = vec2(invScreenSize.x, 0);
    vec2 stepY = vec2(0, invScreenSize.y);

    vec4 left = texture2D(u_texture, v_texCoords - stepX);
    vec4 right = texture2D(u_texture, v_texCoords + stepX);
    vec4 top = texture2D(u_texture, v_texCoords - stepY);
    vec4 bottom = texture2D(u_texture, v_texCoords + stepY);
    vec4 middle = texture2D(u_texture, v_texCoords);

    float ratio = 1.0 / 5.0;
    gl_FragColor = (left + middle + right + top + bottom) * ratio;

    //2. APPLY NOISE
    vec4 noise = vec4(rand(v_texCoords * time), rand(v_texCoords * (time + 1.0) ), rand(v_texCoords * (time - 1.0) ), 1);
    gl_FragColor += noise * vec4(0.2, 0.2, 0.2, 0.1);

    //3. VIGNETTE
    //gl_FragColor *= vec4(1,1,1,0.85);
    vec2 position = (gl_FragCoord.xy / screenSize.xy) - vec2(0.5);
    position.x *= screenSize.x / screenSize.y;

    //determine the vector length of the center position
    float len = length(position);

    //Apply
    gl_FragColor *= vec4( vec3(0.8 - len), 1.0 );
}