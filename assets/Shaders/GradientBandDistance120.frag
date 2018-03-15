
void main(){
    //@input vec2 texCoord The texture coordinate of this pixel/fragment/whatever.
    //@input float bandMid The middle point of the band. Should be expressed in texture coordinates.
    //@input float bandWidth The width of the band. Should also be expressed in texture coordinates.
    //@output float distance The distance of the fragment from the band, normalized to between 0 or 1.

    //insert glsl code here
    distance = texCoord.y - (bandMid - (bandWidth / 2.0));
    distance /= bandMid + (bandWidth / 2.0);
    distance = min( max(distance, 0.0), 1.0 );
}