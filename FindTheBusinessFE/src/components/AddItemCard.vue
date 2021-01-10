<template>
  <vk-card padding="small" class="card" @click="addShop()">
    <vk-icons-plus :height="'30px'" :width="'30px'" class="plusButton"/>
  </vk-card>
</template>

<script>
import axios from 'axios'
import VkIconsPlus from '@vuikit/icons/lib/plus'

export default {
  name: 'AddItemCard',
  data() {
    return {
      backend: process.env.VUE_APP_BACKEND || 'http://localhost:8080',
    }
  },
  components: {
    VkIconsPlus,
  },
  methods: {
    async checkIfCanAddShop() {
      var timeoutVar = setTimeout(() => { this.loading = true; }, 1000);
      let error = false

      await axios({
        url: this.backend+'/api/user/checkIfCanAddItem?id='+this.$route.params.id,
        method: 'post',
        headers: {
          'X-CSRF-TOKEN': this.$cookie.get('CSRF-TOKEN'),
          'X-REFRESH-TOKEN': this.$cookie.get('REFRESH-TOKEN')
        },
        withCredentials: true
      })
        .then((response) => {
          this.$cookie.set("CSRF-TOKEN", response.data.csrfToken, 7);
          this.$cookie.set("REFRESH-TOKEN", response.data.refreshToken, 7);
        })
        .catch((error) => {
          if(error.response.status == 403)
            UIkit.notification({message: 'Nu puteti sa adaugati un nou produs deoarece ati ajuns la limita maxima', status: 'danger'})
          else
            UIkit.notification({message: 'Nu am reusit sa adaugam un nou produs. Va rugam reincercati', status: 'danger'})

          this.error = true
        })
        .finally(() => {
          this.loading = false
          clearTimeout(timeoutVar)
        })

      return error
    },
    async addShop() {
      let error = await this.checkIfCanAddShop();

      if(this.error == true)
        return

      this.$emit('add_item')
    }
  }
}
</script>

<style lang="scss" scoped>
.plusButton {
  transition: transform 0.2s ease-in-out;
}
</style>