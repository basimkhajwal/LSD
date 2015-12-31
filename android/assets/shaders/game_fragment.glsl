#ifdef GL_ES
    precision mediump float;
#endif

varying vec2 v_texCoords;

uniform vec2 screenSize;
uniform vec2 invScreenSize;
uniform float saturation;
uniform float time;
uniform sampler2D u_texture;

//Psuedo-random number generator
float rand(vec2 co){
    return fract(sin(dot(co.xy ,vec2(12.9898,78.233))) * 43758.5453);
}

//RADIUS of our vignette, where 0.5 results in a circle fitting the screen
const float RADIUS = 0.85;

//softness of our vignette, between 0.0 and 1.0
const float SOFTNESS = 0.6;

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
    gl_FragColor += noise * vec4(0.2, 0.2, 0.2, saturation);

    //3. VIGNETTE
    gl_FragColor.a *= 0.8;

    vec2 position = (gl_FragCoord.xy / screenSize.xy) - vec2(0.5);

    //determine the vector length of the center position
    float len = length(position);


    //our vignette effect, using smoothstep
    float vignette = smoothstep(RADIUS, RADIUS-SOFTNESS, len);

    //Apply
    gl_FragColor.rgb = mix(gl_FragColor.rgb, gl_FragColor.rgb * vignette, 0.5);
}