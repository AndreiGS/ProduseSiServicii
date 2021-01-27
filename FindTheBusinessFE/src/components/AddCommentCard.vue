<template>
  <vk-card padding="small" class="card">
    <vk-icons-plus :height="'30px'" :width="'30px'" class="plusButton" :class="{'plusButtonTransition' : showForm}" @click="toggleForm"/>
    <div v-show="showForm" style="text-align: left; margin-top: 15px;">
      <vk-card-title class="uk-text-center">Adauga o recenzie</vk-card-title>

      <form @submit.prevent="submitForm">
        <div>
          <div v-if="loading==false">
            <div v-if="formData.username==null" class="uk-align-center" style="margin-bottom: 0">
              <div class="uk-flex uk-flex-middle uk-flex-center" style="padding: 0">

                  <p style="font-size: 20px; display: inline; padding: 0; margin: 0; padding-right: 5px;">
                    Conecteaza-te cu:
                  </p>
                  <!--<vk-icon v-on:click="loginWithGoogle()" icon="google" ratio="1.5" style="color: #DB4437; cursor: pointer" />-->
                  <div class="login-button">
                    <vk-icons-facebook v-on:click="loginWithFacebook()" :height="'30px'" :width="'30px'" />
                  </div>
              </div>
              <div v-if="error == true" class="uk-text-center" style="margin-bottom: 0; margin-top: 20px">
                <p style="font-size: 20px; display: inline; padding: 0; margin: 0; padding-right: 5px;">
                  Ceva nu a functionat corespunzator. Va rugam sa reincercati
                </p>
              </div>
              <div v-if="error == 401" class="uk-text-center" style="margin-bottom: 0; margin-top: 20px">
                <p style="font-size: 20px; display: inline; padding: 0; margin: 0; padding-right: 5px;">
                  Nu puteti posta mai multe comentarii pentru acelasi magazin
                </p>
              </div>
            </div>
            <div v-else>
              <!--DESKTOP-->
              <div class="uk-visible@m uk-flex">
                <div class="uk-flex uk-flex-middle uk-flex-left uk-width-1-3" style="padding: 0;">

                    <p style="font-size: 20px; display: inline; padding: 0; margin: 0; padding-right: 5px;">
                      {{formData.username}}
                    </p>

                </div>

                <StarRating style="margin-bottom: 10px; padding: 0" class="uk-flex uk-flex-center uk-width-1-3"
                            :rating="formData.rating" @rating-selected="setRating"
                            :show-rating="false" :star-size="30" />
                <select class="uk-select uk-flex uk-flex-right uk-width-1-3" v-model="formData.price">
                  <option :value="null" disabled selected="selected">Cum ti s-a parut pretul?</option>
                  <option value="1">Ieftin</option>
                  <option value="2">Mediu</option>
                  <option value="3">Scump</option>
                </select>
              </div>

              <!--MOBILE-->
              <div class="uk-hidden@m">
                <div class="uk-flex uk-flex-middle uk-flex-center uk-width-1-1" style="padding: 0; margin-bottom: 20px">

                    <p style="font-size: 20px; display: inline; padding: 0; margin: 0; padding-right: 5px;">
                      {{formData.username}}
                    </p>

                </div>

                <StarRating style="margin-bottom: 10px; padding: 0" class="uk-flex uk-flex-center uk-width-1-1"
                            :rating="formData.rating" @rating-selected="setRating"
                            :show-rating="false" :star-size="30" />
                <select style="padding:0px" class="uk-select uk-flex uk-flex-center uk-width-1-1" v-model="formData.price">
                  <option :value="null" disabled selected="selected">Cum ti s-a parut pretul?</option>
                  <option value="1">Ieftin</option>
                  <option value="2">Mediu</option>
                  <option value="3">Scump</option>
                </select>
              </div>
            </div>
            <div v-if="formData.username!=null">
              <br>
              <textarea v-model="formData.comment" class="uk-textarea" style="resize: none" rows="3" placeholder="Scrie o recenzie!" />
              <br>
              <br>
              <button class="uk-button uk-button-primary uk-align-right" style="margin: 0" type="submit" value="Submit"> Adauga </button>
            </div>
          </div>
          <div v-else>
            <div class="uk-flex uk-flex-middle uk-flex-center uk-width-1-1" style="padding: 0; margin-bottom: 20px">
                <p style="font-size: 20px; display: inline; padding: 0; margin: 0; padding-right: 5px;">
                  Se incarca
                </p>
            </div>
          </div>
        </div>
      </form>
    </div>
  </vk-card>
</template>

<script>
import axios from 'axios'
const StarRating = () => import(/* webpackChunkName: "others-chunk" */ 'vue-star-rating');
import VkIconsPlus from '@vuikit/icons/lib/plus'
import VkIconsFacebook from '@vuikit/icons/lib/facebook'

