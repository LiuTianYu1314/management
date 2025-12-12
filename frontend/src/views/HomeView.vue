<template>
  <div class="home-view">
    <div ref="container" class="three-container"></div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue';
import * as THREE from 'three';
// 导入 OrbitControls
import { OrbitControls } from 'three/examples/jsm/controls/OrbitControls.js';

const container = ref(null);
let scene, camera, renderer, clock;
let particlesMesh;
let animationFrameId;
let controls;

// --- 配置参数 ---
const MAP_JSON_URL = 'https://cdn.jsdelivr.net/npm/echarts/map/json/world.json';
const PARTICLE_COUNT = 50000; // 粒子数量
const PARTICLE_SIZE = 0.5;
const MAP_SCALE = 0.5; // 控制地图在 Three.js 场景中的缩放比例
const SAMPLE_DENSITY = 0.5; // 用于在 GeoJSON 顶点之间插值采样的密度

// --- 动画计时变量 ---
const ANIMATION_DURATION = 4.0; // 动画持续时间（秒）
let animationStartTime = null;

// 粒子数据结构
const particleData = [];

// --- 初始化 Three.js 场景 ---
function initThree() {
  scene = new THREE.Scene();
  scene.background = new THREE.Color(0x0a1024); // 深邃的科技蓝色背景

  if (!container.value) return;

  // --- 1. 屏幕自适应计算 ---
  const width = container.value.clientWidth;
  const height = container.value.clientHeight;
  const aspectRatio = width / height;

  camera = new THREE.PerspectiveCamera(60, aspectRatio, 1, 1000);

  // 计算所需的相机 Z 轴位置，以使地图完全可见并占据大部分视野
  const MAP_SPAN = 360 * MAP_SCALE;
  const cameraZ = (MAP_SPAN / aspectRatio) / (2 * Math.tan(THREE.MathUtils.degToRad(camera.fov / 2))) * 1.2;
  camera.position.z = Math.max(100, cameraZ);

  renderer = new THREE.WebGLRenderer({antialias: true});
  renderer.setSize(width, height);
  container.value.appendChild(renderer.domElement);

  clock = new THREE.Clock();

  // 💥 2. 初始化 OrbitControls，实现拖动平移
  controls = new OrbitControls(camera, renderer.domElement);
  controls.enableDamping = true;
  controls.dampingFactor = 0.05;

  // 核心修改：禁用旋转，启用平移
  controls.enableRotate = false; // 禁用鼠标左键/触摸旋转
  controls.enablePan = true;     // 启用平移 (上下左右拖动)
  controls.screenSpacePanning = true; // 确保平移在屏幕空间进行

  controls.minDistance = 50;
  controls.maxDistance = 300;
  // controls.maxPolarAngle = Math.PI * 0.9; // 旋转已禁用，此项不再影响

  loadMapAndCreateParticles();

  window.addEventListener('resize', onWindowResize);

  animate();
}

// --- 响应式调整 ---
function onWindowResize() {
  if (!container.value) return;
  const width = container.value.clientWidth;
  const height = container.value.clientHeight;

  camera.aspect = width / height;
  camera.updateProjectionMatrix();
  renderer.setSize(width, height);
}

