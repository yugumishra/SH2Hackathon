#version 330 core

in vec3 posi;
in vec3 normal;
in vec3 textureCoords;
out vec4 color;

uniform sampler2DArray texture;

void main() {
    color = texture(texture, textureCoords);
}