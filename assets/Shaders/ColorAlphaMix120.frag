
void main(){
    //@input vec4 color1 The color to be "layered on top". We use this alpha value.
    //@input vec4 color2 The color to be "layered below".
    //@output vec4 outColor The final color.

    //insert glsl code here
    outColor = mix(color2, color1, color1.a);
}