#version 330 core

layout(location = 0) in vec3 pos;

out vec3 posi;

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;

void main() {
    gl_Position = projectionMatrix * viewMatrix * vec4(pos, 1.0);
    posi = pos;
}