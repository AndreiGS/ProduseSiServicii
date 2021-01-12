<template>
  <div :style="!$store.getters.getHasCompletedTutorial ? 'margin-top: 100px;' : ''">
    <h3 v-if="loadingImage == true">Se incarca</h3>
    <h3 v-if="errorImage == true">Nu am putut incarca imaginea. Va rugam reincercati</h3>
    <div class="uk-flex@s uk-flex-row uk-flex-center uk-flex-middle">
      <!--<div v-if="loading == false" class="button-wrapper">
        <span class="button uk-button uk-button-primary" @click="resetImage">
          Reseteaza
        </span>
      </div>-->
      <div  v-if="!$store.getters.getHasCompletedTutorial" class="overlay"></div>
      <div style="margin-bottom: 20px;" class="uk-flex-row uk-text-center">
        <vk-button @click="closeModal()" class="uk-margin-small-right uk-margin-small-left">Inchide</vk-button>
        <vk-button v-if="loadingImage == false && uploadedImage != null" @click="saveNewImage(getEdittedImage())" :class="!this.$store.getters.getHasCompletedTutorial && (this.$store.getters.getTutorialStep == 6 || this.$store.getters.getTutorialStep == 29) ? 'in-focus' : ''" class="uk-button-primary uk-margin-small-right uk-margin-small-left">Adauga</vk-button>
      </div>

      <div v-if="loadingImage == false" :class="!this.$store.getters.getHasCompletedTutorial && (this.$store.getters.getTutorialStep == 5 || this.$store.getters.getTutorialStep == 28) ? 'in-focus' : ''" class="button-wrapper" style="margin-bottom: 20px;">
        <span class="button uk-button uk-button-primary" @click="$refs.file.click()">
          <input type="file" ref="file" @change="uploadImage($event)" accept="image/*">
          Incarca {{uploadedImage != null ? 'alta imagine' : 'imagine'}}
        </span>
      </div>
      <div v-if="loadingImage == false" :class="!this.$store.getters.getHasCompletedTutorial && (this.$store.getters.getTutorialStep == 6 || this.$store.getters.getTutorialStep == 29) ? 'in-focus' : ''" class="button-wrapper" style="padding-bottom: 20px;">
        <span class="button uk-button uk-button-primary" @click="rotateImage">
          Roteste
        </span>
      </div>
    </div>
    <div :class="!this.$store.getters.getHasCompletedTutorial && (this.$store.getters.getTutorialStep == 6 || this.$store.getters.getTutorialStep == 29) ? 'in-focus' : ''" style="margin-bottom: 20px;">
      <Cropper
        classname="cropper"
        ref="cropper"
        :src="(uploadedImage != null) ? uploadedImage : null"
        :stencilProps="{
          aspectRatio: aspectRatio,
          handlers: {
            eastNorth: true,
            north: false,
            westNorth: true,
            west: false,
            westSouth: true,
            south: false,
            eastSouth: true,
            east: false,
          }
        }"
        @ready="setLoadingAndError(false, false)"
        @error="setLoadingAndError(false, true)"
      />
    </div>
  </div>
</template>

<script>
import { Cropper } from 'vue-advanced-cropper'

export default {
  name: 'CropperDialogComponent',
  components: {
    Cropper
  },
  props: {
    oldImage: null,
    aspectRatio: null,
    minHeight: null,
    minWidth: null,
    from: null
  },
  data() {
    return {
      uploadedImage: null,
      loadingImage: false,
      errorImage: false
    }
  },
  methods: {
    setLoadingAndError(loading, error) {
      this.loadingImage = loading;
      this.errorImage = error;
    },
    pixelsRestriction({minWidth, minHeight, maxWidth, maxHeight, imageWidth, imageHeight}) {
			return {
				minWidth: minWidth,
				minHeight: minHeight,
				maxWidth: maxWidth,
				maxHeight: maxHeight,
			};
		},
    closeModal() {
      this.$emit('hide_modal');
      this.loadingImage=false;
      this.uploadedImage = null
    },
    saveNewImage(data) {
      let object = {
        newImage: data,
        from: this.from,
      }
      this.$store.dispatch('changeTutorialStep', this.$store.getters.getTutorialStep+1)

      this.$emit('change_image', object)
    },
    getEdittedImage() {
      const { coordinates, canvas, } = this.$refs.cropper.getResult();
      this.coordinates = coordinates;
      try {
        let quality = 0.2;
        if(this.from.includes('storefront'))
          quality = 0.3
        else if(this.from == 'productcard')
          quality = 0.1

        let data = {
          //id: this.item.id,
          //image: this.$refs.cropper.getCroppedCanvas().toDataURL()
          image: canvas.toDataURL('image/jpeg', quality)
        }
        if(!this.from.includes('storefront') && this.$store.getters.getHasCompletedTutorial)
          UIkit.notification({message: 'Poza dumneavoastra a fost modificata', status: 'success'})
        return data;
      } catch(error) {
        this.errorImage=true;
        this.loadingImage=false;
        if(!this.from.includes('storefront'))
          UIkit.notification({message: 'Nu am putut modifica poza. Va rugam sa reincercati pagina si sa reincercati', status: 'danger'})
      }
      return null;
    },
    resetImage() {
      this.$refs.cropper.reset();
    },
    uploadImage(event) {
      this.errorImage = false;
      var input = event.target;
      if (input.files && input.files[0]) {
        var reader = new FileReader();
        reader.onload = (e) => {
          this.uploadedImage = e.target.result

          var image = new Image();
          var vm = this;
          image.src = this.uploadedImage;

          image.onload = function () {
            vm.$store.dispatch('changeTutorialStep', vm.$store.getters.getTutorialStep+1)
            var height = this.height;
            var width = this.width;
            if (height < this.minHeight || width < this.minWidth) {
              UIkit.notification({message: 'Pentru o calitate buna imaginea trebuie sa aiba minim '+ this.minHeight + 'px inaltime si '+ this.minWidth +'px latime', status: 'warning'})
            }
          };
        };
        reader.onerror = (e) => {
          this.errorImage = true;
        }
        reader.readAsDataURL(input.files[0]);
      }
    },
    rotateImage() {
      var image = document.createElement("img");
      image.crossOrigin = "anonymous";
      image.src = this.uploadedImage || this.oldImage;
      image.onload = () => {
        var canvas = document.createElement("canvas");
        var ctx = canvas.getContext("2d");

        if (image.width > image.height) {
          canvas.width = image.height;
          canvas.height = image.width;
          ctx.translate(image.height, image.width / image.height);
        } else {
          canvas.height = image.width;
          canvas.width = image.height;
          ctx.translate(image.height, image.width / image.height);
        }
        ctx.rotate(Math.PI / 2);
        ctx.drawImage(image, 0, 0);
        this.uploadedImage = canvas.toDataURL();
      };
    },
  }
}
</script>

<style lang="scss" scoped>
.button input {
	display: none;
}

.cropper {
  height: 500px;
  .vue-advanced-cropper__area {
    border-style: double;
    border-color: red;
    border-width: 5px;
  }
}
</style>