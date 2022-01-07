package Engine.Utils;

import java.util.Random;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class Maths 
{
    public static float Clamp(float value, float min, float max)
    {
        if(value < min)
            return min;
        else if(value > max)
            return max;
        else
            return value;
    }
    
    public static int Clamp(int value, int min, int max)
    {
        if(value < min)
            return min;
        else if(value > max)
            return max;
        else
            return value;
    }  

    public static float Lerp(float from, float to, float t)
    {
        return (1 - t) * from + t * to;
    }

    public static Vector2f Lerp(Vector2f from, Vector2f to, float t)
    {
        Vector2f vec = new Vector2f();
        vec.x = Lerp(from.x, to.x, t);
        vec.y = Lerp(from.y, to.y, t);
        return vec;
    }

    public static Vector3f Lerp(Vector3f from, Vector3f to, float t)
    {
        Vector3f vec = new Vector3f();
        vec.x = Lerp(from.x, to.x, t);
        vec.y = Lerp(from.y, to.y, t);
        vec.z = Lerp(from.z, to.z, t);
        return vec;
    }

    public static Vector4f Lerp(Vector4f from, Vector4f to, float t)
    {
        Vector4f vec = new Vector4f();
        vec.x = Lerp(from.x, to.x, t);
        vec.y = Lerp(from.y, to.y, t);
        vec.z = Lerp(from.z, to.z, t);
        vec.w = Lerp(from.w, to.w, t);
        return vec;
    }

    public static float RandomFloat(float min, float max)
    {
        Random rand = new Random();
        return min + rand.nextFloat() * (max - min);
    }
}
