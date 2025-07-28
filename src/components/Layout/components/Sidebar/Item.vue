<template>
  <div>
    <i v-if="icon" :class="icon"></i>
    <span slot="title">{{ title }}</span>
  </div>
</template>

<script>
export default {
  name: 'MenuItem',
  props: {
    icon: {
      type: String,
      default: ''
    },
    title: {
      type: String,
      default: ''
    }
  }
}
</script>
