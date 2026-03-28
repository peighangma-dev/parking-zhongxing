<template>
  <router-view />
</template>

<script setup lang="ts">
</script>

<style>
@import url('https://fonts.googleapis.com/css2?family=Orbitron:wght@400;500;600;700&family=Rajdhani:wght@300;400;500;600;700&display=swap');

* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

html, body, #app {
  width: 100%;
  height: 100%;
  font-family: 'Rajdhani', 'Segoe UI', sans-serif;
}

/* Cyberpunk Theme Variables */
:root {
  --cyber-bg-primary: #0a0a0f;
  --cyber-bg-secondary: #12121a;
  --cyber-bg-card: rgba(18, 18, 26, 0.8);
  --cyber-border: rgba(0, 255, 255, 0.15);
  --cyber-border-glow: rgba(0, 255, 255, 0.4);
  --cyber-cyan: #00ffff;
  --cyber-magenta: #ff00ff;
  --cyber-blue: #0080ff;
  --cyber-green: #00ff88;
  --cyber-yellow: #ffcc00;
  --cyber-red: #ff3366;
  --cyber-purple: #9933ff;
  --cyber-text: #e0e0e0;
  --cyber-text-dim: #7a7a8a;
  --cyber-glass: rgba(18, 18, 26, 0.7);
  --cyber-glow-cyan: 0 0 20px rgba(0, 255, 255, 0.3);
  --cyber-glow-magenta: 0 0 20px rgba(255, 0, 255, 0.3);
}

/* Global Styles */
body {
  background: var(--cyber-bg-primary);
  color: var(--cyber-text);
  overflow-x: hidden;
}

/* Scrollbar */
::-webkit-scrollbar {
  width: 6px;
  height: 6px;
}
::-webkit-scrollbar-track {
  background: var(--cyber-bg-secondary);
}
::-webkit-scrollbar-thumb {
  background: var(--cyber-cyan);
  border-radius: 3px;
  box-shadow: var(--cyber-glow-cyan);
}

/* Element Plus Dark Overrides */
.el-card {
  --el-card-bg-color: var(--cyber-glass);
  --el-card-border-color: var(--cyber-border);
  background: var(--cyber-glass) !important;
  border: 1px solid var(--cyber-border) !important;
  backdrop-filter: blur(20px);
  border-radius: 12px !important;
  box-shadow: 0 4px 30px rgba(0, 0, 0, 0.5), inset 0 0 1px rgba(0, 255, 255, 0.1);
}

.el-card:hover {
  border-color: var(--cyber-border-glow) !important;
  box-shadow: 0 4px 30px rgba(0, 0, 0, 0.5), var(--cyber-glow-cyan), inset 0 0 1px rgba(0, 255, 255, 0.2);
}

.el-card__header {
  border-bottom: 1px solid var(--cyber-border);
  font-family: 'Orbitron', sans-serif;
  font-weight: 600;
  letter-spacing: 2px;
  text-transform: uppercase;
  color: var(--cyber-cyan);
  text-shadow: var(--cyber-glow-cyan);
}

.el-button {
  --el-button-bg-color: transparent;
  --el-button-border-color: var(--cyber-border);
  --el-button-text-color: var(--cyber-text);
  font-family: 'Rajdhani', sans-serif;
  font-weight: 600;
  letter-spacing: 1px;
  border-radius: 6px;
  transition: all 0.3s ease;
}

.el-button--primary {
  --el-button-bg-color: linear-gradient(135deg, var(--cyber-cyan), var(--cyber-blue));
  --el-button-border-color: var(--cyber-cyan);
  --el-button-text-color: #000;
  background: linear-gradient(135deg, var(--cyber-cyan), var(--cyber-blue)) !important;
  border: 1px solid var(--cyber-cyan) !important;
  box-shadow: var(--cyber-glow-cyan);
}

.el-button--primary:hover {
  background: linear-gradient(135deg, var(--cyber-cyan), var(--cyber-magenta)) !important;
  border-color: var(--cyber-magenta) !important;
  box-shadow: var(--cyber-glow-magenta), 0 0 30px rgba(255, 0, 255, 0.4);
  transform: translateY(-2px);
}

