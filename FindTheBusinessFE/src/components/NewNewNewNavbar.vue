<template>
  <div>
    <div class="uk-visible@m">
      <nav class="nav-margin uk-navbar-container navbar" uk-navbar>
        <div class="uk-navbar-left">
          <router-link :to="{name: 'Home'}" class="router">
            <button class="nav-button uk-flex uk-flex-column" :class="(page == 'home') ? 'active' : ''">
              <span uk-icon="icon: home; ratio: 1" style="align-self: center"></span>
              <p class="under-icon-text">Acasa</p>
            </button>
          </router-link>
          <router-link :to="{name: 'TermsAndConditions'}" class="router">
            <button class="nav-button uk-flex uk-flex-column" :class="(page == 'terms') ? 'active' : ''">
              <span uk-icon="icon: file-text; ratio: 1" style="align-self: center"></span>
              <p class="under-icon-text">Conditii</p>
            </button>
          </router-link>
          <router-link :to="{name: 'Tutorial'}" class="router">
            <button class="nav-button uk-flex uk-flex-column" :class="(page == 'tutorial') ? 'active' : ''">
              <span uk-icon="icon: info; ratio: 1" style="align-self: center"></span>
              <p class="under-icon-text">Utilizare</p>
            </button>
          </router-link>
        </div>
        <div class="uk-navbar-center">
          <router-link :to="{name: 'Home'}" class="router">
            <h3 class="uk-text-center title">ProduseSiServicii.ro</h3>
          </router-link>
        </div>
        <div class="uk-navbar-right">
          <button v-if="!logged" class="sign-up-button" href="#login-sections" uk-toggle>Conecteaza-ti afacerea</button>
          <router-link v-else :to="{name: 'Profile'}" class="router">
            <button class="nav-button uk-flex uk-flex-column" :class="(page == 'profile') ? 'active' : ''">
              <span uk-icon="icon: user; ratio: 1" style="align-self: center"></span>
              <p class="under-icon-text">Profil</p>
            </button>
          </router-link>
          <button v-if="shouldShowInstallButton" @click="installApp(); closeOffCanvas();" class="search-button"><span uk-icon="icon: download; ratio: 1.2"></span></button>
          <button class="search-button" href="#search-sections" uk-toggle ><span uk-icon="icon: search; ratio: 1.2"></span></button>
        </div>
      </nav>
    </div>

    <div uk-sticky="sel-target: .uk-navbar-container; cls-active: uk-navbar-sticky">
        <nav id="mobile-navigation-bar" class="uk-navbar-container uk-margin uk-hidden@m custom-bottom-navigation-bar" uk-navbar="dropbar: true; dropbar-mode: push">
            <div class="uk-navbar-left" :class="(page == 'home') ? 'active-div-mobile' : ''">
              <router-link :to="{name: 'Home'}" class="router">
                <button class="nav-button uk-flex uk-flex-column active">
                  <span uk-icon="icon: home; ratio: 1" style="margin: auto;"></span>
                  <p class="under-icon-text">Acasa</p>
                </button>
              </router-link>
            </div>

            <div class="uk-navbar-center">
              <button href="#search-sections" uk-toggle class="nav-button uk-flex uk-flex-column active">
                <span uk-icon="icon: search; ratio: 1" style="margin: auto;"></span>
                <p class="under-icon-text">Cauta</p>
              </button>
            </div>

            <div class="uk-navbar-right" :class="((page == 'profile' || page == 'terms' || page == 'tutorial') && logged == true) ? 'active-div-mobile' : ''">
              <div>
                <button class="nav-button uk-flex uk-flex-column active"  uk-toggle="target: #offcanvas-others-menu">
                  <span uk-icon="icon: menu; ratio: 1" style="margin: auto;"></span>
                  <p class="under-icon-text">Altele</p>
                </button>
              </div>

              <div id="offcanvas-others-menu" uk-offcanvas="overlay: true">
                <div class="uk-offcanvas-bar others-menu">

                    <button class="uk-offcanvas-close" type="button" uk-close></button>

                    <div class="uk-flex uk-flex-column uk-flex-middle">
                      
                    </div>

                    <h3>ProduseSiServicii.ro</h3>

                    <button v-if="shouldShowInstallButton" @click="installApp" class="others-nav-button uk-flex uk-flex-row uk-flex-middle">
                      <span uk-icon="icon: download; ratio: 1"></span>
                      <p class="under-icon-text-others">Instaleaza aplicatia</p>
                    </button>

                    <hr>

                    <div v-if="logged == false">
                      <button href="#login-sections" uk-toggle class="sign-up-button-mobile uk-flex uk-flex-row uk-flex-middle">
                        <span uk-icon="icon: user; ratio: 1"></span>
                        <p class="under-icon-text-others">Conecteaza-ti afacerea</p>
                      </button>
                    </div>
                    <router-link v-else :to="{name: 'Profile'}" class="router">
                      <button @click="closeOffCanvas()" class="others-nav-button uk-flex uk-flex-row  uk-flex-middle" :class="(page == 'profile'  && logged == true) ? 'active-div-mobile' : ''">
                        <span uk-icon="icon: user; ratio: 1"></span>
                        <p class="under-icon-text-others">Profil</p>
                      </button>
                    </router-link>

                    <router-link :to="{name: 'TermsAndConditions'}" class="router">
                      <button @click="closeOffCanvas()" class="others-nav-button uk-flex uk-flex-row  uk-flex-middle" :class="(page == 'terms') ? 'active-div-mobile' : ''">
                        <span uk-icon="icon: file-text; ratio: 1"></span>
                        <p class="under-icon-text-others">Termeni si Conditii</p>
                      </button>
                    </router-link>

                    <router-link :to="{name: 'Tutorial'}" class="router">
                      <button @click="closeOffCanvas()" class="others-nav-button uk-flex uk-flex-row  uk-flex-middle" :class="(page == 'tutorial') ? 'active-div-mobile' : ''">
                        <span uk-icon="icon: info; ratio: 1"></span>
                        <p class="under-icon-text-others">Utilizare</p>
                      </button>
                    </router-link>

                </div>
              </div>
            </div>
        </nav>
    </div>
    <SearchDialog />
    <LoginDialog />
    <RegisterDialog />
  </div>
