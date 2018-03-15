
void main(){
    //@input vec4 upperColor  The color if above the cutoff.
    //@input vec4 lowerColor The color if below, or equal to, cutoff.
    //@input vec3 position The position of the fragment.
    //@input float cutoff The cutoff point for the fragment.
    //@output vec4 outColor The color we output.

    //insert glsl code here
    if( position.y > cutoff ){
        outColor = upperColor;
    }
    else {
        outColor = lowerColor;
    }

}