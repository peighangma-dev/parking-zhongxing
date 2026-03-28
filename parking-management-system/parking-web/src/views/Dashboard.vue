<template>
  <div class="dashboard">
    <div class="dashboard-header">
      <h2 class="cyber-title">SYSTEM OVERVIEW</h2>
      <div class="header-line"></div>
    </div>

    <el-row :gutter="20" class="stats-row">
      <el-col :span="6">
        <div class="stat-card stat-cyan">
          <div class="stat-glow"></div>
          <div class="stat-icon">
            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <path d="M9 17V7m0 10a2 2 0 01-2 2H5a2 2 0 01-2-2V7a2 2 0 012-2h2a2 2 0 012 2m0 10a2 2 0 002 2h2a2 2 0 002-2M9 7a2 2 0 012-2h2a2 2 0 012 2m0 10V7m0 10a2 2 0 002 2h2a2 2 0 002-2V7a2 2 0 00-2-2h-2a2 2 0 00-2 2"/>
            </svg>
          </div>
          <div class="stat-content">
            <div class="stat-label">今日入场</div>
            <div class="stat-value">1,234</div>
            <div class="stat-trend trend-up">
              <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <path d="M5 15l7-7 7 7"/>
              </svg>
              +12.5%
            </div>
          </div>
        </div>
      </el-col>
      
      <el-col :span="6">
        <div class="stat-card stat-magenta">
          <div class="stat-glow"></div>
          <div class="stat-icon">
            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <path d="M17 7l-8 8-4-4"/>
            </svg>
          </div>
          <div class="stat-content">
            <div class="stat-label">今日出场</div>
            <div class="stat-value">1,156</div>
            <div class="stat-trend trend-up">
              <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <path d="M5 15l7-7 7 7"/>
              </svg>
              +8.3%
            </div>
          </div>
        </div>
      </el-col>
      
      <el-col :span="6">
        <div class="stat-card stat-green">
          <div class="stat-glow"></div>
          <div class="stat-icon">
            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <rect x="3" y="3" width="18" height="18" rx="2"/>
              <path d="M3 9h18M9 21V9"/>
            </svg>
          </div>
          <div class="stat-content">
            <div class="stat-label">当前在场</div>
            <div class="stat-value">78</div>
            <div class="stat-trend trend-down">
              <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <path d="M19 9l-7 7-7-7"/>
              </svg>
              -3.2%
            </div>
          </div>
        </div>
      </el-col>
      
      <el-col :span="6">
        <div class="stat-card stat-yellow">
          <div class="stat-glow"></div>
          <div class="stat-icon">
            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <path d="M12 1v22M17 5H9.5a3.5 3.5 0 000 7h5a3.5 3.5 0 010 7H6"/>
            </svg>
          </div>
          <div class="stat-content">
            <div class="stat-label">今日收入</div>
            <div class="stat-value">¥15,680</div>
            <div class="stat-trend trend-up">
              <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <path d="M5 15l7-7 7 7"/>
              </svg>
              +23.1%
            </div>
          </div>
        </div>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="charts-row">
      <el-col :span="12">
        <el-card class="data-card">
          <template #header>
            <div class="card-header-wrapper">
              <span class="card-title">道闸设备状态</span>
              <div class="status-badge online">
                <span class="status-dot"></span>
                4 在线
              </div>
            </div>
          </template>
          <div class="device-list">
            <div v-for="device in barrierStatus" :key="device.name" class="device-item">
              <div class="device-info">
                <div class="device-name">{{ device.name }}</div>
                <div class="device-location">{{ device.location }}</div>
              </div>
              <div :class="['device-status', device.status]">
                <span class="status-indicator"></span>
                {{ device.status === 'online' ? '在线' : '离线' }}
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
      
      <el-col :span="12">
        <el-card class="data-card">
          <template #header>
            <div class="card-header-wrapper">
              <span class="card-title">实时交易</span>
              <div class="live-indicator">
                <span class="live-dot"></span>
                LIVE
              </div>
            </div>
          </template>
          <div class="transaction-list">
            <div v-for="tx in recentPayments" :key="tx.orderNo" class="transaction-item">
              <div class="tx-plate">{{ tx.plateNumber }}</div>
              <div class="tx-channel">{{ tx.channel }}</div>
              <div class="tx-amount">¥{{ tx.amount }}</div>
              <div class="tx-time">{{ tx.time }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="charts-row">
      <el-col :span="24">
        <el-card class="data-card full-width">
          <template #header>
            <div class="card-header-wrapper">
              <span class="card-title">24小时通行趋势</span>
            </div>
          </template>
          <div class="chart-placeholder">
            <div class="chart-grid">
              <div v-for="i in 24" :key="i" class="chart-bar-wrapper">
                <div class="chart-bar" :style="{ height: Math.random() * 60 + 20 + '%' }"></div>
                <div class="chart-label">{{ i - 1 }}:00</div>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'

const barrierStatus = ref([
  { name: '入口道闸 A-01', location: '东门入口', status: 'online' },
  { name: '入口道闸 A-02', location: '东门入口', status: 'online' },
  { name: '出口道闸 B-01', location: '西门出口', status: 'online' },
  { name: '出口道闸 B-02', location: '西门出口', status: 'offline' }
])

const recentPayments = ref([
  { orderNo: 'TXN001', plateNumber: '京A·12345', amount: '10.00', channel: '微信支付', time: '10:30:25' },
  { orderNo: 'TXN002', plateNumber: '京B·67890', amount: '5.00', channel: '支付宝', time: '10:28:12' },
  { orderNo: 'TXN003', plateNumber: '沪C·11111', amount: '15.00', channel: '现金', time: '10:25:08' },
  { orderNo: 'TXN004', plateNumber: '粤D·22222', amount: '20.00', channel: '微信支付', time: '10:22:45' },
  { orderNo: 'TXN005', plateNumber: '苏E·33333', amount: '8.00', channel: '支付宝', time: '10:20:33' }
])
</script>

<style scoped>
.dashboard {
  padding: 0;
}

.dashboard-header {
  margin-bottom: 30px;
}

.cyber-title {
  font-family: 'Orbitron', sans-serif;
  font-size: 14px;
  font-weight: 600;
  color: var(--cyber-cyan);
  letter-spacing: 4px;
  margin-bottom: 10px;
}

.header-line {
  width: 100%;
  height: 1px;
  background: linear-gradient(90deg, var(--cyber-cyan), var(--cyber-magenta), transparent);
}

.stats-row {
  margin-bottom: 20px;
}

.stat-card {
  background: var(--cyber-glass);
  border: 1px solid var(--cyber-border);
  border-radius: 16px;
  padding: 20px;
  position: relative;
  overflow: hidden;
  transition: all 0.3s ease;
}

.stat-card:hover {
  transform: translateY(-5px);
  border-color: var(--cyber-cyan);
}

.stat-card:hover .stat-glow {
  opacity: 1;
}

.stat-glow {
  position: absolute;
  top: -50%;
  left: -50%;
  width: 200%;
  height: 200%;
  border-radius: 50%;
  opacity: 0.3;
  transition: opacity 0.3s ease;
  pointer-events: none;
}

.stat-cyan .stat-glow {
  background: radial-gradient(circle, var(--cyber-cyan) 0%, transparent 70%);
}

.stat-magenta .stat-glow {
  background: radial-gradient(circle, var(--cyber-magenta) 0%, transparent 70%);
}

.stat-green .stat-glow {
  background: radial-gradient(circle, var(--cyber-green) 0%, transparent 70%);
}

.stat-yellow .stat-glow {
  background: radial-gradient(circle, var(--cyber-yellow) 0%, transparent 70%);
}

.stat-icon {
  width: 50px;
  height: 50px;
  margin-bottom: 15px;
  opacity: 0.8;
}

.stat-cyan .stat-icon { color: var(--cyber-cyan); filter: drop-shadow(0 0 10px var(--cyber-cyan)); }
.stat-magenta .stat-icon { color: var(--cyber-magenta); filter: drop-shadow(0 0 10px var(--cyber-magenta)); }
.stat-green .stat-icon { color: var(--cyber-green); filter: drop-shadow(0 0 10px var(--cyber-green)); }
.stat-yellow .stat-icon { color: var(--cyber-yellow); filter: drop-shadow(0 0 10px var(--cyber-yellow)); }

.stat-icon svg {
  width: 100%;
  height: 100%;
}

.stat-label {
  font-family: 'Rajdhani', sans-serif;
  font-size: 12px;
  color: var(--cyber-text-dim);
  letter-spacing: 2px;
  text-transform: uppercase;
  margin-bottom: 5px;
}

.stat-value {
  font-family: 'Orbitron', sans-serif;
  font-size: 28px;
  font-weight: 700;
  margin-bottom: 8px;
}

.stat-cyan .stat-value { color: var(--cyber-cyan); text-shadow: 0 0 20px var(--cyber-cyan); }
.stat-magenta .stat-value { color: var(--cyber-magenta); text-shadow: 0 0 20px var(--cyber-magenta); }
.stat-green .stat-value { color: var(--cyber-green); text-shadow: 0 0 20px var(--cyber-green); }
.stat-yellow .stat-value { color: var(--cyber-yellow); text-shadow: 0 0 20px var(--cyber-yellow); }

.stat-trend {
  display: flex;
  align-items: center;
  gap: 5px;
  font-family: 'Rajdhani', sans-serif;
  font-size: 12px;
  font-weight: 600;
}

.stat-trend svg {
  width: 14px;
  height: 14px;
}

.trend-up { color: var(--cyber-green); }
.trend-down { color: var(--cyber-red); }

.charts-row {
  margin-bottom: 20px;
}

.data-card {
  height: 100%;
}

.data-card.full-width {
  width: 100%;
}

.card-header-wrapper {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.card-title {
  font-family: 'Orbitron', sans-serif;
  font-size: 12px;
  letter-spacing: 2px;
}

.status-badge {
  display: flex;
  align-items: center;
  gap: 6px;
  font-family: 'Rajdhani', sans-serif;
  font-size: 12px;
  font-weight: 600;
  padding: 4px 12px;
  border-radius: 20px;
  background: rgba(0, 255, 136, 0.1);
  border: 1px solid rgba(0, 255, 136, 0.3);
  color: var(--cyber-green);
}

.status-dot {
  width: 6px;
  height: 6px;
  background: var(--cyber-green);
  border-radius: 50%;
  box-shadow: 0 0 10px var(--cyber-green);
  animation: cyberPulse 1.5s ease-in-out infinite;
}

.live-indicator {
  display: flex;
  align-items: center;
  gap: 6px;
  font-family: 'Orbitron', sans-serif;
  font-size: 10px;
  color: var(--cyber-red);
  letter-spacing: 2px;
}

.live-dot {
  width: 8px;
  height: 8px;
  background: var(--cyber-red);
  border-radius: 50%;
  box-shadow: 0 0 10px var(--cyber-red);
  animation: cyberPulse 0.8s ease-in-out infinite;
}

.device-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.device-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 15px;
  background: rgba(0, 255, 255, 0.03);
  border: 1px solid var(--cyber-border);
  border-radius: 8px;
  transition: all 0.3s ease;
}

.device-item:hover {
  background: rgba(0, 255, 255, 0.08);
  border-color: var(--cyber-border-glow);
}

.device-name {
  font-family: 'Rajdhani', sans-serif;
  font-size: 14px;
  font-weight: 600;
  color: var(--cyber-text);
  margin-bottom: 4px;
}

.device-location {
  font-family: 'Rajdhani', sans-serif;
  font-size: 12px;
  color: var(--cyber-text-dim);
}

.device-status {
  display: flex;
  align-items: center;
  gap: 6px;
  font-family: 'Rajdhani', sans-serif;
  font-size: 12px;
  font-weight: 600;
  padding: 4px 12px;
  border-radius: 20px;
}

.device-status.online {
  background: rgba(0, 255, 136, 0.1);
  border: 1px solid rgba(0, 255, 136, 0.3);
  color: var(--cyber-green);
}

.device-status.offline {
  background: rgba(255, 51, 102, 0.1);
  border: 1px solid rgba(255, 51, 102, 0.3);
  color: var(--cyber-red);
}

.status-indicator {
  width: 6px;
  height: 6px;
  border-radius: 50%;
}

.online .status-indicator {
  background: var(--cyber-green);
  box-shadow: 0 0 10px var(--cyber-green);
}

.offline .status-indicator {
  background: var(--cyber-red);
  box-shadow: 0 0 10px var(--cyber-red);
}

.transaction-list {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.transaction-item {
  display: grid;
  grid-template-columns: 2fr 1.5fr 1fr 1fr;
  align-items: center;
  padding: 12px 15px;
  background: rgba(0, 255, 255, 0.03);
  border: 1px solid var(--cyber-border);
  border-radius: 8px;
  transition: all 0.3s ease;
}

.transaction-item:hover {
  background: rgba(0, 255, 255, 0.08);
  border-color: var(--cyber-border-glow);
}

.tx-plate {
  font-family: 'Orbitron', sans-serif;
  font-size: 13px;
  font-weight: 600;
  color: var(--cyber-cyan);
}

.tx-channel {
  font-family: 'Rajdhani', sans-serif;
  font-size: 12px;
  color: var(--cyber-text-dim);
}

.tx-amount {
  font-family: 'Orbitron', sans-serif;
  font-size: 14px;
  font-weight: 700;
  color: var(--cyber-green);
  text-align: right;
}

.tx-time {
  font-family: 'Rajdhani', sans-serif;
  font-size: 12px;
  color: var(--cyber-text-dim);
  text-align: right;
}

.chart-placeholder {
  padding: 20px 0;
}

.chart-grid {
  display: flex;
  justify-content: space-between;
  align-items: flex-end;
  height: 150px;
  padding: 0 10px;
  border-bottom: 1px solid var(--cyber-border);
}

.chart-bar-wrapper {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8px;
  flex: 1;
}

.chart-bar {
  width: 80%;
  background: linear-gradient(180deg, var(--cyber-cyan), var(--cyber-blue));
  border-radius: 4px 4px 0 0;
  box-shadow: 0 0 10px var(--cyber-cyan);
  transition: all 0.3s ease;
}

.chart-bar:hover {
  background: linear-gradient(180deg, var(--cyber-cyan), var(--cyber-magenta));
  box-shadow: 0 0 20px var(--cyber-magenta);
}

.chart-label {
  font-family: 'Rajdhani', sans-serif;
  font-size: 10px;
  color: var(--cyber-text-dim);
}
</style>
