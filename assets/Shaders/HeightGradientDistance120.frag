
void main(){
    //@input vec3 position The position of this pixel/fragment/whatever.
    //@input float bandMid The middle of the gradient band's position. In model terms?
    //@input float bandWidth The width of the gradient band. In model coordinates? 
    //@output float distance The distance of the fragment/pixel from the band. Normalized to 0.0 - 1.0.

    //insert glsl code here
    distance = position.y - (bandMid - (bandWidth / 2.0));
    distance /= bandMid + (bandWidth / 2.0);
    distance = min( max(distance, 0.0), 1.0 );
}