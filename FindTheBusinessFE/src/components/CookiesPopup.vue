<template>
  <div class="cookie-container uk-flex uk-flex-middle uk-flex-center">
    <p>Sunt de acord cu folosirea cookie-urilor in vederea salvarii datelor de autentificare si utilizarea serviciilor de plata</p>
    <button class="ok-button" @click="setHasAcceptedCookiesTrue">De acord</button>
  </div>
</template>

<script>
export default {
  name: 'CookiesPopup',
  methods: {
    setHasAcceptedCookiesTrue() {
      localStorage.setItem("has_accepted_cookies", true);
      this.$emit("set_has_accepted_cookies_true");
    }
  }
}
</script>

<style lang="scss" scoped>
.cookie-container {
  width: 100%;
  position: fixed;
  bottom: 0;
  right: 0;
  background-color: rgba(0, 0, 0, 0.856);
  color: white;
  padding: 5px;

  p {
    margin: 10px;
  }
}

.ok-button {
  border: none;
  background-color: #64c316;
  color: white;
  border-radius: 20px;
  padding: 5px 10px;
  outline: 0;
  cursor: pointer;
}

@media(max-width: 960px) {
  .cookie-container {
    margin-bottom: 70px;
    flex-direction: column;
  }
}
</style>