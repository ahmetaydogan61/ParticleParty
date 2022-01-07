package Engine.Core;

public abstract class RenderLayer 
{
    public abstract void OnAwake();
    public abstract void OnDetach();
    public abstract void OnRender();
    public abstract void Clear();
}