export default {
    name: "AddCommentCard",
    components: {
        StarRating,
        VkIconsPlus,
        VkIconsFacebook,
    },
    data () {
        return {
          showForm: true,
          formData: {
              rating: 0,
              price: null,
              comment: "",
              username: null
          },
          loading: false,
          error: false,
          codeData: null,
          backend: process.env.VUE_APP_BACKEND || 'http://localhost:8080'
        }
    },
    props: {
      code: null
    },
    methods: {
        toggleForm () {
            this.showForm = !this.showForm;
        },
        setRating (rating) {
            this.formData.rating = rating;
        },
        async submitForm () {
            var error = 0;
            if(this.formData.rating == 0) {
              UIkit.notification({message: 'Trebuie sa selectati un rating', status: 'danger'})
              error = 1;
            }
              
            if(this.formData.comment == "") {
              UIkit.notification({message: 'Trebuie sa adaugati un mesaj', status: 'danger'})
              error = 1
            }
              
            if(this.formData.price == null) {
              UIkit.notification({message: 'Trebuie sa selectati un pret', status: 'danger'})
              error = 1;
            }

            if(error == 1)
              return;

            var timeoutVar = setTimeout(() => { this.loading = true; }, 1000);


            if(!this.$cookie.get("CSRF-TOKEN"))  {
              let csrfToken=Math.floor(Math.random() * 10000000);
              this.$cookie.set("CSRF-TOKEN", csrfToken, 7)
            }
            
            await axios({
              method: 'post',
              url: this.backend+"/api/comments/postComment?store="+this.$route.params.id,
              headers: {
                'X-CSRF-TOKEN': this.$cookie.get("CSRF-TOKEN")
              },
              data: {
                rating: this.formData.rating,
                price: this.formData.price,
                comment: this.formData.comment,
                username: this.formData.username
              },
              withCredentials: true
            })
              .then((response) => {
                this.formData.username = null;
                this.formData.price = null;
                this.formData.comment = "";
                this.formData.rating = 0;

                this.$emit('reload_comments');
                this.$emit('reload_rating');

                UIkit.notification({message: 'Comentariul dumneavoastra a fost adaugat!', status: 'success'})
              })
              .catch((error) => {
                this.codeData = null;
                this.error = true;
              })
              .finally(() => {
                this.loading = false;
                clearTimeout(timeoutVar)
              }) 
        },
        async loginWithFacebook() {
          var thisPage = process.env.VUE_APP_FRONTEND+"/store";
          var clientId = process.env.VUE_APP_FACEBOOK_CLIENT_ID;

          if(this.codeData==null) {
            window.location="https://www.facebook.com/v7.0/dialog/oauth?client_id="+ clientId +"&redirect_uri="+ thisPage + "&state={storeid=\""+this.$route.params.id+"\"}";
            return;
          }

          var timeoutVar = setTimeout(() => { this.loading = true; }, 1000);

          if(!this.$cookie.get("CSRF-TOKEN"))  {
            let csrfToken=Math.floor(Math.random() * 10000000);
            this.$cookie.set("CSRF-TOKEN", csrfToken, 7)
          }

          await axios({
            method: 'post',
            url: this.backend+"/api/authentication/loginWithFacebook?store="+this.$route.params.id,
            headers: {
              'X-CSRF-TOKEN': this.$cookie.get("CSRF-TOKEN")
            },
            data: {
              fbcode: this.codeData,
              url: thisPage
            },
            withCredentials: true
          })
            .then((response) => {
              this.formData.username = response.data.name;
              this.codeData = null;
            })
            .catch((error) => {
              this.codeData = null;
              
              if(error.response == null)
                this.error = true;
              else {
                if(error.response.status == 401) {
                  this.error = 401;
                } else {
                  this.error = true;
                }
              }
            })
            .finally(() => {
              let query = Object.assign({}, this.$route.query);
              delete query.code;
              delete query.state;
              this.$router.replace({ query });

              this.loading = false;
              clearTimeout(timeoutVar)
            }) 
        },
        loginWithGoogle() {
          
        }
    },
    created() {
      this.codeData = this.code;

      if(this.codeData!=null)
        this.loginWithFacebook();
    }
}
</script>

<style lang="scss" scoped>
.plusButton {
  transition: transform 0.2s ease-in-out;
  cursor: pointer;
}
.plusButtonTransition {
  transform: rotate(45deg);
}
.login-button {
  color: #4267B2;
  cursor: pointer; 
  border-radius: 50px; 
  padding: 5px;
  background-color: #e8e8e8a6;

  &:hover {
    background-color: #d6d6d6a6;
  }
}
</style>