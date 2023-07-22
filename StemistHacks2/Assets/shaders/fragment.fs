#version 330 core

in vec3 Normal;
in vec3 textureCoords;
in vec3 FragPos;
in vec3 LightPos;
out vec4 color;

uniform sampler2DArray texture;

void main() {
    vec3 offset = vec3(texture(texture, textureCoords));
    vec3 lightColor = vec3(1.0, 1.0, 1.0);

    //ambient
    float ambientStrength = 0.1;
    vec3 ambient = ambientStrength * lightColor;

    //diffuse
    vec3 norm = normalize(Normal);
    vec3 lightDir = normalize(LightPos - FragPos);
    float diff = max(dot(norm, lightDir), 0.0);
    vec3 diffuse = diff * lightColor;

    //specular
    float ss = 0.5;
    vec3 viewDir = normalize(-FragPos);
    vec3 reflectDir = reflect(-lightDir, norm);
    float spec = pow(max(dot(viewDir, reflectDir), 0.0), 32);
    vec3 specular = ss * spec * lightColor;

    vec3 result = (ambient + diffuse + specular) * offset;
    color = vec4(result, 1.0);
}