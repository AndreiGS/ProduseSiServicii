<script>
import InstallMixin from '@/mixins/install.js'

export default {
  name: "InstallDialog",
  mixins: [
    InstallMixin
  ],
}
</script>

<template>
  <div v-if="shouldShow" class="overlay">
    <div class="container">
      <p v-if="hasInstalledAppNow == 0">Pentru o accesare mai usoara, recomandam instalarea aplicatiei. O puteti face ulterior si din meniul website-ului</p>
      <p v-else-if="hasInstalledAppNow == 1">Va multumim pentru ca ati ales sa descarcati aplicatia!</p>
      <p v-else-if="hasInstalledAppNow == 2">Intelegem optiunea dumneavoastra si speram sa va schimbam varianta in urma utilizarii website-ului</p>
      <div class="container__button">
        <button @click="close" class="button close">Inchide</button>
        <button v-if="hasInstalledAppNow == 0" @click="installApp" class="button install">Instaleaza</button>
      </div>
    </div>
  </div>
</template>

<style lang="scss" scoped>
.overlay {
  position: fixed;
  height: 100%;
  width: 100%;
  z-index: 200;
  background: #0000006e;
}

.container {
  position: fixed;
  top: 60px;
  right: 0px;
  border-radius: 0 0 0 500px;
  padding: 50px;
  background: linear-gradient(90deg, rgba(43,43,43,1) 0%, rgba(0,0,0,1) 50%, rgba(0,0,0,1) 100%);
  color: white;
  text-align: right;
  max-width: 25%;

  .container__button {
    display: flex;
    flex-direction: row;
    justify-content: flex-end;

    .button {
      border: none;
      border-radius: 20px;
      padding: 5px 10px;
      outline: 0;
      cursor: pointer;
      margin: 0 10px;
    }

    .close {
      background-color: transparent;
      color: #ffffffc7;
    }

    .install {
      background-color: #6fc42b;
      color: white;

      &:hover {
        background-color: #1e87f0
      }
    }
  }
}

@media (max-width: 959px) {
  .container {
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    background: radial-gradient(circle, rgba(43, 43, 43, 1) 0%, #000000 50%);
    border-radius: 50px;
    text-align: center;
    max-width: 50%;

    .container__button {
      flex-direction: column;
      place-items: center;

      .install {
        margin-top: 10px;
      }
    }
  }
}

@media (max-width: 560px) {
  .container {
    padding: 30px;
    width: 70%;
  }
}
</style>