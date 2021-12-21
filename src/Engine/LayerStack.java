package Engine;

import java.util.ArrayList;

public class LayerStack 
{
    private ArrayList<Layer> m_Layers = new ArrayList<>();
    
    public void PushLayer(Layer layer)
    {
        m_Layers.add(layer);
        layer.OnAwake();
    }

    public ArrayList<Layer> GetLayers()
    {
        return m_Layers;
    }
}