.el-button--success {
  background: linear-gradient(135deg, var(--cyber-green), #00cc66) !important;
  border-color: var(--cyber-green) !important;
  box-shadow: 0 0 15px rgba(0, 255, 136, 0.3);
}

.el-button--warning {
  background: linear-gradient(135deg, var(--cyber-yellow), #ff9900) !important;
  border-color: var(--cyber-yellow) !important;
  box-shadow: 0 0 15px rgba(255, 204, 0, 0.3);
}

.el-button--danger {
  background: linear-gradient(135deg, var(--cyber-red), #cc0033) !important;
  border-color: var(--cyber-red) !important;
  box-shadow: 0 0 15px rgba(255, 51, 102, 0.3);
}

.el-input__wrapper {
  background: var(--cyber-bg-secondary) !important;
  border: 1px solid var(--cyber-border);
  box-shadow: inset 0 2px 10px rgba(0, 0, 0, 0.5);
  border-radius: 6px;
}

.el-input__wrapper:hover {
  border-color: var(--cyber-cyan);
  box-shadow: inset 0 2px 10px rgba(0, 0, 0, 0.5), var(--cyber-glow-cyan);
}

.el-input__wrapper.is-focus {
  border-color: var(--cyber-cyan) !important;
  box-shadow: inset 0 2px 10px rgba(0, 0, 0, 0.5), var(--cyber-glow-cyan) !important;
}

.el-input__inner {
  color: var(--cyber-text) !important;
  font-family: 'Rajdhani', sans-serif;
}

.el-input__inner::placeholder {
  color: var(--cyber-text-dim) !important;
}

.el-select__wrapper {
  background: var(--cyber-bg-secondary) !important;
  border: 1px solid var(--cyber-border);
  box-shadow: inset 0 2px 10px rgba(0, 0, 0, 0.5);
}

.el-select__wrapper:hover {
  border-color: var(--cyber-cyan);
}

.el-select__wrapper.is-focused {
  border-color: var(--cyber-cyan) !important;
  box-shadow: var(--cyber-glow-cyan) !important;
}

.el-table {
  --el-table-bg-color: transparent;
  --el-table-tr-bg-color: transparent;
  --el-table-header-bg-color: rgba(0, 255, 255, 0.05);
  --el-table-row-hover-bg-color: rgba(0, 255, 255, 0.08);
  --el-table-border-color: var(--cyber-border);
  --el-table-text-color: var(--cyber-text);
  --el-table-header-text-color: var(--cyber-cyan);
  font-family: 'Rajdhani', sans-serif;
  font-size: 14px;
}

.el-table th.el-table__cell {
  background: rgba(0, 255, 255, 0.05) !important;
  font-family: 'Orbitron', sans-serif;
  font-weight: 600;
  letter-spacing: 1px;
  text-transform: uppercase;
  border-bottom: 1px solid var(--cyber-border) !important;
}

.el-table td.el-table__cell {
  border-bottom: 1px solid rgba(0, 255, 255, 0.08) !important;
}

.el-table__row:hover > td {
  background: rgba(0, 255, 255, 0.08) !important;
}

.el-pagination {
  --el-pagination-bg-color: transparent;
  --el-pagination-text-color: var(--cyber-text-dim);
  --el-pagination-button-disabled-bg-color: transparent;
  --el-pagination-hover-color: var(--cyber-cyan);
}

.el-pagination.is-background .el-pager li {
  background: transparent;
  border: 1px solid var(--cyber-border);
  border-radius: 4px;
  margin: 0 3px;
}

.el-pagination.is-background .el-pager li:not(.is-disabled).is-active {
  background: linear-gradient(135deg, var(--cyber-cyan), var(--cyber-blue)) !important;
  border-color: var(--cyber-cyan) !important;
  color: #000 !important;
  font-weight: 700;
  box-shadow: var(--cyber-glow-cyan);
}

.el-tag {
  --el-tag-bg-color: rgba(0, 255, 255, 0.1);
  --el-tag-border-color: var(--cyber-border);
  --el-tag-text-color: var(--cyber-cyan);
  border-radius: 4px;
  font-family: 'Rajdhani', sans-serif;
  font-weight: 600;
}

.el-tag--success {
  --el-tag-bg-color: rgba(0, 255, 136, 0.1);
  --el-tag-border-color: rgba(0, 255, 136, 0.3);
  --el-tag-text-color: var(--cyber-green);
}

.el-tag--warning {
  --el-tag-bg-color: rgba(255, 204, 0, 0.1);
  --el-tag-border-color: rgba(255, 204, 0, 0.3);
  --el-tag-text-color: var(--cyber-yellow);
}

.el-tag--danger {
  --el-tag-bg-color: rgba(255, 51, 102, 0.1);
  --el-tag-border-color: rgba(255, 51, 102, 0.3);
  --el-tag-text-color: var(--cyber-red);
}

.el-dialog {
  --el-dialog-bg-color: var(--cyber-bg-card);
  --el-dialog-border-radius: 16px;
  background: var(--cyber-glass) !important;
  border: 1px solid var(--cyber-border);
  backdrop-filter: blur(30px);
  box-shadow: 0 0 50px rgba(0, 0, 0, 0.8), var(--cyber-glow-cyan);
}

.el-dialog__header {
  border-bottom: 1px solid var(--cyber-border);
  font-family: 'Orbitron', sans-serif;
  font-weight: 600;
  letter-spacing: 2px;
}

.el-dialog__title {
  color: var(--cyber-cyan) !important;
  text-shadow: var(--cyber-glow-cyan);
}

.el-form-item__label {
  color: var(--cyber-text) !important;
  font-family: 'Rajdhani', sans-serif;
  font-weight: 600;
  letter-spacing: 1px;
}

.el-checkbox__input.is-checked .el-checkbox__inner {
  background: linear-gradient(135deg, var(--cyber-cyan), var(--cyber-blue)) !important;
  border-color: var(--cyber-cyan) !important;
  box-shadow: var(--cyber-glow-cyan);
}

.el-checkbox__label {
  color: var(--cyber-text) !important;
}

.el-tree {
  --el-tree-node-hover-bg-color: rgba(0, 255, 255, 0.1);
  --el-tree-text-color: var(--cyber-text);
  --el-tree-expand-icon-color: var(--cyber-text-dim);
  background: transparent;
  font-family: 'Rajdhani', sans-serif;
}

.el-tree-node__content:hover {
  background: rgba(0, 255, 255, 0.1) !important;
}

.el-menu {
  --el-menu-bg-color: transparent;
  --el-menu-text-color: var(--cyber-text-dim);
  --el-menu-active-color: var(--cyber-cyan);
  --el-menu-hover-bg-color: rgba(0, 255, 255, 0.1);
  --el-menu-hover-text-color: var(--cyber-cyan);
  border: none;
  background: transparent !important;
}

.el-sub-menu__title {
  color: var(--cyber-text-dim) !important;
  transition: all 0.3s ease;
}

.el-sub-menu__title:hover {
  background: rgba(0, 255, 255, 0.1) !important;
  color: var(--cyber-cyan) !important;
}

.el-menu-item {
  color: var(--cyber-text-dim) !important;
  transition: all 0.3s ease;
  position: relative;
}

.el-menu-item::before {
  content: '';
  position: absolute;
  left: 0;
  top: 50%;
  transform: translateY(-50%);
  width: 0;
  height: 0;
  border-left: 2px solid var(--cyber-cyan);
  opacity: 0;
  transition: all 0.3s ease;
}

.el-menu-item:hover {
  background: rgba(0, 255, 255, 0.1) !important;
  color: var(--cyber-cyan) !important;
  text-shadow: var(--cyber-glow-cyan);
}

.el-menu-item:hover::before {
  width: 100%;
  opacity: 1;
}

.el-menu-item.is-active {
  color: var(--cyber-cyan) !important;
  background: rgba(0, 255, 255, 0.15) !important;
  text-shadow: var(--cyber-glow-cyan);
  border-left: 2px solid var(--cyber-cyan);
}

.el-switch {
  --el-switch-off-color: var(--cyber-bg-secondary);
  --el-switch-on-color: var(--cyber-cyan);
  --el-switch-border-color: var(--cyber-border);
}

.el-switch.is-checked .el-switch__core {
  background: var(--cyber-cyan) !important;
  border-color: var(--cyber-cyan) !important;
  box-shadow: var(--cyber-glow-cyan);
}

.el-date-picker {
  background: var(--cyber-bg-card) !important;
  border: 1px solid var(--cyber-border) !important;
}

.el-date-editor .el-input__wrapper {
  background: var(--cyber-bg-secondary) !important;
}

.el-picker-panel {
  background: var(--cyber-bg-card) !important;
  border: 1px solid var(--cyber-border) !important;
  color: var(--cyber-text);
}

.el-message-box {
  --el-messagebox-title-color: var(--cyber-cyan);
  background: var(--cyber-bg-card) !important;
  border: 1px solid var(--cyber-border) !important;
  backdrop-filter: blur(30px);
}

.el-message-box__title {
  color: var(--cyber-cyan) !important;
}

/* Animations */
@keyframes cyberPulse {
  0%, 100% { opacity: 1; }
  50% { opacity: 0.5; }
}

@keyframes neonFlicker {
  0%, 100% { opacity: 1; }
  92% { opacity: 1; }
  93% { opacity: 0.8; }
  94% { opacity: 1; }
  96% { opacity: 0.9; }
  97% { opacity: 1; }
}

@keyframes scanline {
  0% { transform: translateY(-100%); }
  100% { transform: translateY(100vh); }
}

@keyframes gradientShift {
  0% { background-position: 0% 50%; }
  50% { background-position: 100% 50%; }
  100% { background-position: 0% 50%; }
}

/* Utility Classes */
.cyber-glow {
  box-shadow: var(--cyber-glow-cyan);
}

.cyber-text {
  color: var(--cyber-cyan);
  text-shadow: var(--cyber-glow-cyan);
}

.cyber-border {
  border: 1px solid var(--cyber-border);
}

.cyber-gradient-text {
  background: linear-gradient(135deg, var(--cyber-cyan), var(--cyber-magenta));
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}
</style>
