<template>
  <div>
    <div class="uk-visible@m">
      <nav class="nav-margin uk-navbar-container navbar" uk-navbar>
        <div class="uk-navbar-left">
          <img src="../assets/4k.jpg" width="100px" alt="">
        </div>
        <div class="uk-navbar-right">
          <router-link :to="{name: 'Home'}"><button class="nav-button" :class="(page == 'home') ? 'active' : ''">Acasa</button></router-link>
          <button v-if="!logged" class="nav-button" href="#login-sections" uk-toggle>Ofera serviciile tale</button>
          <router-link v-else :to="{name: 'Profile'}">
            <button class="nav-button" :class="(page == 'profile') ? 'active' : ''">
              Profil
            </button>
          </router-link>
          <button class="nav-button search-button" href="#search-sections" uk-toggle ><span uk-icon="icon: search; ratio: 1"></span></button>
        </div>
      </nav>
    </div>

    <div uk-sticky="sel-target: .uk-navbar-container; cls-active: uk-navbar-sticky">
        <nav id="mobile-navigation-bar" class="uk-navbar-container uk-margin uk-hidden@m custom-bottom-navigation-bar" uk-navbar>
            <div class="uk-navbar-left">
              <router-link :to="{name: 'Home'}"><button @click="page='home'" class="uk-button uk-button-default custom-mobile-nav-button" :class="(page == 'home') ? 'active' : ''">Acasa</button></router-link>
            </div>

            <div class="uk-navbar-center">
              <button class="uk-button uk-button-default custom-mobile-nav-button" href="#search-sections" uk-toggle>Cauta</button>
            </div>

            <div class="uk-navbar-right">
                <button v-if="!logged" class="uk-grid-row-small uk-button uk-button-default custom-mobile-nav-button" href="#login-sections" uk-toggle>Ofer</button>
                <router-link v-else :to="{ name: 'Profile' }">
                  <button @click="page='profile'" class="uk-grid-row-small uk-button uk-button-default custom-mobile-nav-button" :class="(page == 'profile') ? 'active' : ''">
                    Profil
                  </button>
                </router-link>
            </div>
        </nav>
    </div>
    <SearchDialog />
    <LoginDialog />
    <RegisterDialog />
  </div>
</template>

<script>
const SearchDialog = () => import('@/components/SearchDialog.vue')
const LoginDialog = () => import('@/components/LoginDialog.vue')
const RegisterDialog = () => import('@/components/RegisterDialog.vue')

export default {
  name: 'NewNewNavbar',
  data() {
        return {
            logged: null,
            page: 'home'
        }
    },
    components: {
        SearchDialog,
        LoginDialog,
        RegisterDialog
    },
    methods: {
      setPage() {
        if(this.$router.currentRoute.name == 'Home')
          this.page = 'home'
        else if(this.$router.currentRoute.name == 'Profile')
          this.page = 'profile'
        else 
          this.page = ''
      }
    },
    created() {
        return this.$store.watch(
            (state)=>{
                this.logged = this.$store.getters.getLogged
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
  background: linear-gradient(90deg, white, rgba(111, 196, 43, 0.2), #6fc42b);
}

.active {
  //border-bottom: 2px solid white!important;
  color: white!important;
  font-weight: 600!important;
}

.nav-button {
  outline: 0;
  border: none;
  height: 100%;
  margin: 0 10px;
  background: transparent;
  font-size: 17px;
  cursor: pointer;
  color: black;

  &:hover, &:focus {
    color: white!important;
  }
}

.search-button {
  margin-left: 10px;
}

.nav-margin {
  padding: 0 30px;
  margin-bottom: 30px;
}

.uk-logo {
    color: black;
    font-weight: 600;
}

.custom-desktop-nav-button {
    margin-right: 10px;
    border: 2px solid rgba(31, 182, 33, 0.301);
    background-color: #64c316;
    color: black;
    //border: 2px solid #AAAAAA;
    //background-color: black;
    //color: white;
    font-weight: 600;
    border-radius: 20px;
    &:hover, &:focus {
        color: white;
        border: 2px solid rgba(31, 182, 33, 0.301);
    }
}

.custom-mobile-nav-button {
    margin-right: 2px;
    color: black;
    font-weight: 600;
    border: none;
    &:hover, &:focus {
        color:white;
    }
}

#desktop-navigation-bar {
    background-color: #6fc42b;
    //background-color: black;
}

#mobile-navigation-bar {
    background-color: #6fc42b;
    position: fixed;
    left: 0;
    bottom: 0;
    overflow: hidden;
    margin-bottom: 0!important;
    padding: 2%;
    z-index: 100;
    width: 100%;
}

.custom-padding-navbar {
    margin-right: 2%;
}

.custom-bottom-navigation-bar{
    padding-left: 0!important;
    padding-right: 0!important;
}

.margin-div {
  height: 30px;
  background-color: rgba(42, 42, 42, 0.15);
}

@media(min-width: 960px) {
  .nav-margin {
    padding: 0 70px;
    margin-bottom: 30px;
  }
}
</style>