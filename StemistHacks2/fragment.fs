#version 330 core

in vec3 posi;
out vec4 color;

void main() {
    color = vec4(posi.x + 0.17, 0.0, posi.y + 0.25, 1.0);
}