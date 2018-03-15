
void main(){
    //@input vec4 color01 The first color we compare. Returns this if equal.
    //@input vec4 color02 The second color we compare.
    //@output vec4 outColor The color we output - should be the input color with the maximum alpha.

    //insert glsl code here
    if( color02.a > color01.a ){
    	outColor = color02;
    }
    else{
    	outColor = color01;
    }
}