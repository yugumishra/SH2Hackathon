#version 330 core

layout(location = 0) in vec3 pos;
layout(location = 1) in vec3 normall;
layout(location = 2) in vec2 textureCoordss;

out vec3 posi;
out vec3 normal;
out vec2 textureCoords;

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;

void main() {
    gl_Position = projectionMatrix * viewMatrix * vec4(pos, 1.0);
    posi = pos;
    normal = normall;
    textureCoords = textureCoordss;
}