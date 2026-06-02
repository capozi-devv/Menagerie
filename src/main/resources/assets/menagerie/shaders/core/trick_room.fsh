#version 150

uniform vec4 ColorModulator;
uniform vec3 iResolution;
uniform float iTime;
uniform float BorderOnly;

in vec2 texCoord0;

out vec4 fragColor;

const float ANIMATION_SPEED = 0.25;
const float WALL_ALPHA = 0.95;
const vec3 DARK_COLOR = vec3(0.011764705882352941, 0.1411764705882353, 0.1411764705882353);
const vec3 BRIGHT_COLOR = vec3(0.011764705882352941, 0.9882352941176471, 0.9882352941176471);
const vec3 BORDER_COLOR = vec3(0.011764705882352941, 0.1411764705882353, 0.1411764705882353);
const float BORDER_PIXELS = 6.0;
const float BORDER_ALPHA = 0.8;
const vec2 ATLAS_SIZE = vec2(4.0, 3.0);
const float ATLAS_Y_OFFSET = 1.0;

vec3 primaryLayer(vec2 fragCoord, float time) {
    vec2 uv = (2.0 * fragCoord - iResolution.xy) / min(iResolution.x, iResolution.y);

    for (int i = 1; i < 10; i++) {
        float fi = float(i);
        uv.x += 0.6 / fi * cos(fi * 2.5 * uv.y + time);
        uv.y += 0.6 / fi * cos(fi * 1.5 * uv.x + time);
    }

    float wave = max(abs(sin(time - uv.y - uv.x)), 0.035);
    return vec3(0.1) / wave;
}

vec3 tx2D(vec2 uv, float time) {
    return primaryLayer(uv * iResolution.xy, time);
}

float getHeight(vec2 uv, float time) {
    return dot(tx2D(uv, time), vec3(0.299, 0.587, 0.114));
}

vec3 curlLightingLayer(vec2 fragCoord, float time) {
    vec2 uv = fragCoord / iResolution.xy;
    vec2 aspect = vec2(iResolution.x / iResolution.y, 1.0);
    uv += vec2(sin((uv.y + time * 0.08) * 6.28318), cos((uv.x - time * 0.06) * 6.28318)) * 0.018;

    vec3 col = tx2D(uv, time);
    float height = dot(col, vec3(0.299, 0.587, 0.114));
    float height2 = getHeight(uv - normalize(vec2(1.0, 2.0)) * 0.001 * aspect, time);

    vec2 e = vec2(0.0045, 0.0);
    vec4 t4 = vec4(
        getHeight(uv - e.xy, time),
        getHeight(uv + e.xy, time),
        getHeight(uv - e.yx, time),
        getHeight(uv + e.yx, time)
    );

    vec3 vx = vec3(e.x * 2.0, 0.0, t4.x - t4.y);
    vec3 vy = vec3(0.0, -e.x * 2.0, t4.w - t4.z);
    vec3 sn = normalize(cross(vx, vy));

    float curv = clamp((height * 4.0 - dot(t4, vec4(1.0))) / e.x * 0.35 + 0.5, 0.0, 1.0);
    vec3 ld = normalize(vec3(-0.5, 1.0, -1.0));

    float b = max(height2 - height, 0.0) / 0.001;
    float b2 = max(height - height2, 0.0) / 0.001;
    vec3 hiCol = vec3(b * 0.035 + b2 * 0.02);

    col *= max(dot(sn, ld), 0.0) + hiCol + 0.4;
    col *= curv + 0.2;
    return sqrt(max(col, 0.0));
}

vec3 colorize(vec3 monochrome) {
    float intensity = dot(monochrome, vec3(0.299, 0.587, 0.114));
    intensity = 1.0 - exp(-max(intensity, 0.0) * 0.85);
    return mix(DARK_COLOR, BRIGHT_COLOR, clamp(intensity, 0.0, 1.0));
}

void mainImage(out vec4 fragColorOut, in vec2 fragCoord) {
    float time = iTime * ANIMATION_SPEED;
    vec3 base = primaryLayer(fragCoord, time);
    vec3 curl = curlLightingLayer(fragCoord, time);
    vec3 color = colorize(base * 0.72 + curl * 0.68);
    fragColorOut = vec4(color, 1.0);
}

void main() {
    vec2 faceUv = fract(texCoord0);
    vec2 borderSize = BORDER_PIXELS / iResolution.xy;
    bool border = faceUv.x <= borderSize.x
            || faceUv.x >= 1.0 - borderSize.x
            || faceUv.y <= borderSize.y
            || faceUv.y >= 1.0 - borderSize.y;

    if (BorderOnly > 0.5) {
        if (!border) {
            discard;
        }
        fragColor = vec4(BORDER_COLOR * ColorModulator.rgb, BORDER_ALPHA * ColorModulator.a);
        return;
    }

    vec4 shaderColor;
    vec2 atlasUv = vec2(texCoord0.x, texCoord0.y + ATLAS_Y_OFFSET) / ATLAS_SIZE;
    mainImage(shaderColor, atlasUv * iResolution.xy);
    fragColor = vec4(shaderColor.rgb * ColorModulator.rgb, WALL_ALPHA * shaderColor.a * ColorModulator.a);
}
