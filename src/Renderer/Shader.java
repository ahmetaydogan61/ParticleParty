package Renderer;

import static org.lwjgl.opengl.GL20.*;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;

public class Shader
{
	private int m_RendererID;
	private String m_VertexSource;
	private String m_FragmentSource;
	private Map<String, Integer> m_UniformLocationCache = new HashMap<String, Integer>();
	public String filePath;

	public Shader(String filePath)
	{
		this.filePath = filePath;
		try
		{
			String source = new String(Files.readAllBytes(Paths.get(filePath)));
			String[] splitString = source.split("(#shader)( )+([a-zA-Z]+)");

			int index = source.indexOf("#shader") + 7;
			int eol = source.indexOf("\r\n", index);
			String firstPattern = source.substring(index, eol).trim();

			index = source.indexOf("#shader", eol) + 7;
			eol = source.indexOf("\r\n", index);
			String secondPattern = source.substring(index, eol).trim();

			if (firstPattern.equals("vertex"))
				m_VertexSource = splitString[1];
			else if (firstPattern.equals("fragment"))
				m_FragmentSource = splitString[1];
			else
				throw new IOException("No such shader " + filePath);

			if (secondPattern.equals("vertex"))
				m_VertexSource = splitString[2];
			else if (secondPattern.equals("fragment"))
				m_FragmentSource = splitString[2];
			else
				throw new IOException("No such shader" + filePath);
		}
		catch (IOException e)
		{
			e.printStackTrace();
			assert false : "Error: Shader file couldnt open";
		}
		m_RendererID = CreateShader();
	}

	public void Destroy()
	{
		glDeleteProgram(m_RendererID);
	}

	private int CompileShader(int type, String source)
	{
		int id = glCreateShader(type);
		glShaderSource(id, source);
		glCompileShader(id);

		IntBuffer result = BufferUtils.createIntBuffer(1);
		glGetShaderiv(id, GL_COMPILE_STATUS, result);
		if (result.get(0) == GL_FALSE)
		{
			int length = glGetShaderi(id, GL_COMPILE_STATUS);
			String message = glGetShaderInfoLog(id, length);
			System.out.println("Failed to Compile Shader " + (type == GL_VERTEX_SHADER ? "vertex" : "fragment"));
			System.out.println(message);
			glDeleteShader(id);
			return 0;
		}
		return id;
	}

	public int CreateShader()
	{
		int program = glCreateProgram();
		int vs = CompileShader(GL_VERTEX_SHADER, m_VertexSource);
		int fs = CompileShader(GL_FRAGMENT_SHADER, m_FragmentSource);

		glAttachShader(program, vs);
		glAttachShader(program, fs);
		glLinkProgram(program);
		glValidateProgram(program);

		glDeleteShader(vs);
		glDeleteShader(fs);
		return program;
	}

	public void Bind()
	{
		glUseProgram(m_RendererID);
	}

	public void Unbind()
	{
		glUseProgram(0);
	}

	public void UploadUniform4f(String name, float v0, float v1, float v2, float v3)
	{
		glUniform4f(GetUnfiformLocation(name), v0, v1, v2, v3);
	}

	public void UploadUniform1i(String name, int value)
	{
		glUniform1i(GetUnfiformLocation(name), value);
	}

	public void UploadUniformMat4f(String name, Matrix4f mat4)
	{
		FloatBuffer matBuffer = BufferUtils.createFloatBuffer(16);
		mat4.get(matBuffer);
		glUniformMatrix4fv(GetUnfiformLocation(name), false, matBuffer);
	}

	private int GetUnfiformLocation(String name)
	{
		if(m_UniformLocationCache.containsKey(name))
			return m_UniformLocationCache.get(name);

		int location = glGetUniformLocation(m_RendererID, name);

		if(location == -1)
			System.out.println("Warning: Uniform: " + name + " does not exist!");

		m_UniformLocationCache.put(name, location);
		return location;
	}
}
