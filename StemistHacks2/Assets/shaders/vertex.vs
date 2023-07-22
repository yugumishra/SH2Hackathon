#version 330 core

layout(location = 0) in vec3 pos;
layout(location = 1) in vec3 normall;
layout(location = 2) in vec3 textureCoordss;

out vec3 Normal;
out vec3 textureCoords;
out vec3 FragPos;
out vec3 LightPos;

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform vec3 lightPos;
uniform mat4 modelMatrix;

void main() {
    gl_Position = projectionMatrix * viewMatrix * modelMatrix * vec4(pos, 1.0);
    FragPos = vec3(viewMatrix * modelMatrix * vec4(pos,1.0));
    Normal = mat3(transpose(inverse(viewMatrix * modelMatrix))) * normall;
    textureCoords = textureCoordss;
    LightPos = vec3(viewMatrix * vec4(lightPos, 1.0));
}