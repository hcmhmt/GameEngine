package simurgh;

import org.lwjgl.BufferUtils;
import renderer.Shader;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class LevelEditorScene extends Scene {

    /*    private boolean changingScene = false;
        private float timeToChangeScene = 2.0f;*/

    private float[] vertexArray = {
            // Position             // Color
            0.5f, -0.5f,  0.0f,      1.0f, 0.0f, 0.0f, 1.0f, // -> Bottom Right (0)
            -0.5f, 0.5f,  0.0f,      0.0f, 1.0f, 0.0f, 1.0f, // -> Top Left     (1)
            0.5f, 0.5f,   0.0f,      0.0f, 0.0f, 1.0f, 1.0f, // -> Top Right    (2)
            -0.5f, -0.5f, 0.0f,      1.0f, 1.0f, 0.0f, 1.0f   // -> Bottom Left  (3)
    };


    // IMPORTANT: Must be in counter-clockwise order
    private int[] elementArray = {
            /*
                    x - 3,2       x - 2

                    x - 3         x - 1, 1
             */

            2, 1, 0, // Top right triangle
            0, 1, 3  // Bottom left triangle

    };

    private int vaoId, vboId, eboId;

    private Shader defaultShader;

    public LevelEditorScene() {
    }

    @Override
    public void init() {
        defaultShader = new Shader("assets/shaders/default.glsl");
        defaultShader.compile();

        // ===========================================
        // Generate VAO, VBO and EBO buffer objects and send to GPU
        // ===========================================
        vaoId = glGenVertexArrays();
        glBindVertexArray(vaoId);

        // Create a float buffer of vertices
        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertexArray.length);
        vertexBuffer.put(vertexArray).flip();

        // Create VBO upload the vertex buffer
        vboId = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboId);
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);

        // Create the indices and upload
        IntBuffer elementBuffer = BufferUtils.createIntBuffer(elementArray.length);
        elementBuffer.put(elementArray).flip();

        eboId = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboId);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, elementBuffer, GL_STATIC_DRAW);

        // Add the vertex attribute pointers
        int positionsSize = 3;
        int colorSize = 4;
        int floatSizeBytes = 4;
        int vertexSizeBytes = (positionsSize + colorSize) * floatSizeBytes;
        glVertexAttribPointer(0, positionsSize, GL_FLOAT, false, vertexSizeBytes, 0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, colorSize, GL_FLOAT, false, vertexSizeBytes, positionsSize * floatSizeBytes);
        glEnableVertexAttribArray(1);
    }

    @Override
    public void update(float dt) {

        defaultShader.use();

        // Bind the VAO that we're using
        glBindVertexArray(vaoId);

        // Enable the vertex attribute pointers
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawElements(GL_TRIANGLES, elementArray.length ,GL_UNSIGNED_INT, 0);

        // Unbind everything
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);

        glBindVertexArray(0);

        defaultShader.detach();

//        System.out.println("" + (1.0f / dt + " FPS"));

/*        if (!changingScene && KeyListener.isKeyPressed(KeyEvent.VK_SPACE)) {
            changingScene = true;
        }

        if (changingScene && timeToChangeScene > 0) {
            timeToChangeScene -= dt;
            Window.get().r -= dt * 5.0f;
            Window.get().g -= dt * 5.0f;
            Window.get().b -= dt * 5.0f;
        } else if (changingScene) {
            Window.changeScene(1);
        }*/

    }


}