</template>

<script>
const SearchDialog = () => import(/* webpackChunkName: "dialogs-chunk" */ '@/components/SearchDialog.vue')
const LoginDialog = () => import(/* webpackChunkName: "dialogs-chunk" */ '@/components/LoginDialog.vue')
const RegisterDialog = () => import(/* webpackChunkName: "dialogs-chunk" */ '@/components/RegisterDialog.vue')
import InstallMixin from '@/mixins/install.js'

export default {
  name: 'NewNewNewNavbar',
  data() {
        return {
            logged: false,
            page: 'home'
        }
    },
    components: {
        SearchDialog,
        LoginDialog,
        RegisterDialog,
    },
    mixins:[
      InstallMixin
    ],
    methods: {
      setPage() {
        if(this.$router.currentRoute.name == 'Home')
          this.page = 'home'
        else if(this.$router.currentRoute.name == 'Profile')
          this.page = 'profile'
        else if(this.$router.currentRoute.name == 'TermsAndConditions')
          this.page = 'terms'
        else if(this.$router.currentRoute.name == 'Tutorial')
          this.page = 'tutorial'
        else 
          this.page = ''
      },
      closeOffCanvas() {
        UIkit.offcanvas('#offcanvas-others-menu').hide();
      },
    },
    created() {
        return this.$store.watch(
            (state)=>{
              this.logged = this.$store.getters.getLogged != '' && this.$store.getters.getLogged != null && this.$store.getters.getLogged != undefined
            },
            (newValue, oldValue)=>{
            }
        )
    },
    mounted() {
      this.setPage();
    },
    watch: {
      $route (to, from) {
        this.setPage();
      }
    }
}
</script>

<style lang="scss" scoped>
.navbar {
  background: radial-gradient(circle, rgba(43, 43, 43, 1) 0%, #000000 50%);
  height: 60px;
}

.nav-margin {
  padding: 0 30px;
  margin-bottom: 0px!important;
}

#mobile-navigation-bar {
  background: radial-gradient(circle, rgba(43, 43, 43, 1) 0%, #000000 50%);
  position: fixed;
  left: 0;
  bottom: 0;
  overflow: hidden;
  margin-bottom: 0!important;
  padding: 2%;
  z-index: 100;
  width: 100%;
  padding: 0!important;
  height: 70px;

  .active-div-mobile {
    background: rgba(43, 43, 43, 1);
  }
}

.custom-padding-navbar {
    margin-right: 2%;
}

.custom-bottom-navigation-bar{
    padding-left: 0!important;
    padding-right: 0!important;
}

.active {
  p {
    display: inline-flex!important;
  }
}

.nav-button {
  outline: 0;
  border: none;
  height: 100%;
  width: 70px;
  margin: 0 10px;
  background: transparent;
  cursor: pointer;
  color: white;

  p {
    display: none; 
  }

  &:hover {
    p {
      display: inline-flex!important;
    }
  }
}

.others-nav-button {
  outline: 0;
  border: none;
  width: 100%;
  margin: 10px 0;
  padding: 10px;
  background: transparent;
  cursor: pointer;
  color: white;
}

.search-button {
  border: none;
  outline: 0;

  width: 50px;
  height: 50px;

  margin-left: 10px;

  border-radius: 50%;

  background-color: rgba(43, 43, 43, 1);
  color: white;

  font-weight: 700;

  cursor: pointer;

  &:hover {
    background-color: rgb(36, 36, 36);
  }
}

.install-button {
  border: none;
  outline: 0;

  width: 50px;
  height: 50px;

  margin-left: 10px;

  border-radius: 50%;

  background-color: #6fc42b;
  color: white;

  font-weight: 700;

  cursor: pointer;

  &:hover {
    background-color: #1e87f0;
  }
}

.sign-up-button {
  border: none;
  outline: 0;
  background-color: #ff0000b3;
  height: 60%;
  padding: 0 20px;
  color: white;
  border-radius: 20px;
  cursor: pointer;
  font-weight: 600;
  font-family: 'Montserrat'
}

.sign-up-button:hover {
  background-color: #ec0202b3;
}

.title {
  color: white;
  font-weight: 700;
  font-size: 30;
  margin: 0;
  cursor: pointer;
  font-family: 'Montserrat'
}

.router {
  border: none;
  outline: 0;
  text-decoration: none;
}

.under-icon-text {
  place-content: center;
  width: 100%;
  font-family: 'Montserrat';
  margin: 5px 0 0 0!important;
}

.under-icon-text-others {
  place-content: center;
  width: 100%;
  font-family: 'Montserrat';
  margin: 0!important;
  text-align: left;
  margin: 0 10px!important;
}

.others-menu {
  background: radial-gradient(circle, rgba(43, 43, 43, 1) 0%, #000000 50%);;
}

.sign-up-button-mobile {
  font-family: 'Montserrat';
  outline: 0;
  border: none;
  width: 100%;
  margin: 10px 0;
  padding: 10px;
  background-color: #ff0000b3;
  cursor: pointer;
  color: white;
}

@media(min-width: 960px) {
  .nav-margin {
    padding: 0 70px;
    margin-bottom: 30px;
  }
}

@media(max-width: 1079px) {
  .nav-margin {
    padding: 0 30px;
  }
}
</style>