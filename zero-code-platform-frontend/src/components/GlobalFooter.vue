<template>
  <a-layout-footer class="footer">
    <div class="footer-content">
      <p>会飞的鱼工作室 by <a href="https://fishdum.github.io/" target="_blank">会飞的鱼</a></p>
    </div>
  </a-layout-footer>
</template>

<script setup lang="ts"></script>

<style scoped>
.footer {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  background-color: #f0f2f5;
  padding: 16px 24px;
  text-align: center;
  box-shadow: 0 -2px 8px rgba(0, 0, 0, 0.09);
  z-index: 100;
}

.footer-content {
  display: flex;
  justify-content: center;
  align-items: center;
}

.footer-content p {
  margin: 0;
  font-size: 14px;
  color: #666;
}

.footer-content a {
  color: #1890ff;
  text-decoration: none;
  margin-left: 4px;
}

.footer-content a:hover {
  text-decoration: underline;
}

@media (max-width: 768px) {
  .footer {
    padding: 12px 16px;
  }
  
  .footer-content p {
    font-size: 12px;
  }
}
</style>