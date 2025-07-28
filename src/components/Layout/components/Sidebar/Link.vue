<template>
  <component :is="linkProps.is" v-bind="linkProps">
    <slot />
  </component>
</template>

<script>
import { isExternal } from '@/utils/validate'

export default {
  props: {
    to: {
      type: String,
      required: true
    }
  },
  computed: {
    linkProps() {
      if (isExternal(this.to)) {
        return {
          is: 'a',
          href: this.to,
          target: '_blank',
          rel: 'noopener'
        }
      }
      return {
        is: 'router-link',
        to: this.to
      }
    }
  }
}
</script>
