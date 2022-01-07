#shader vertex
#version 330 core
layout (location = 0) in vec2 aPos;
layout (location = 1) in vec2 aOffset;
layout (location = 2) in vec3 aColor;
layout (location = 3) in vec2 aSize;

out vec3 fColor;

uniform mat4 u_VP;

void main()
{
    fColor = aColor;
    gl_Position = u_VP * vec4((aPos * aSize) + aOffset, 0.0, 1.0);
}

#shader fragment
#version 330 core
out vec4 FragColor;

in vec3 fColor;

void main()
{
    FragColor = vec4(fColor, 1.0);
}