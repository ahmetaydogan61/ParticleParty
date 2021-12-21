package Utils;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import static org.lwjgl.stb.STBImage.stbi_load;
import org.lwjgl.system.MemoryStack;

public class ImageParser 
{
    private ByteBuffer m_Image;
    private int m_Width, m_Height;

    ImageParser(int width, int heigh, ByteBuffer image) 
    {
        m_Image = image;
        m_Height = heigh;
        m_Width = width;
    }

    public ByteBuffer GetImage() 
    {
        return m_Image;
    }

    public int GetWidth() 
    {
        return m_Width;
    }

    public int GetHeight() 
    {
        return m_Height;
    }
    
    public static ImageParser LoadImage(String filePath) 
    {
        ByteBuffer image;
        int width, height;
        try (MemoryStack stack = MemoryStack.stackPush()) 
        {
            IntBuffer comp = stack.mallocInt(1);
            IntBuffer wb = stack.mallocInt(1);
            IntBuffer hb = stack.mallocInt(1);

            image = stbi_load(filePath, wb, hb, comp, 4);
            if (image == null) 
                System.out.println("No Such Image");
            width = wb.get();
            height = hb.get();
        }
        return new ImageParser(width, height, image);
    }
}