// --- JSON 加载和粒子生成 ---
async function loadMapAndCreateParticles() {
  try {
    const response = await fetch(MAP_JSON_URL);
    if (!response.ok) {
      throw new Error('Failed to load GeoJSON data');
    }
    const geoJson = await response.json();

    const targetPoints = [];

    function processCoordinates(coords) {
      if (Array.isArray(coords[0]) && coords[0].length >= 2 && typeof coords[0][0] === 'number') {
        for (let i = 0; i < coords.length; i++) {
          const lon = coords[i][0];
          const lat = coords[i][1];

          const x = lon * MAP_SCALE;
          const y = lat * MAP_SCALE;

          if (i > 0) {
            const prevLon = coords[i - 1][0];
            const prevLat = coords[i - 1][1];

            const prevX = prevLon * MAP_SCALE;
            const prevY = prevLat * MAP_SCALE;

            const distance = Math.sqrt(Math.pow(x - prevX, 2) + Math.pow(y - prevY, 2));
            const steps = Math.ceil(distance / SAMPLE_DENSITY);

            for (let step = 0; step <= steps; step++) {
              const t = step / steps;
              const interpX = prevX + (x - prevX) * t;
              const interpY = prevY + (y - prevY) * t;
              targetPoints.push(new THREE.Vector3(interpX, interpY, 0));
            }
          } else {
            targetPoints.push(new THREE.Vector3(x, y, 0));
          }
        }
      } else {
        coords.forEach(processCoordinates);
      }
    }

    geoJson.features.forEach(feature => {
      const geometry = feature.geometry;
      if (geometry && geometry.coordinates) {
        if (geometry.type === 'Polygon' || geometry.type === 'MultiLineString') {
          geometry.coordinates.forEach(processCoordinates);
        } else if (geometry.type === 'MultiPolygon') {
          geometry.coordinates.forEach(poly => poly.forEach(processCoordinates));
        }
      }
    });

    // --- 2. 粒子数据初始化 ---
    const numParticles = Math.min(PARTICLE_COUNT, targetPoints.length);
    const positions = new Float32Array(numParticles * 3);
    const geometry = new THREE.BufferGeometry();

    particleData.length = 0;
    const startPositions = new Float32Array(numParticles * 3);

    for (let i = 0; i < numParticles; i++) {
      const targetIndex = Math.floor(Math.random() * targetPoints.length);
      const targetPos = targetPoints[targetIndex];

      const startPos = new THREE.Vector3(
          (Math.random() - 0.5) * 300,
          (Math.random() - 0.5) * 300,
          (Math.random() - 0.5) * 300
      );

      positions[i * 3 + 0] = targetPos.x;
      positions[i * 3 + 1] = targetPos.y;
      positions[i * 3 + 2] = targetPos.z;

      startPositions[i * 3 + 0] = startPos.x;
      startPositions[i * 3 + 1] = startPos.y;
      startPositions[i * 3 + 2] = startPos.z;

      particleData.push({
        currentPos: startPos.clone(),
        targetPos: targetPos,
        speed: Math.random() * 0.005 + 0.001,
      });
    }

    geometry.setAttribute('position', new THREE.BufferAttribute(positions, 3));
    geometry.setAttribute('startPosition', new THREE.BufferAttribute(startPositions, 3));

    // --- 3. 粒子材质和着色器 (ShaderMaterial) ---
    const material = new THREE.ShaderMaterial({
      uniforms: {
        uCurrentProgress: {value: 0.0},
        color: {value: new THREE.Color(0xffffff)},
      },
      vertexShader: `
                attribute vec3 startPosition;
                uniform float uCurrentProgress;

                // 快速开始，慢速结束的缓动函数 (EaseOutQuad)
                float easeOutQuad(float t) {
                    return t * (2.0 - t);
                }

                void main() {
                    float progress = easeOutQuad(clamp(uCurrentProgress, 0.0, 1.0));
                    vec3 finalPosition = mix(startPosition, position, progress);

                    vec4 mvPosition = modelViewMatrix * vec4(finalPosition, 1.0);

                    float size = ${PARTICLE_SIZE} * (1.0 + 5.0 * (1.0 - progress));
                    gl_PointSize = size * (200.0 / -mvPosition.z);

                    gl_Position = projectionMatrix * mvPosition;
                }
            `,
      fragmentShader: `
                uniform vec3 color;
                uniform float uCurrentProgress;

                void main() {
                    float strength = 1.0 - length(gl_PointCoord - 0.5) * 2.0;
                    strength = pow(strength, 1.0);

                    vec3 finalColor = vec3(0.0, 0.7, 1.0) * color * 1.5;

                    float opacity = strength * (0.9 + 0.9 * uCurrentProgress);

                    gl_FragColor = vec4(finalColor, opacity);
                }
            `,
      transparent: true,
      blending: THREE.AdditiveBlending,
      depthTest: false,
    });

    if (particlesMesh) scene.remove(particlesMesh);

    particlesMesh = new THREE.Points(geometry, material);
    scene.add(particlesMesh);

    animationStartTime = performance.now();

  } catch (error) {
    console.error('Error loading or processing GeoJSON:', error);
  }
}


// --- 动画循环 ---
function animate() {
  animationFrameId = requestAnimationFrame(animate);

  const elapsedTime = performance.now() - animationStartTime;
  let progress = Math.min(1.0, elapsedTime / (ANIMATION_DURATION * 1000));

  if (particlesMesh) {
    // 1. 更新 Shader Uniforms
    particlesMesh.material.uniforms.uCurrentProgress.value = progress;
  }

  // 💥 必须更新 OrbitControls
  if (controls) {
    controls.update();
  }

  renderer.render(scene, camera);
}

// --- Vue 生命周期 ---
onMounted(() => {
  initThree();
});

onUnmounted(() => {
  cancelAnimationFrame(animationFrameId);
  window.removeEventListener('resize', onWindowResize);
  if (controls) {
    controls.dispose();
  }
  if (renderer) {
    renderer.dispose();
  }
});
</script>

<style scoped>
.home-view {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: 100%;
  background-color: #0a1024;
  overflow: hidden;
}

.three-container {
  width: 100%;
  height: 100vh;
}
</style>